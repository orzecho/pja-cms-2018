package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the ExamResult entity. This class is used in ExamResultResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /exam-results?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExamResultCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter date;

    private IntegerFilter result;

    private LongFilter writtenAnswersId;

    private LongFilter trueFalseAnswersId;

    private LongFilter studentId;

    private LongFilter examId;

    public ExamResultCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDate() {
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public IntegerFilter getResult() {
        return result;
    }

    public void setResult(IntegerFilter result) {
        this.result = result;
    }

    public LongFilter getWrittenAnswersId() {
        return writtenAnswersId;
    }

    public void setWrittenAnswersId(LongFilter writtenAnswersId) {
        this.writtenAnswersId = writtenAnswersId;
    }

    public LongFilter getTrueFalseAnswersId() {
        return trueFalseAnswersId;
    }

    public void setTrueFalseAnswersId(LongFilter trueFalseAnswersId) {
        this.trueFalseAnswersId = trueFalseAnswersId;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getExamId() {
        return examId;
    }

    public void setExamId(LongFilter examId) {
        this.examId = examId;
    }

    @Override
    public String toString() {
        return "ExamResultCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (result != null ? "result=" + result + ", " : "") +
                (writtenAnswersId != null ? "writtenAnswersId=" + writtenAnswersId + ", " : "") +
                (trueFalseAnswersId != null ? "trueFalseAnswersId=" + trueFalseAnswersId + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
                (examId != null ? "examId=" + examId + ", " : "") +
            "}";
    }

}
