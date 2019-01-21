package pl.edu.pja.nyan.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A ExamResult.
 */
@Entity
@Table(name = "exam_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "result", nullable = false)
    private Integer result;

    @OneToMany(mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WrittenAnswer> writtenAnswers = new HashSet<>();

    @OneToMany(mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TrueFalseAnswer> trueFalseAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private User student;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public ExamResult date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getResult() {
        return result;
    }

    public ExamResult result(Integer result) {
        this.result = result;
        return this;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Set<WrittenAnswer> getWrittenAnswers() {
        return writtenAnswers;
    }

    public ExamResult writtenAnswers(Set<WrittenAnswer> writtenAnswers) {
        this.writtenAnswers = writtenAnswers;
        return this;
    }

    public ExamResult addWrittenAnswers(WrittenAnswer writtenAnswer) {
        this.writtenAnswers.add(writtenAnswer);
        writtenAnswer.setExam(this);
        return this;
    }

    public ExamResult removeWrittenAnswers(WrittenAnswer writtenAnswer) {
        this.writtenAnswers.remove(writtenAnswer);
        writtenAnswer.setExam(null);
        return this;
    }

    public void setWrittenAnswers(Set<WrittenAnswer> writtenAnswers) {
        this.writtenAnswers = writtenAnswers;
    }

    public Set<TrueFalseAnswer> getTrueFalseAnswers() {
        return trueFalseAnswers;
    }

    public ExamResult trueFalseAnswers(Set<TrueFalseAnswer> trueFalseAnswers) {
        this.trueFalseAnswers = trueFalseAnswers;
        return this;
    }

    public ExamResult addTrueFalseAnswers(TrueFalseAnswer trueFalseAnswer) {
        this.trueFalseAnswers.add(trueFalseAnswer);
        trueFalseAnswer.setExam(this);
        return this;
    }

    public ExamResult removeTrueFalseAnswers(TrueFalseAnswer trueFalseAnswer) {
        this.trueFalseAnswers.remove(trueFalseAnswer);
        trueFalseAnswer.setExam(null);
        return this;
    }

    public void setTrueFalseAnswers(Set<TrueFalseAnswer> trueFalseAnswers) {
        this.trueFalseAnswers = trueFalseAnswers;
    }

    public User getStudent() {
        return student;
    }

    public ExamResult student(User user) {
        this.student = user;
        return this;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public Exam getExam() {
        return exam;
    }

    public ExamResult exam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExamResult examResult = (ExamResult) o;
        if (examResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamResult{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", result=" + getResult() +
            "}";
    }
}
