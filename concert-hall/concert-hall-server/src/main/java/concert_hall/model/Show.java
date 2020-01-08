package concert_hall.model;

import concert_hall.BaseEntity;
import lombok.Data;

import java.sql.Date;

@Data
public class Show implements BaseEntity<Integer> {
    private Integer id;
    private Date date;
    private String title;
    private String desc;
    private Integer sold;
}
