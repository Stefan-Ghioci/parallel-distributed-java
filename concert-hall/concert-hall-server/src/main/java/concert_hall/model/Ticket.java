package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket implements BaseEntity<ThreeKeyTuple<Integer>> {
    private Integer seatID;
    private Integer showID;
    private Integer saleID;

    @Override
    public ThreeKeyTuple<Integer> getId() {
        return new ThreeKeyTuple<>(seatID,showID,saleID);
    }

    @Override
    public void setId(ThreeKeyTuple<Integer> integerThreeKeyTuple) {
        seatID = integerThreeKeyTuple.getKey1();
        showID = integerThreeKeyTuple.getKey2();
        saleID = integerThreeKeyTuple.getKey3();
    }
}
