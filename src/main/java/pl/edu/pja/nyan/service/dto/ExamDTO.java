package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import pl.edu.pja.nyan.domain.enumeration.TestType;

/**
 * A DTO for the Exam entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExamDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private TestType type;

    private String code;

    private Long creatorId;

    private String creatorLogin;

    @Singular
    private Set<WordDTO> words;

    @Singular
    private List<ExamResultDTO> results;

}
