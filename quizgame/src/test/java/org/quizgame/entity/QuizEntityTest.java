package org.quizgame.entity;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class QuizEntityTest {

    @Test
    public void testQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQuestion("Whats nine plus ten?");
        quiz.setAnswer1("20");
        quiz.setAnswer2("18");
        quiz.setAnswer3("21");
        quiz.setAnswer4("50");
        quiz.setCorrectAnswerIndex(1);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");
        EntityManager em = factory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            em.persist(quiz);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail();
        } finally {
            em.close();
            factory.close();
        }

        System.out.println("GENERATED ID: " + quiz.getId());
    }
}