package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

import java.sql.Date;

@Data
public class Sale implements BaseEntity<Integer> {
    private Integer id;
    private Date date;
}
