package pl.edu.pja.nyan.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Column(name = "translation_from")
    private String translationFrom;

    @Column(name = "is_right_answer")
    private Boolean isRightAnswer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Word srcWord;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Word targetWord;

    @ManyToOne
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
