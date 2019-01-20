package pl.edu.pja.nyan.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ExamResult entity.
 */
public class ExamResultDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private Integer result;

    private Long studentId;

    private String studentLogin;

    private Long examId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long userId) {
        this.studentId = userId;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public void setStudentLogin(String userLogin) {
        this.studentLogin = userLogin;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExamResultDTO examResultDTO = (ExamResultDTO) o;
        if (examResultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examResultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamResultDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", result=" + getResult() +
            ", student=" + getStudentId() +
            ", student='" + getStudentLogin() + "'" +
            ", exam=" + getExamId() +
            "}";
    }
}
