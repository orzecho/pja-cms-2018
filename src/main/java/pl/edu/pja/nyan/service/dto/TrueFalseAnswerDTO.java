package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TrueFalseAnswer entity.
 */
public class TrueFalseAnswerDTO implements Serializable {

    private Long id;

    private String translationFrom;

    private Boolean isRightAnswer;

    private Long srcWordId;

    private Long targetWordId;

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

    public Boolean isIsRightAnswer() {
        return isRightAnswer;
    }

    public void setIsRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public Long getSrcWordId() {
        return srcWordId;
    }

    public void setSrcWordId(Long wordId) {
        this.srcWordId = wordId;
    }

    public Long getTargetWordId() {
        return targetWordId;
    }

    public void setTargetWordId(Long wordId) {
        this.targetWordId = wordId;
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

        TrueFalseAnswerDTO trueFalseAnswerDTO = (TrueFalseAnswerDTO) o;
        if (trueFalseAnswerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trueFalseAnswerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrueFalseAnswerDTO{" +
            "id=" + getId() +
            ", translationFrom='" + getTranslationFrom() + "'" +
            ", isRightAnswer='" + isIsRightAnswer() + "'" +
            ", srcWord=" + getSrcWordId() +
            ", targetWord=" + getTargetWordId() +
            ", exam=" + getExamId() +
            "}";
    }
}
