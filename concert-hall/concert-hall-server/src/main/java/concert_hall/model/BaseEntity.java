package concert_hall.model;

import java.io.Serializable;

public interface BaseEntity<ID> extends Serializable {
    ID getId();

    @SuppressWarnings("unused")
    void setId(ID id);
}
