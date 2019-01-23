package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

/**
 * A DTO for the TrueFalseAnswer entity.
 */
@Data
@Builder
public class TrueFalseAnswerDTO implements Serializable {

    private Long id;

    @NotNull
    private String translationFrom;

    @NotNull
    private Boolean isRightAnswer;

    private WordDTO srcWord;

    private WordDTO targetWord;

    private Long examId;

}
