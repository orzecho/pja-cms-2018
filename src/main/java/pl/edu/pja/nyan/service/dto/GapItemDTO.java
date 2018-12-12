package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GapItem entity.
 */
public class GapItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    private Long testItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTestItemId() {
        return testItemId;
    }

    public void setTestItemId(Long fillingGapsTestItemId) {
        this.testItemId = fillingGapsTestItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GapItemDTO gapItemDTO = (GapItemDTO) o;
        if (gapItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gapItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GapItemDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", testItem=" + getTestItemId() +
            "}";
    }
}
