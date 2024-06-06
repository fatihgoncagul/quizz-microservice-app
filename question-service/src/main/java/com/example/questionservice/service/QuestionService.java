package com.example.questionservice.service;

import com.example.questionservice.dao.QuestionDao;
import com.example.questionservice.model.Question;
import com.example.questionservice.model.QuestionWrapper;
import com.example.questionservice.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public ResponseEntity<List<Question>> getAllQuestions() {

        try {
            return  new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByDiffLevel(String difficultylevel) {



       try {
           return new ResponseEntity<>(questionDao.findByDifficultylevel(difficultylevel),HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
       }
       return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
    }


    public ResponseEntity<String> addQuestion(Question question) {

        try {
            questionDao.save(question);
            return new ResponseEntity<>("Succes",HttpStatus.CREATED);//201
        }catch (Exception e){
            e.printStackTrace();

        }
        return new ResponseEntity<>("Failed",HttpStatus.BAD_REQUEST);


    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String difficultylevel, Integer numOfQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByDifficultylevel(difficultylevel, numOfQuestions);

        return  new ResponseEntity<>(questions,HttpStatus.OK);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for(Integer id: questionIds){
            questions.add(questionDao.findById(id).get());
        }

        for (Question question : questions){
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }


        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {


        int right = 0;

        for (Response response : responses) {
            Question question = questionDao.findById(response.getId()).get();

            if (response.getResponse().equals(question.getRightAnswer())) {
                right++;
            }



        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
