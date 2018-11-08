package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Word entity.
 */
public class WordDTO implements Serializable {

    private Long id;

    @NotNull
    private String translation;

    @NotNull
    private String kana;

    private String kanji;

    private String romaji;

    private String note;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordDTO wordDTO = (WordDTO) o;
        if (wordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WordDTO{" +
            "id=" + getId() +
            ", translation='" + getTranslation() + "'" +
            ", kana='" + getKana() + "'" +
            ", kanji='" + getKanji() + "'" +
            ", romaji='" + getRomaji() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
