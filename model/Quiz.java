package model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "quizzes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Quiz {
    @Id
//    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @NotBlank String title;
    private @NotBlank String text;
    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 2) @NotNull
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> options;
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User author;


    public Quiz(@NotBlank String title, @NotBlank String text, @Size(min = 2) @NotNull List<String> options, List<Integer> answer, User a) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.author = a;
    }

    public UserDetails getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Quiz() {

    }

    @Override
    public String toString() {
        return "Quiz[" +
                "title=" + title + ", " +
                "text=" + text + ", " +
                "options=" + options + ']';
    }

    public @NotBlank String title() {
        return title;
    }

    public @NotBlank String text() {
        return text;
    }

    public List<String> options() {
        return options;
    }

    public List<Integer> answer() {
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Quiz) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.text, that.text) &&
                Objects.equals(this.options, that.options) &&
                Objects.equals(this.answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, options, answer);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public @NotBlank String getText() {
        return text;
    }

    public void setText(@NotBlank String text) {
        this.text = text;
    }

    public @Size(min = 2) @NotNull List<String> getOptions() {
        return options;
    }

    public void setOptions(@Size(min = 2) @NotNull List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }
}
