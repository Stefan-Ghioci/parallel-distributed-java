package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

@Data
public class Ticket implements BaseEntity<ThreeKeyTuple<Integer>> {
    private Integer seatID;
    private Integer showID;
    private Integer saleID;

    @Override
    public ThreeKeyTuple<Integer> getId() {
        ThreeKeyTuple<Integer> keyTuple = new ThreeKeyTuple<>();
        keyTuple.setKey1(seatID);
        keyTuple.setKey2(showID);
        keyTuple.setKey3(saleID);
        return keyTuple;
    }

    @Override
    public void setId(ThreeKeyTuple<Integer> integerThreeKeyTuple) {
        seatID = integerThreeKeyTuple.getKey1();
        showID = integerThreeKeyTuple.getKey2();
        saleID = integerThreeKeyTuple.getKey3();
    }
}
