package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * A DTO for the WrittenAnswer entity.
 */
@Data
@Builder
public class WrittenAnswerDTO implements Serializable {

    private Long id;

    private String translationFrom;

    private String translation;

    private String kana;

    private String kanji;

    private String romaji;

    private Boolean isRightAnswer;

    private WordDTO word;

    private Long answerId;

    private Long examId;

}
