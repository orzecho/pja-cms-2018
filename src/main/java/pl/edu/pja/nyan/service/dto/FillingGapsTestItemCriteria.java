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
 * Criteria class for the FillingGapsTestItem entity. This class is used in FillingGapsTestItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /filling-gaps-test-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FillingGapsTestItemCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter question;

    private LongFilter gapItemId;

    private LongFilter tagId;

    public FillingGapsTestItemCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getQuestion() {
        return question;
    }

    public void setQuestion(StringFilter question) {
        this.question = question;
    }

    public LongFilter getGapItemId() {
        return gapItemId;
    }

    public void setGapItemId(LongFilter gapItemId) {
        this.gapItemId = gapItemId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "FillingGapsTestItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (question != null ? "question=" + question + ", " : "") +
                (gapItemId != null ? "gapItemId=" + gapItemId + ", " : "") +
                (tagId != null ? "tagId=" + tagId + ", " : "") +
            "}";
    }

}
