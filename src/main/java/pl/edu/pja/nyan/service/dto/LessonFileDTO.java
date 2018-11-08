package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the LessonFile entity.
 */
public class LessonFileDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    
    @Lob
    private byte[] content;
    private String contentContentType;

    private Long lessonId;

    private String lessonName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

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
