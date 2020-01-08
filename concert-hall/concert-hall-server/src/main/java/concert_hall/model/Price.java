package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price implements BaseEntity<SeatType> {
    SeatType type;
    Double value;

    @Override
    public SeatType getId() {
        return type;
    }

    @Override
    public void setId(SeatType seatType) {
        type = seatType;
    }
}
