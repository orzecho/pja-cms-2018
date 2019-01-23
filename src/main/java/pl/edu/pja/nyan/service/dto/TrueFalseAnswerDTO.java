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

    private Long srcWordId;

    private Long targetWordId;

    private Long examId;

}
