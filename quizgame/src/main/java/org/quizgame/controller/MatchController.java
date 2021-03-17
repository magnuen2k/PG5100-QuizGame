package org.quizgame.controller;

import org.quizgame.entity.Category;
import org.quizgame.entity.Quiz;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class MatchController implements Serializable {
    public boolean isMatchOn() {
        return false;
    }

    public String newMatch() {
        return null;
    }

    public List<Category> getCategories() {
        return null;
    }

    public boolean isCategorySelected() {
        return false;
    }

    public void selectCategory(long id) {

    }

    public Quiz getCurrentQuiz() {
        return null;
    }

    public String answerQuiz(int index) {
        return null;
    }

}
