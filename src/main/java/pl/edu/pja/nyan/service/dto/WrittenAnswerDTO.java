package pl.edu.pja.nyan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the WrittenAnswer entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
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

    private WordDTO word;

    private Long answerId;

    private Long examId;

}
