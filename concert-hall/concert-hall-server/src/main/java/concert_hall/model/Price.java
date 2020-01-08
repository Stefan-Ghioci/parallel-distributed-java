package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

@Data
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
