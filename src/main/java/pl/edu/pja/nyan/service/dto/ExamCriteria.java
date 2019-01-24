package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import pl.edu.pja.nyan.domain.enumeration.TestType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Exam entity. This class is used in ExamResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /exams?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExamCriteria implements Serializable {
    /**
     * Class for filtering TestType
     */
    public static class TestTypeFilter extends Filter<TestType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private TestTypeFilter type;

    private StringFilter code;

    private LongFilter resultsId;

    private LongFilter creatorId;

    private LongFilter wordId;

    public ExamCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public TestTypeFilter getType() {
        return type;
    }

    public void setType(TestTypeFilter type) {
        this.type = type;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getResultsId() {
        return resultsId;
    }

    public void setResultsId(LongFilter resultsId) {
        this.resultsId = resultsId;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getWordId() {
        return wordId;
    }

    public void setWordId(LongFilter wordId) {
        this.wordId = wordId;
    }

    @Override
    public String toString() {
        return "ExamCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (resultsId != null ? "resultsId=" + resultsId + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (wordId != null ? "wordId=" + wordId + ", " : "") +
            "}";
    }

}
