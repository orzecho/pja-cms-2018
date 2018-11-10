package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

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
public class LessonFileDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] content;
    private String contentContentType;

    private Long lessonId;

    private String lessonName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LessonFileDTO lessonFileDTO = (LessonFileDTO) o;
        if (lessonFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lessonFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LessonFileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", lesson=" + getLessonId() +
            ", lesson='" + getLessonName() + "'" +
            "}";
    }
}
