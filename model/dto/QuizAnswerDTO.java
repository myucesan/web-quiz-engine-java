package model.dto;

import java.util.List;
import java.util.Objects;

public class QuizAnswerDTO {
    private List<Integer> answer;

    public QuizAnswerDTO(List<Integer> answer) {
        this.answer = answer;
    }

    public QuizAnswerDTO() {
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public List<Integer> answer() {
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (QuizAnswerDTO) obj;
        return Objects.equals(this.answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }

    @Override
    public String toString() {
        return "QuizAnswerDTO[" +
                "answer=" + answer + ']';
    }

}
