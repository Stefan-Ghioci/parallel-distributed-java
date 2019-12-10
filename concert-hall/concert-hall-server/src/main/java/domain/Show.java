package domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Show
{
   private @Id @GeneratedValue Long id;
   private Timestamp date;
   private String title;
   private String desc;
}
