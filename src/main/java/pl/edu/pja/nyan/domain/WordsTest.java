package pl.edu.pja.nyan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import pl.edu.pja.nyan.domain.enumeration.TestType;

/**
 * A WordsTest.
 */
@Entity
@Table(name = "words_test")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WordsTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TestType type;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User creator;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "words_test_word",
               joinColumns = @JoinColumn(name = "words_tests_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "words_id", referencedColumnName = "id"))
    private Set<Word> words = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WordsTest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestType getType() {
        return type;
    }

    public WordsTest type(TestType type) {
        this.type = type;
        return this;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public WordsTest code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getCreator() {
        return creator;
    }

    public WordsTest creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Set<Word> getWords() {
        return words;
    }

    public WordsTest words(Set<Word> words) {
        this.words = words;
        return this;
    }

    public WordsTest addWord(Word word) {
        this.words.add(word);
        word.getWordsTests().add(this);
        return this;
    }

    public WordsTest removeWord(Word word) {
        this.words.remove(word);
        word.getWordsTests().remove(this);
        return this;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
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
        WordsTest wordsTest = (WordsTest) o;
        if (wordsTest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wordsTest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WordsTest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
