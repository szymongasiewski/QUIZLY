package com.example.quizly.api;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiCount {
    @SerializedName("category_name")
    private int categoryId;

    @SerializedName("category_question_count")
    private CategoryQuestionCount categoryQuestionCount;
}
