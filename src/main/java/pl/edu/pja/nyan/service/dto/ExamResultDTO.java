package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the ExamResult entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ExamResultDTO implements Serializable {

    private Long id;

    private Instant date;

    private Integer result;

    private Long studentId;

    private String studentLogin;

    @NotNull
    private Long examId;

    @Getter(AccessLevel.NONE)
    private List<WrittenAnswerDTO> writtenAnswers;

    @Getter(AccessLevel.NONE)
    private List<TrueFalseAnswerDTO> trueFalseAnswers;

    public List<WrittenAnswerDTO> getWrittenAnswers() {
        return writtenAnswers == null ? new ArrayList<>() : writtenAnswers;
    }

    public List<TrueFalseAnswerDTO> getTrueFalseAnswers() {
        return trueFalseAnswers == null ? new ArrayList<>() : trueFalseAnswers;
    }
}
