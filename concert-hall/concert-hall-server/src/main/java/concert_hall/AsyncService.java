package concert_hall;

import concert_hall.model.Price;
import concert_hall.model.SeatType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {


    public AsyncService() {
        test();
    }

    @Async("asyncExecutor")
    public void test() {
        Repository<Price, SeatType> repository = new Repository<>("prices.txt");

        Price front = new Price();
        front.setType(SeatType.Front);
        front.setValue(15.99);

        Price middle = new Price();
        middle.setType(SeatType.Middle);
        middle.setValue(12.99);

        Price back = new Price();
        back.setType(SeatType.Back);
        back.setValue(9.99);

        repository.save(front);
        repository.save(middle);
        repository.save(back);
    }
}
