package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat implements BaseEntity<Integer> {
    private Integer id;
    private SeatType type;
}
