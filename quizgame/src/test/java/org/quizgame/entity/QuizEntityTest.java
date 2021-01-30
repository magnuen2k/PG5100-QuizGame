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
        Quiz quiz = createQuiz();

        assertTrue(persistInATransaction(quiz));

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

    public Quiz createQuiz() {
        return createQuiz(null);
    }

    public Quiz createQuiz(SubCategory subCategory) {
        Quiz quiz = new Quiz();
        quiz.setQuestion("Whats nine plus ten?");
        quiz.setAnswer1("20");
        quiz.setAnswer2("18");
        quiz.setAnswer3("21");
        quiz.setAnswer4("50");
        quiz.setCorrectAnswerIndex(1);
        quiz.setSubCategory(subCategory);

        return quiz;
    }

    public SubCategory createSubcategory(String name, Category category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCategory(category);

        return subCategory;
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return category;
    }
}