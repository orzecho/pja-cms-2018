package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the TrueFalseAnswer entity. This class is used in TrueFalseAnswerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /true-false-answers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TrueFalseAnswerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter translationFrom;

    private BooleanFilter isRightAnswer;

    private LongFilter srcWordId;

    private LongFilter targetWordId;

    private LongFilter examId;

    public TrueFalseAnswerCriteria() {
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

    public BooleanFilter getIsRightAnswer() {
        return isRightAnswer;
    }

    public void setIsRightAnswer(BooleanFilter isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }

    public LongFilter getSrcWordId() {
        return srcWordId;
    }

    public void setSrcWordId(LongFilter srcWordId) {
        this.srcWordId = srcWordId;
    }

    public LongFilter getTargetWordId() {
        return targetWordId;
    }

    public void setTargetWordId(LongFilter targetWordId) {
        this.targetWordId = targetWordId;
    }

    public LongFilter getExamId() {
        return examId;
    }

    public void setExamId(LongFilter examId) {
        this.examId = examId;
    }

    @Override
    public String toString() {
        return "TrueFalseAnswerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (translationFrom != null ? "translationFrom=" + translationFrom + ", " : "") +
                (isRightAnswer != null ? "isRightAnswer=" + isRightAnswer + ", " : "") +
                (srcWordId != null ? "srcWordId=" + srcWordId + ", " : "") +
                (targetWordId != null ? "targetWordId=" + targetWordId + ", " : "") +
                (examId != null ? "examId=" + examId + ", " : "") +
            "}";
    }

}
