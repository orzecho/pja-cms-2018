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
 * A FillingGapsTestItem.
 */
@Entity
@Table(name = "filling_gaps_test_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FillingGapsTestItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @OneToMany(mappedBy = "testItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GapItem> gapItems = new HashSet<>();

    @ManyToMany(mappedBy = "fillingGapsTestItems")
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

    public String getQuestion() {
        return question;
    }

    public FillingGapsTestItem question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<GapItem> getGapItems() {
        return gapItems;
    }

    public FillingGapsTestItem gapItems(Set<GapItem> gapItems) {
        this.gapItems = gapItems;
        return this;
    }

    public FillingGapsTestItem addGapItem(GapItem gapItem) {
        this.gapItems.add(gapItem);
        gapItem.setTestItem(this);
        return this;
    }

    public FillingGapsTestItem removeGapItem(GapItem gapItem) {
        this.gapItems.remove(gapItem);
        gapItem.setTestItem(null);
        return this;
    }

    public void setGapItems(Set<GapItem> gapItems) {
        this.gapItems = gapItems;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public FillingGapsTestItem tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public FillingGapsTestItem addTag(Tag tag) {
        this.tags.add(tag);
        tag.getFillingGapsTestItems().add(this);
        return this;
    }

    public FillingGapsTestItem removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getFillingGapsTestItems().remove(this);
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
        FillingGapsTestItem fillingGapsTestItem = (FillingGapsTestItem) o;
        if (fillingGapsTestItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fillingGapsTestItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FillingGapsTestItem{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
