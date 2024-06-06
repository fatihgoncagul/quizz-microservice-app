package com.example.questionservice.controller;


import com.example.questionservice.model.QuestionWrapper;
import com.example.questionservice.model.Response;
import com.example.questionservice.service.QuestionService;
import com.example.questionservice.model.Question;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    private final QuestionService questionService;

    private final Environment environment;

    public QuestionController(QuestionService questionService, Environment environment) {
        this.questionService = questionService;
        this.environment = environment;
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {

        return questionService.getAllQuestions();
    }

    @GetMapping("difficultylevel/{difficultylevel}")
    public ResponseEntity<List<Question>> getQuestionsByDiffLevel(@PathVariable String difficultylevel) {

        return questionService.getQuestionsByDiffLevel(difficultylevel);

    }
    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);


    }
// quiz service wont have access to the question db, it will have to ask for the questions

    // generate quiz
    // getquestions (questionid)
    // getScore

    @GetMapping("generate")//previously we were returning questions but now since calculating score will be here, theres no necessary for quiz to know the questions. only id is enough
    //also quiz service will request to the question service by following parameters
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String difficultylevel, @RequestParam Integer numOfQuestions){

        System.out.println(environment.getProperty("local.server.port"));
        return questionService.getQuestionsForQuiz(difficultylevel,numOfQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestParam List<Integer> questionIds){
        return questionService.getQuestionsFromId(questionIds);
    }
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){

        return questionService.getScore(responses);


    }




    /*
    *
    *
*
{
"id": 2,
"questionTitle": "Which keyword is used to inherit a class in Java?",
"option1": "extends",
"option2": "implements",
"option3": "inherits",
"option4": "creates",
"rightAnswer": "extends",
"difficultylevel": "Medium"
}
*
    *
    * */

}
