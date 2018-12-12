package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FillingGapsTestItemDTO implements Serializable {

    private Long id;
    @NotNull
    private String question;
    private List<GapItemDTO> gapItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FillingGapsTestItemDTO fillingGapsTestItemDTO = (FillingGapsTestItemDTO) o;
        if (fillingGapsTestItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fillingGapsTestItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FillingGapsTestItemDTO{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
