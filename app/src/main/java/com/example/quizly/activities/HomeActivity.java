package com.example.quizly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizly.R;
import com.example.quizly.api.Api;
import com.example.quizly.api.ApiCount;
import com.example.quizly.api.QuizQuestions;
import com.example.quizly.api.Result;
import com.example.quizly.models.Question;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    Button start;
    Button filter;
    ProgressBar progresBar;
    Question question;
    String difficulty;
    String category;
    Button chrome;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.home_start) {
                progresBar.setVisibility(View.VISIBLE);

                question = new Question(getApplicationContext());

                view.setClickable(false);
                fetchQuestionCount();
            }
            else if(view.getId() == R.id.home_filter) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(view.getId() == R.id.chrome) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Api.BASE_URL));
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setFilterDefaultValues();
        start = findViewById(R.id.home_start);
        filter = findViewById(R.id.home_filter);
        chrome = findViewById(R.id.chrome);
        progresBar = findViewById(R.id.progressBar2);
        start.setOnClickListener(onClickListener);
        filter.setOnClickListener(onClickListener);
        chrome.setOnClickListener(onClickListener);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        category = sharedPreferences.getString(getString(R.string.category_key), getString(R.string.medium_value));
        difficulty = sharedPreferences.getString(getString(R.string.difficulty_key), getString(R.string.medium_value));
    }

    private void setFilterDefaultValues() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getString(getString(R.string.difficulty_key), null);
        category = sharedPreferences.getString(getString(R.string.category_key), null);

        if(difficulty == null) {
            sharedPreferences
                    .edit()
                    .putString(getString(R.string.difficulty_key), getString(R.string.easy_value))
                    .apply();
        }
        if(category == null) {
            sharedPreferences
                    .edit()
                    .putString(getString(R.string.category_key), getString(R.string.any_category))
                    .apply();
        }
    }

    private void fetchQuestionCount() {
        int category_value = Integer.valueOf(category);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<ApiCount> call = api.getQuiz(category_value);

        call.enqueue(new Callback<ApiCount>() {
           @Override
           public void onResponse(Call<ApiCount> call, Response<ApiCount> response) {
               switch(difficulty) {
                   case "easy":
                       fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalEasyQuestionCount());
                       break;
                   case "medium":
                       fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalMediumQuestionCount());
                       break;
                   case "hard":
                       fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalHardQuestionsCount());
                       break;
               }
           }

           @Override
           public void onFailure(Call<ApiCount> call, Throwable t) {
               Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
               progresBar.setVisibility(View.INVISIBLE);
               start.setClickable(true);
           }
        });
    }

    private void fetchQuestionAPI(int categoryCount) {
        int category_value = Integer.valueOf(category);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<QuizQuestions> call = api. getQuiz("url3986", categoryCount >= 10 ? 10 : categoryCount - 1, difficulty, category_value);

        call.enqueue(new Callback<QuizQuestions>() {
            @Override
            public void onResponse(Call<QuizQuestions> call, Response<QuizQuestions> response) {
                Log.v("url---", call.request().url().toString());

                QuizQuestions quizQuestions = response.body();

                if(quizQuestions.getResponseCode() == 0) {
                    question.results = quizQuestions.getResults();

                    if(question.results != null) {
                        for(Result r : question.results) {
                            try {
                                question.question.add(java.net.URLDecoder.decode(r.getQuestion(), "UTF-8"));

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            Random random = new Random();
                            int ran = r.getType().equals("boolean") ? random.nextInt(2) : random.nextInt(4);
                            setOptions(r, ran);
                            question.Answer.add(ran + 1);
                        }
                        Log.v("answers", question.Answer.toString());
                    }
                }

                progresBar.setVisibility(View.INVISIBLE);
                start.setClickable(true);
                Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                intent.putExtra("question", question);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<QuizQuestions> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progresBar.setVisibility(View.INVISIBLE);
                start.setClickable(true);
            }
        });
    }

    void setOptions(Result r, int ran) {
        List<String> wrong;

        switch(ran) {
            case 0:
                try {
                    question.optA.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();

                try {
                    question.optB.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(r.getType().equals("boolean")) {
                    question.optC.add("false");
                    question.optD.add("false");
                    return;
                }

                try {
                    question.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    question.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    question.optB.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();

                try {
                    question.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(r.getType().equals("boolean")) {
                    question.optC.add("false");
                    question.optD.add("false");
                    return;
                }

                try {
                    question.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    question.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    question.optC.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    question.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    question.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    question.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    question.optD.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    question.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    question.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    question.optC.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}