package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
