package concert_hall;

import concert_hall.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    private final static Logger logger = LoggerFactory.getLogger(AsyncService.class);

    private final Repository<Balance, Double> balanceRepository;
    private final Repository<Price, SeatType> priceRepository;
    private final Repository<Sale, Integer> saleRepository;
    private final Repository<Seat, Integer> seatRepository;
    private final Repository<Show, Integer> showRepository;
    private final Repository<Ticket, ThreeKeyTuple<Integer>> ticketRepository;

    @Async("asyncExecutor")
    public synchronized CompletableFuture<Map<SeatType, Pair<Double, Integer>>> getAvailableSeatInfo(Integer showID) {
        Map<SeatType, Pair<Double, Integer>> availableSeatInfo = new HashMap<>();
        priceRepository.findAll().forEach(price -> {
            int availableSeatsPerType = (int) seatRepository.findAll().stream()
                    .filter(seat -> seat.getType() == price.getType() &&
                            ticketRepository.findAll().stream()
                                    .noneMatch(ticket -> ticket.getShowID().equals(showID) &&
                                            ticket.getSeatID().equals(seat.getId())))
                    .count();
            availableSeatInfo.put(price.getType(), Pair.of(price.getValue(), availableSeatsPerType));
        });
        return CompletableFuture.completedFuture(availableSeatInfo);
    }

    @Async("asyncExecutor")
    public synchronized CompletableFuture<Collection<Show>> getShows() {
        return CompletableFuture.completedFuture(showRepository.findAll());
    }

    @Async("asyncExecutor")
    public synchronized CompletableFuture<Boolean> sellTickets(Integer showID, Integer front, Integer middle, Integer back) {

        Date date = new Date(System.currentTimeMillis());

        Integer saleID = saleRepository.count();

        Map<SeatType, Integer> seats = new HashMap<>() {{
            put(SeatType.Back, back);
            put(SeatType.Middle, middle);
            put(SeatType.Front, front);
        }};

        Boolean created = createTickets(saleID, showID, seats);

        if (created)
            saleRepository.save(new Sale(saleID, date));

        return CompletableFuture.completedFuture(created);
    }

    public AsyncService() {
        balanceRepository = new Repository<>("Balance");
        priceRepository = new Repository<>("Price");
        saleRepository = new Repository<>("Sale");
        seatRepository = new Repository<>("Seat");
        showRepository = new Repository<>("Show");
        ticketRepository = new Repository<>("Ticket");

        initializePrices();
        initializeSeats();
        initializeShows();
        initializeBalance();

    }

    private void initializeBalance() {
        if (balanceRepository.count() == 0)
            balanceRepository.save(new Balance(0.0));
    }

    private void initializeShows() {
        if (showRepository.count() == 0) {
            showRepository.save(new Show(0,
                    Date.valueOf("2020-10-04"),
                    "Theatre Russian Ballet - Lacul Lebedelor",
                    "Cea mai prestigioasă trupă de Balet Rus din Sankt Petersburg vine pentru prima oară în România cu spectacolul Lacul Lebedelor ce va avea loc la Opera Națională din București pe 9 februarie 2020."));
            showRepository.save(new Show(1,
                    Date.valueOf("2020-08-29"),
                    "The Mono Jacks - lansare album",
                    "Trupa este o colecție de gânduri și emoții puse pe note, care a început să prindă viață în toamna lui 2008 la inițiativa lui Doru Trăscău. Formația a avut parte de un debut excelent, a lansat până acum un album și două EP-uri, și a adunat o bogată experiență de live atât în cluburi cât și la mari festivaluri."));
            showRepository.save(new Show(2,
                    Date.valueOf("2020-06-12"),
                    "The Illusionists",
                    "The Illusionists duc mai departe tradiția magiei de scenă într-o nouă eră, dincolo de limitele imaginației umane, purtându-și audiența într-un univers palpitant unde granița dintre realitate și iluzie dispare."));
        }
    }

    private void initializeSeats() {
        if (seatRepository.count() == 0) {
            for (int i = 0; i < 50; i++)
                seatRepository.save(new Seat(i, SeatType.Front));
            for (int i = 50; i < 150; i++)
                seatRepository.save(new Seat(i, SeatType.Middle));
            for (int i = 150; i < 200; i++)
                seatRepository.save(new Seat(i, SeatType.Back));
        }
    }

    private void initializePrices() {
        if (priceRepository.count() == 0) {
            priceRepository.save(new Price(SeatType.Front, 15.99));
            priceRepository.save(new Price(SeatType.Middle, 12.99));
            priceRepository.save(new Price(SeatType.Back, 9.99));
        }
    }

    private Boolean createTickets(Integer saleID, Integer showID, Map<SeatType, Integer> seats) {

        if (!checkAvailability(showID, seats))
            return false;

        reserveAvailableSeats(saleID, showID, seats);


        updateBalance(seats);

        return true;
    }

    private void updateBalance(Map<SeatType, Integer> seats) {
        Double totalPrice = seats.entrySet().stream()
                .mapToDouble(entry -> priceRepository.findById(entry.getKey()).getValue() * entry.getValue())
                .sum();

        Balance oldBalance = balanceRepository.findAll().iterator().next();
        balanceRepository.deleteById(oldBalance.getId());

        balanceRepository.save(new Balance(oldBalance.getAmount() + totalPrice));
    }

    private void reserveAvailableSeats(Integer saleID, Integer showID, Map<SeatType, Integer> seats) {
        seats.forEach((seatType, seatsByType) -> {
            Integer reservedByType = 0;

            while (!reservedByType.equals(seatsByType)) {
                Optional<Seat> unreservedSeat = seatRepository.findAll().stream()
                        .filter(seat -> seat.getType() == seatType &&
                                ticketRepository.findAll().stream()
                                        .noneMatch(ticket -> ticket.getShowID().equals(showID) &&
                                                ticket.getSeatID().equals(seat.getId())))
                        .findFirst();
                unreservedSeat.ifPresent(seat -> ticketRepository.save(new Ticket(seat.getId(), showID, saleID)));
                reservedByType++;
            }
        });
    }

    private Boolean checkAvailability(Integer showID, Map<SeatType, Integer> seats) {
        for (Map.Entry<SeatType, Integer> entry : seats.entrySet()) {
            SeatType seatType = entry.getKey();
            Integer seatsByType = entry.getValue();

            long totalSeatsByType = seatRepository.findAll().stream()
                    .filter(seat -> seat.getType().equals(seatType))
                    .count();
            long soldTicketsByType = ticketRepository.findAll().stream()
                    .filter(ticket -> ticket.getShowID().equals(showID) &&
                            seatRepository.findById(ticket.getSeatID()).getType() == seatType)
                    .count();
            if (soldTicketsByType + seatsByType > totalSeatsByType)
                return false;
        }
        return true;
    }
}
