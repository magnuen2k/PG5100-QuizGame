package org.quizgame.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizEntityTest {

    private EntityManagerFactory factory;
    private EntityManager em;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        factory.close();
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }

    @Test
    public void testQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQuestion("Whats nine plus ten?");
        quiz.setAnswer1("20");
        quiz.setAnswer2("18");
        quiz.setAnswer3("21");
        quiz.setAnswer4("50");
        quiz.setCorrectAnswerIndex(1);

        // EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");
        // EntityManager em = factory.createEntityManager();

        /*EntityTransaction tx = em.getTransaction();
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
        }*/

        assertTrue(persistInATransaction(quiz));

        System.out.println("GENERATED ID: " + quiz.getId());
    }

    @Test
    public void testQuizWithSubCategory(){
        Category c = new Category();
        SubCategory sc = new SubCategory();
        c.setSubCategory(sc);
        Quiz quiz = new Quiz();
        quiz.setSubCategory(sc);

        assertTrue(persistInATransaction(c, sc, quiz));
    }

    @Test
    public void testQuery() {
        Category c = new Category();
        c.setName("JEE");

        SubCategory sc1 = new SubCategory();
        sc1.setName("JPA");
        SubCategory sc2 = new SubCategory();
        sc2.setName("EJB");
        SubCategory sc3 = new SubCategory();
        sc3.setName("JSF");

        c.setSubCategory(sc1);
        c.setSubCategory(sc2);
        c.setSubCategory(sc3);
        sc1.setCategory(c);
        sc2.setCategory(c);
        sc3.setCategory(c);

        Quiz quiz1 = new Quiz();
        quiz1.setQuestion("Whats nine plus ten?");
        quiz1.setAnswer1("20");
        quiz1.setAnswer2("18");
        quiz1.setAnswer3("21");
        quiz1.setAnswer4("50");
        quiz1.setCorrectAnswerIndex(1);

        Quiz quiz2 = new Quiz();
        quiz2.setQuestion("Whats nine plus ten?");
        quiz2.setAnswer1("20");
        quiz2.setAnswer2("18");
        quiz2.setAnswer3("21");
        quiz2.setAnswer4("50");
        quiz2.setCorrectAnswerIndex(1);

        Quiz quiz3 = new Quiz();
        quiz3.setQuestion("Whats nine plus ten?");
        quiz3.setAnswer1("20");
        quiz3.setAnswer2("18");
        quiz3.setAnswer3("21");
        quiz3.setAnswer4("50");
        quiz3.setCorrectAnswerIndex(1);

        Quiz quiz4 = new Quiz();
        quiz4.setQuestion("Whats nine plus ten?");
        quiz4.setAnswer1("20");
        quiz4.setAnswer2("18");
        quiz4.setAnswer3("21");
        quiz4.setAnswer4("50");
        quiz4.setCorrectAnswerIndex(1);

        quiz1.setSubCategory(sc1);
        quiz2.setSubCategory(sc1);
        quiz3.setSubCategory(sc2);
        quiz4.setSubCategory(sc3);

        persistInATransaction(c, quiz1, quiz2, quiz3, quiz4, sc1, sc2, sc3);

        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q where q.subCategory.name = 'JPA'", Quiz.class);
        List<Quiz> quizzes = query.getResultList();

        assertEquals(2, quizzes.size());

        TypedQuery<Quiz> query2 = em.createQuery("select q from Quiz q where q.subCategory.category.name = 'JEE'", Quiz.class);
        List<Quiz> quizzes2 = query2.getResultList();

        assertEquals(4, quizzes2.size());

    }
}