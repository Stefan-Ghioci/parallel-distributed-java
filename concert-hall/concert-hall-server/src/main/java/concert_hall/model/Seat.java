package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

@Data
public class Seat implements BaseEntity<Integer> {
    private Integer id;
    private SeatType type;
}
