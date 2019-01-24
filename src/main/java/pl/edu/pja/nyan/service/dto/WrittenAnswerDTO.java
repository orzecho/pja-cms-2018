package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
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

    @NotNull
    private String translationFrom;

    @NotNull
    private String translation;

    @NotNull
    private String kana;

    @NotNull
    private String kanji;

    @NotNull
    private Boolean isRightAnswer;

    private Long wordId;

    private Long answerId;

    private Long examId;

}
