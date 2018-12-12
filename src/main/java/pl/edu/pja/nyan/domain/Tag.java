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

import lombok.NoArgsConstructor;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tag_lesson",
               joinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "lessons_id", referencedColumnName = "id"))
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tag_filling_gaps_test_item",
               joinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "filling_gaps_test_items_id", referencedColumnName = "id"))
    private Set<FillingGapsTestItem> fillingGapsTestItems = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Word> words = new HashSet<>();

    public Tag(String name, Lesson lesson) {
        this.name = name;
        this.addLesson(lesson);
    }

    public Tag(String name, Word word) {
        this.name = name;
        this.addWord(word);
    }

    public Tag(String name) {
        this.name = name;
    }

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

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Tag lessons(Set<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }

    public Tag addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.getTags().add(this);
        return this;
    }

    public Tag removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.getTags().remove(this);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Set<FillingGapsTestItem> getFillingGapsTestItems() {
        return fillingGapsTestItems;
    }

    public Tag fillingGapsTestItems(Set<FillingGapsTestItem> fillingGapsTestItems) {
        this.fillingGapsTestItems = fillingGapsTestItems;
        return this;
    }

    public Tag addFillingGapsTestItem(FillingGapsTestItem fillingGapsTestItem) {
        this.fillingGapsTestItems.add(fillingGapsTestItem);
        fillingGapsTestItem.getTags().add(this);
        return this;
    }

    public Tag removeFillingGapsTestItem(FillingGapsTestItem fillingGapsTestItem) {
        this.fillingGapsTestItems.remove(fillingGapsTestItem);
        fillingGapsTestItem.getTags().remove(this);
        return this;
    }

    public void setFillingGapsTestItems(Set<FillingGapsTestItem> fillingGapsTestItems) {
        this.fillingGapsTestItems = fillingGapsTestItems;
    }

    public Set<Word> getWords() {
        return words;
    }

    public Tag words(Set<Word> words) {
        this.words = words;
        return this;
    }

    public Tag addWord(Word word) {
        this.words.add(word);
        word.getTags().add(this);
        return this;
    }

    public Tag removeWord(Word word) {
        this.words.remove(word);
        word.getTags().remove(this);
        return this;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
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
        Tag tag = (Tag) o;
        if (tag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
