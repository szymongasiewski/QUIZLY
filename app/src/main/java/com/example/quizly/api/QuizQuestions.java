package com.example.quizly.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestions {
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
}
