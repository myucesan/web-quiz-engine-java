package repository;

import model.Quiz;
import model.User;
import model.UserQuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizCompletionRepository  extends JpaRepository<UserQuizCompletion, Long>  {
    Page<UserQuizCompletion> findByUser(User user, Pageable pageable);
    List<UserQuizCompletion> findByQuiz(Quiz quiz);
}
