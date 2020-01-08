package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

@Data
public class Balance implements BaseEntity<Double> {
    private Double amount;

    @Override
    public Double getId() {
        return amount;
    }

    @Override
    public void setId(Double aDouble) {
        amount = aDouble;
    }

}
