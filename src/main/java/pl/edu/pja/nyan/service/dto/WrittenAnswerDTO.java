package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the WrittenAnswer entity.
 */
public class WrittenAnswerDTO implements Serializable {

    private Long id;

    private String translationFrom;

    private String translation;

    private String kana;

    private String kanji;

    private String romaji;

    private Boolean isRightAnswer;

    private Long answerId;

    private Long examId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslationFrom() {
        return translationFrom;
    }

    public void setTranslationFrom(String translationFrom) {
        this.translationFrom = translationFrom;
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

    public Boolean isIsRightAnswer() {
        return isRightAnswer;
    }

    public void setIsRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long wordId) {
        this.answerId = wordId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examResultId) {
        this.examId = examResultId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WrittenAnswerDTO writtenAnswerDTO = (WrittenAnswerDTO) o;
        if (writtenAnswerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), writtenAnswerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WrittenAnswerDTO{" +
            "id=" + getId() +
            ", translationFrom='" + getTranslationFrom() + "'" +
            ", translation='" + getTranslation() + "'" +
            ", kana='" + getKana() + "'" +
            ", kanji='" + getKanji() + "'" +
            ", romaji='" + getRomaji() + "'" +
            ", isRightAnswer='" + isIsRightAnswer() + "'" +
            ", answer=" + getAnswerId() +
            ", exam=" + getExamId() +
            "}";
    }
}
