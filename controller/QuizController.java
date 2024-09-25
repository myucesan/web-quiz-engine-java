package controller;

import model.Quiz;
import model.User;
import model.UserQuizCompletion;
import model.dto.QuizAnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import service.QuizService;
import service.UserQuizCompletionService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class QuizController {
    private final QuizService qs;
    private final UserQuizCompletionService uqcs;

    public QuizController(QuizService qs, UserQuizCompletionService uqcs) {
        this.qs = qs;
        this.uqcs = uqcs;
    }

    @PostMapping("/api/quizzes")
    public Map<Object, Object> postQuiz(@Valid @RequestBody Quiz q, @AuthenticationPrincipal User user) {
        q.setAuthor(user);
        Quiz savedQuiz = qs.addQuiz(q); // Ensure this method saves and returns the entity with the generated ID
        Map<Object, Object> result = new LinkedHashMap<>();
        result.put("id", savedQuiz.getId()); // Use the ID from the saved entity
        result.put("title", savedQuiz.getTitle());
        result.put("text", savedQuiz.getText());
        result.put("options", savedQuiz.getOptions());
        return result;
    }

    @GetMapping("/api/quizzes")
    ResponseEntity<Page<Quiz>> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        // Assuming getQuizByPage returns a Page<Quiz>
        Page<Quiz> quizzes = qs.getQuizByPage(PageRequest.of(page, 10));

        if (quizzes == null || quizzes.isEmpty()) {
            return ResponseEntity.ok(Page.empty()); // Return an empty Page object
        }

        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/api/quizzes/{id}")
    Map<Object, Object> getQuizzes(@PathVariable Long id) {
        Quiz q = qs.getQuiz(id);
        Map<Object, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("title", q.title());
        result.put("text", q.text());
        result.put("options", q.options());
        return result;
    }

    @GetMapping("/api/quizzes/completed")
    Page<UserQuizCompletion> getCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page, @AuthenticationPrincipal User user) {
        return uqcs.getQuizByPageByUser(user, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "completedAt")));
    }
    @DeleteMapping("/api/quizzes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Map<Object, Object>> deleteQuizzes(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Quiz q = qs.getQuiz(id);
        if (q == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (q.getAuthor().getUsername().equals(userDetails.getUsername())) {
            qs.deleteQuiz(id);
        } else  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
//        Map<Object, Object> result = new LinkedHashMap<>();
//        result.put("id", id);
//        result.put("title", q.title());
//        result.put("text", q.text());
//        result.put("options", q.options());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/quizzes/{id}/solve")
    RetrievalDTO getQuiz(@PathVariable Long id, @RequestBody QuizAnswerDTO answer, @AuthenticationPrincipal User user) {
        Quiz q = qs.getQuiz(id);
//        if (q == null) {
//            throw new QuizNotFoundException(id);
//        }

        // Handle null gracefully by converting null to an empty list
        List<Integer> correctAnswer = q.answer() == null ? new ArrayList<>() : new ArrayList<>(q.answer());
        List<Integer> userAnswer = answer.answer() == null ? new ArrayList<>() : new ArrayList<>(answer.answer());

        // Sort the lists to compare them regardless of order
        Collections.sort(correctAnswer);
        Collections.sort(userAnswer);

        // Compare the sorted lists
        boolean success = correctAnswer.equals(userAnswer);
        String feedback = success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";

        if (success) {
            uqcs.completeQuiz(new UserQuizCompletion(q, user));
        }

        return new RetrievalDTO(success, feedback);
    }
}

record RetrievalDTO(boolean success, String feedback){}
