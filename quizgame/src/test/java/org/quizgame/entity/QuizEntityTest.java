package org.quizgame.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizEntityTest extends EntityTestBase{

    @Test
    public void testQuiz() {
        Category category = createCategory("no");
        SubCategory sub = createSubcategory("yes", category);
        Quiz quiz = createQuiz(sub);

        assertFalse(persistInATransaction(category, quiz));

        System.out.println("GENERATED ID: " + quiz.getId());
    }

    @Test
    public void testQuizWithSubCategory(){
        Category c = createCategory("TestCategory");
        SubCategory sc = createSubcategory("TestSub", c);
        Quiz quiz = createQuiz(sc);

        assertTrue(persistInATransaction(c, sc, quiz));
    }


    @Test
    public void testQuery() {

        Category JEE = createCategory("JEE");

        SubCategory JPA = createSubcategory("JPA", JEE);
        SubCategory EJB = createSubcategory("EJB", JEE);
        SubCategory JSF = createSubcategory("JSF", JEE);

        assertTrue(persistInATransaction(JPA, JEE, EJB, JSF));

        Quiz quiz1 = createQuiz(JPA);
        Quiz quiz2 = createQuiz(JPA);
        Quiz quiz3 = createQuiz(EJB);
        Quiz quiz4 = createQuiz(JSF);

        assertTrue(persistInATransaction(quiz1, quiz2, quiz3, quiz4));

        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q where q.subCategory.name = 'JPA'", Quiz.class);
        List<Quiz> quizzes = query.getResultList();

        assertEquals(2, quizzes.size());

        TypedQuery<Quiz> query2 = em.createQuery("select q from Quiz q where q.subCategory.category.name = 'JEE'", Quiz.class);
        List<Quiz> quizzes2 = query2.getResultList();

        assertEquals(4, quizzes2.size());

    }


}