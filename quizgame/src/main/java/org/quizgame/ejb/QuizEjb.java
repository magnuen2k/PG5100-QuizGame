package org.quizgame.ejb;

import org.quizgame.entity.Quiz;
import org.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class QuizEjb {

    @PersistenceContext
    private EntityManager em;

    public long createQuiz (long subCategoryId, String question, String firstAnswer, String secondAnswer, String thirdAnswer, String fourthAnswer, int indexOfCorrectAnswer) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setAnswer1(firstAnswer);
        quiz.setAnswer2(secondAnswer);
        quiz.setAnswer3(thirdAnswer);
        quiz.setAnswer4(fourthAnswer);
        quiz.setCorrectAnswerIndex(indexOfCorrectAnswer);
        quiz.setSubCategory(em.find(SubCategory.class, subCategoryId));

        em.persist(quiz);
        return quiz.getId();
    }

    public List<Quiz> getQuizzes() {
        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q", Quiz.class);
        return query.getResultList();
    }

    public Quiz getQuiz(long id) {
        return em.find(Quiz.class, id);
    }

    public List<Quiz> getRandomQuizzes(int n, long categoryId) {
        TypedQuery<Long> sizeQuery= em.createQuery(
                "select count(q) from Quiz q where q.subCategory.category.id=?1", Long.class);
        sizeQuery.setParameter(1, categoryId);
        long size = sizeQuery.getSingleResult();

        if(n > size){
            throw new IllegalArgumentException("Cannot choose " + n + " unique quizzes out of the " + size + " existing");
        }

        Random rng = new Random();

        List<Quiz> quizzes = new ArrayList<>();
        Set<Integer> chosen = new HashSet<>();

        while(chosen.size() < n) {

            int k = rng.nextInt((int)size);
            if(chosen.contains(k)){
                continue;
            }
            chosen.add(k);

            TypedQuery<Quiz> query = em.createQuery(
                    "select q from Quiz q where q.subCategory.category.id=?1", Quiz.class);
            query.setParameter(1, categoryId);
            query.setMaxResults(1);
            query.setFirstResult(k);

            quizzes.add(query.getSingleResult());
        }

        return  quizzes;
    }
}
