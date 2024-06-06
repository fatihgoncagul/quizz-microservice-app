package com.example.quizservice.service;


import com.example.quizservice.dao.QuizDao;
import com.example.quizservice.feign.QuizInterface;
import com.example.quizservice.model.QuestionWrapper;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizDao quizDao;

    private final QuizInterface quizInterface;

    //private final QuestionDao questionDao;

    public QuizService(QuizDao quizDao, QuizInterface quizInterface) {
        this.quizDao = quizDao;

        this.quizInterface = quizInterface;
    }


    /**
     * Important thing here is that, in monolithic app we were accessing through questionDao
     * Now we will interact with question microservice (//call the generate url) and get a list of questionIds
     * we  use feign client - http web request but its declarative it will help us - what are the api we want to expose and hit
     * <p>
     * another thing is service discovery (eureka), question service needs to discovarable, quiz will discover
     * <p>
     * when a quiz service want to create a quiz it will use question servşice
     * in order to search for question service, we will need a eureaka server
     * question service will register itself to eureka server and quiz service will be able to search it
     * <p>
     * Now our quiz service should be able to find it. İt will use Feign dependency to find question service!Now our quiz service should be able to find it.
     * İt will use Feign dependency to find question service!
     */
    public ResponseEntity<String> createQuiz(String difficultylevel, int num, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(difficultylevel, num).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Succes", HttpStatus.CREATED);


    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }


}
