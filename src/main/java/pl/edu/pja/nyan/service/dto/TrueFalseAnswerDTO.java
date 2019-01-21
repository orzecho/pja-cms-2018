package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * A DTO for the TrueFalseAnswer entity.
 */
@Data
@Builder
public class TrueFalseAnswerDTO implements Serializable {

    private Long id;

    private String translationFrom;

    private Boolean isRightAnswer;

    private WordDTO srcWord;

    private WordDTO targetWord;

    private Long examId;

}
