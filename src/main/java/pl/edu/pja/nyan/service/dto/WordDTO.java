package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

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
public class WordDTO implements Serializable {

    private Long id;

    @NotNull
    private String translation;

    @NotNull
    private String kana;

    private String kanji;

    private String romaji;

    private String note;

    private List<TagDTO> tags = new ArrayList<>();

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
