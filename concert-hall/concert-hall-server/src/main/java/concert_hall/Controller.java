package concert_hall;

import concert_hall.model.SeatType;
import concert_hall.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("concert_hall")
public class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    AsyncService service;

    public Controller(AsyncService service) {
        this.service = service;
    }

    @PostMapping("/tickets")
    public @ResponseBody
    ResponseEntity<?> sellTickets(@RequestParam(name = "showID") Integer showID,
                                  @RequestParam(name = "front") Integer front,
                                  @RequestParam(name = "middle") Integer middle,
                                  @RequestParam(name = "back") Integer back) {
        String requestInfo = "show with ID=" + showID + " for seats - front=" + front + ", middle=" + middle + ", back=" + back;

        logger.info("Sell request at " + requestInfo);
        Boolean success = service.sellTickets(showID, front, middle, back).join();

        logger.info(success ?
                "Sell success for request at" + requestInfo :
                "Sell denied for request at " + requestInfo + ", not enough seats");

        return new ResponseEntity<>(success ? HttpStatus.OK : HttpStatus.FORBIDDEN);
    }

    @GetMapping("/shows")
    public @ResponseBody
    Iterable<Show> getShows() {
        return service.getShows().join();
    }

    @GetMapping("/seats")
    public @ResponseBody
    Map<SeatType, Pair<Double, Integer>> getSeatInfo(@RequestParam(name = "showID") Integer showID) {
        return service.getAvailableSeatInfo(showID).join();
    }


}
