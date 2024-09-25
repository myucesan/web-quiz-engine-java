package service;

import exceptions.QuizNotFoundException;
import model.Quiz;
import model.UserQuizCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.QuizRepository;
import repository.UserQuizCompletionRepository;

import java.util.*;

@Service
public class QuizService {
    private QuizRepository qr;
    private UserQuizCompletionRepository uqr;
    @Autowired
    public QuizService(QuizRepository qr, UserQuizCompletionRepository uqr) {
        this.qr = qr;
        this.uqr = uqr;
    }

    public Quiz addQuiz(Quiz q) {
        return qr.save(q);
    }

    public List<Quiz> getQuiz() {
        return qr.findAll();
    }


    public Quiz getQuiz(Long id) {
        Quiz q = qr.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        return q;
    }

    public void deleteQuiz(Long id) {
        if (!qr.existsById(id)) {
            throw new QuizNotFoundException(id);
        }

        Quiz quiz = qr.findById(id).orElseThrow(() -> new QuizNotFoundException(id));

        // Find all quiz completions related to the quiz
        List<UserQuizCompletion> completions = uqr.findByQuiz(quiz);

        // Delete all quiz completions
        uqr.deleteAll(completions);

        // Delete the quiz
        qr.deleteById(id);
    }

    public Page<Quiz> getQuizByPage(Pageable pageable) {
        return qr.findAll(pageable);
    }

}
