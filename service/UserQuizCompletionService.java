package service;

import model.Quiz;
import model.User;
import model.UserQuizCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.UserQuizCompletionRepository;

@Service
public class UserQuizCompletionService {
    private UserQuizCompletionRepository qr;
    @Autowired
    public UserQuizCompletionService(UserQuizCompletionRepository qr) {
        this.qr = qr;
    }

    public UserQuizCompletion completeQuiz(UserQuizCompletion q) {
        UserQuizCompletion completion = new UserQuizCompletion(q.getQuiz(), q.getUser());
        return qr.save(completion);
    }

    public Page<UserQuizCompletion> getQuizByPageByUser(User user, Pageable pageable) {
        return qr.findByUser(user, pageable);
    }
}
