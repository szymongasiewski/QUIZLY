package com.example.quizly.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("difficulty")
    @Expose
    private String difficulty;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;

    @SerializedName("incorrect_answers")
    @Expose
    private List<String> incorrectAnswers;
}
