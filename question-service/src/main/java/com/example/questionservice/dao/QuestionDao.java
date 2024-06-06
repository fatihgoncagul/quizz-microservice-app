package com.example.questionservice.dao;

import com.example.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {

    List<Question> findByDifficultylevel(String difficultylevel);


    @Query(value = "SELECT q.id FROM question q where q.difficultylevel=:difficultylevel ORDER BY RANDOM() LIMIT :num",nativeQuery = true)
    List<Integer> findRandomQuestionsByDifficultylevel(String difficultylevel, int num);
}
