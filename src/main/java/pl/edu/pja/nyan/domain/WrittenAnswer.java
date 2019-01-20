package pl.edu.pja.nyan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A WrittenAnswer.
 */
@Entity
@Table(name = "written_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrittenAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "translation_from")
    private String translationFrom;

    @Column(name = "translation")
    private String translation;

    @Column(name = "kana")
    private String kana;

    @Column(name = "kanji")
    private String kanji;

    @Column(name = "romaji")
    private String romaji;

    @Column(name = "is_right_answer")
    private Boolean isRightAnswer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Word answer;

    @ManyToOne
    @JsonIgnoreProperties("writtenAnswers")
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

    public WrittenAnswer translationFrom(String translationFrom) {
        this.translationFrom = translationFrom;
        return this;
    }

    public void setTranslationFrom(String translationFrom) {
        this.translationFrom = translationFrom;
    }

    public String getTranslation() {
        return translation;
    }

    public WrittenAnswer translation(String translation) {
        this.translation = translation;
        return this;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getKana() {
        return kana;
    }

    public WrittenAnswer kana(String kana) {
        this.kana = kana;
        return this;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getKanji() {
        return kanji;
    }

    public WrittenAnswer kanji(String kanji) {
        this.kanji = kanji;
        return this;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getRomaji() {
        return romaji;
    }

    public WrittenAnswer romaji(String romaji) {
        this.romaji = romaji;
        return this;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public Boolean isIsRightAnswer() {
        return isRightAnswer;
    }

    public WrittenAnswer isRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
        return this;
    }

    public void setIsRightAnswer(Boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public Word getAnswer() {
        return answer;
    }

    public WrittenAnswer answer(Word word) {
        this.answer = word;
        return this;
    }

    public void setAnswer(Word word) {
        this.answer = word;
    }

    public ExamResult getExam() {
        return exam;
    }

    public WrittenAnswer exam(ExamResult examResult) {
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
        WrittenAnswer writtenAnswer = (WrittenAnswer) o;
        if (writtenAnswer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), writtenAnswer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WrittenAnswer{" +
            "id=" + getId() +
            ", translationFrom='" + getTranslationFrom() + "'" +
            ", translation='" + getTranslation() + "'" +
            ", kana='" + getKana() + "'" +
            ", kanji='" + getKanji() + "'" +
            ", romaji='" + getRomaji() + "'" +
            ", isRightAnswer='" + isIsRightAnswer() + "'" +
            "}";
    }
}
