package com.example.quizly.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://opentdb.com/";

    @GET("api.php")
    Call<QuizQuestions> getQuiz(
            @Query("encode") String encode,
            @Query("amount") Integer amount,
            @Query("difficulty") String difficulty,
            @Query("category") Integer category
    );

    @GET("api_count.php")
    Call<ApiCount> getQuiz(@Query("category") Integer category);
}
