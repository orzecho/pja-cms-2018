package pl.edu.pja.nyan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TrueFalseAnswer.
 */
@Entity
@Table(name = "true_false_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrueFalseAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "translation_from", nullable = false)
    private String translationFrom;

    @NotNull
    @Column(name = "is_right_answer", nullable = false)
    private Boolean isRightAnswer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Word srcWord;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Word targetWord;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("trueFalseAnswers")
    private ExamResult exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslationFrom() {
        return translationFrom;
    }

    public TrueFalseAnswer translationFrom(String translationFrom) {
        this.translationFrom = translationFrom;
        return this;
    }

    public void setTranslationFrom(String translationFrom) {
        this.translationFrom = translationFrom;
    }

    public Boolean isRightAnswer() {
        return isRightAnswer;
    }

    public TrueFalseAnswer isRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
        return this;
    }

    public void setIsRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public Word getSrcWord() {
        return srcWord;
    }

    public TrueFalseAnswer srcWord(Word word) {
        this.srcWord = word;
        return this;
    }

    public void setSrcWord(Word word) {
        this.srcWord = word;
    }

    public Word getTargetWord() {
        return targetWord;
    }

    public TrueFalseAnswer targetWord(Word word) {
        this.targetWord = word;
        return this;
    }

    public void setTargetWord(Word word) {
        this.targetWord = word;
    }

    public ExamResult getExam() {
        return exam;
    }

    public TrueFalseAnswer exam(ExamResult examResult) {
        this.exam = examResult;
        return this;
    }

    public void setExam(ExamResult examResult) {
        this.exam = examResult;
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
        TrueFalseAnswer trueFalseAnswer = (TrueFalseAnswer) o;
        if (trueFalseAnswer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trueFalseAnswer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrueFalseAnswer{" +
            "id=" + getId() +
            ", translationFrom='" + getTranslationFrom() + "'" +
            ", isRightAnswer='" + isRightAnswer() + "'" +
            "}";
    }
}
