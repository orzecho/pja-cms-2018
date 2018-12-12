package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FillingGapsTestItem entity.
 */
public class FillingGapsTestItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

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
