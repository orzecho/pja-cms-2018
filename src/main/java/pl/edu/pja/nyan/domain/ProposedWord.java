package pl.edu.pja.nyan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProposedWord.
 */
@Entity
@Table(name = "proposed_word")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProposedWord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "translation", nullable = false)
    private String translation;

    @NotNull
    @Column(name = "kana", nullable = false)
    private String kana;

    @Column(name = "kanji")
    private String kanji;

    @Column(name = "romaji")
    private String romaji;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "proposed_word_tag",
               joinColumns = @JoinColumn(name = "proposed_words_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private User addedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public ProposedWord translation(String translation) {
        this.translation = translation;
        return this;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getKana() {
        return kana;
    }

    public ProposedWord kana(String kana) {
        this.kana = kana;
        return this;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getKanji() {
        return kanji;
    }

    public ProposedWord kanji(String kanji) {
        this.kanji = kanji;
        return this;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getRomaji() {
        return romaji;
    }

    public ProposedWord romaji(String romaji) {
        this.romaji = romaji;
        return this;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public String getNote() {
        return note;
    }

    public ProposedWord note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public ProposedWord tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public ProposedWord addTag(Tag tag) {
        this.tags.add(tag);
//        tag.getProposedWords().add(this);
        return this;
    }

    public ProposedWord removeTag(Tag tag) {
        this.tags.remove(tag);
//        tag.getProposedWords().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public ProposedWord addedBy(User user) {
        this.addedBy = user;
        return this;
    }

    public void setAddedBy(User user) {
        this.addedBy = user;
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
        ProposedWord proposedWord = (ProposedWord) o;
        if (proposedWord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proposedWord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProposedWord{" +
            "id=" + getId() +
            ", translation='" + getTranslation() + "'" +
            ", kana='" + getKana() + "'" +
            ", kanji='" + getKanji() + "'" +
            ", romaji='" + getRomaji() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
