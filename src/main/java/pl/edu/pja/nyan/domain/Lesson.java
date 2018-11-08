package pl.edu.pja.nyan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Lesson.
 */
@Entity
@Table(name = "lesson")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "lesson")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LessonFile> lessonFiles = new HashSet<>();

    @ManyToMany(mappedBy = "lessons")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Lesson name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Lesson description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LessonFile> getLessonFiles() {
        return lessonFiles;
    }

    public Lesson lessonFiles(Set<LessonFile> lessonFiles) {
        this.lessonFiles = lessonFiles;
        return this;
    }

    public Lesson addLessonFile(LessonFile lessonFile) {
        this.lessonFiles.add(lessonFile);
        lessonFile.setLesson(this);
        return this;
    }

    public Lesson removeLessonFile(LessonFile lessonFile) {
        this.lessonFiles.remove(lessonFile);
        lessonFile.setLesson(null);
        return this;
    }

    public void setLessonFiles(Set<LessonFile> lessonFiles) {
        this.lessonFiles = lessonFiles;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Lesson tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Lesson addTag(Tag tag) {
        this.tags.add(tag);
        tag.getLessons().add(this);
        return this;
    }

    public Lesson removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getLessons().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lesson lesson = (Lesson) o;
        if (lesson.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lesson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Lesson{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
