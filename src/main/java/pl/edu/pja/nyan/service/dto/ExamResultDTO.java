package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * A DTO for the ExamResult entity.
 */
@Data
@Builder
public class ExamResultDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private Integer result;

    private UserDTO student;

    private String studentLogin;

    private Long examId;

    @Singular
    private List<WrittenAnswerDTO> writtenAnswers;

    @Singular
    private List<TrueFalseAnswerDTO> trueFalseAnswers;

}
