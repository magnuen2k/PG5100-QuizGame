package org.quizgame.entity;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Quiz {

    public Quiz() {};

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 128)
    @Column(unique = true)
    private String question;

    @NotBlank
    @Size(max = 128)
    private String answer1;

    @NotBlank
    @Size(max = 128)
    private String answer2;

    @NotBlank
    @Size(max = 128)
    private String answer3;

    @NotBlank
    @Size(max = 128)
    private String answer4;

    @Range(min = 0, max = 3)
    private int correctAnswerIndex;

    @OneToOne
    @NotNull
    private SubCategory subCategory;

    public Long getId() {
        return id;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
