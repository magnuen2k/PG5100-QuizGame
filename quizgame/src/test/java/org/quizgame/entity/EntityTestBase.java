package org.quizgame.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class EntityTestBase {
    protected EntityManagerFactory factory;
    protected EntityManager em;

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

    boolean persistInATransaction(Object... obj) {
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
