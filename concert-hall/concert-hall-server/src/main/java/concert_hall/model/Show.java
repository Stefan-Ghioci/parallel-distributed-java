package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Show implements BaseEntity<Integer> {
    private Integer id;
    private Date date;
    private String title;
    private String desc;
}
