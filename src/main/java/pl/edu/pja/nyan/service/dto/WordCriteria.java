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
 * Criteria class for the Word entity. This class is used in WordResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /words?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WordCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter translation;

    private StringFilter kana;

    private StringFilter kanji;

    private StringFilter romaji;

    private StringFilter note;

    private LongFilter tagId;

    public WordCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public StringFilter getRomaji() {
        return romaji;
    }

    public void setRomaji(StringFilter romaji) {
        this.romaji = romaji;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "WordCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (translation != null ? "translation=" + translation + ", " : "") +
                (kana != null ? "kana=" + kana + ", " : "") +
                (kanji != null ? "kanji=" + kanji + ", " : "") +
                (romaji != null ? "romaji=" + romaji + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (tagId != null ? "tagId=" + tagId + ", " : "") +
            "}";
    }

}
