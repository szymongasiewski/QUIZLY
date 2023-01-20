package com.example.quizly.api;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryQuestionCount {
    @SerializedName("total_hard_question_count")
    private int totalHardQuestionsCount;

    @SerializedName("total_easy_question_count")
    private int totalEasyQuestionCount;

    @SerializedName("total_medium_question_count")
    private int totalMediumQuestionCount;
}
