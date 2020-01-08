package concert_hall.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public@AllArgsConstructor
class ThreeKeyTuple<T> implements Serializable {
    private T key1;
    private T key2;
    private T key3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreeKeyTuple<?> that = (ThreeKeyTuple<?>) o;
        return Objects.equals(key1, that.key1) &&
                Objects.equals(key2, that.key2) &&
                Objects.equals(key3, that.key3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key1, key2, key3);
    }
}
