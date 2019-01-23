package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the WrittenAnswer entity. This class is used in WrittenAnswerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /written-answers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WrittenAnswerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter translationFrom;

    private StringFilter translation;

    private StringFilter kana;

    private StringFilter kanji;

    private BooleanFilter isRightAnswer;

    private LongFilter wordId;

    private LongFilter examId;

    public WrittenAnswerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTranslationFrom() {
        return translationFrom;
    }

    public void setTranslationFrom(StringFilter translationFrom) {
        this.translationFrom = translationFrom;
    }

    public StringFilter getTranslation() {
        return translation;
    }

    public void setTranslation(StringFilter translation) {
        this.translation = translation;
    }

    public StringFilter getKana() {
        return kana;
    }

    public void setKana(StringFilter kana) {
        this.kana = kana;
    }

    public StringFilter getKanji() {
        return kanji;
    }

    public void setKanji(StringFilter kanji) {
        this.kanji = kanji;
    }

    public BooleanFilter getIsRightAnswer() {
        return isRightAnswer;
    }

    public void setIsRightAnswer(BooleanFilter isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public LongFilter getWordId() {
        return wordId;
    }

    public void setWordId(LongFilter wordId) {
        this.wordId = wordId;
    }

    public LongFilter getExamId() {
        return examId;
    }

    public void setExamId(LongFilter examId) {
        this.examId = examId;
    }

    @Override
    public String toString() {
        return "WrittenAnswerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (translationFrom != null ? "translationFrom=" + translationFrom + ", " : "") +
                (translation != null ? "translation=" + translation + ", " : "") +
                (kana != null ? "kana=" + kana + ", " : "") +
                (kanji != null ? "kanji=" + kanji + ", " : "") +
                (isRightAnswer != null ? "isRightAnswer=" + isRightAnswer + ", " : "") +
                (wordId != null ? "wordId=" + wordId + ", " : "") +
                (examId != null ? "examId=" + examId + ", " : "") +
            "}";
    }

}
