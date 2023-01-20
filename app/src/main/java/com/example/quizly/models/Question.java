package com.example.quizly.models;

import android.content.Context;

import com.example.quizly.api.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Question implements Serializable {
    public transient Context context;
    public List<Result> results;
    public List<String> question;
    public List<String> optA;
    public List<String> optB;
    public List<String> optC;
    public List<String> optD;
    public List<Integer> Answer;

    public Question(Context context) {
        this.context = context;
        question = new ArrayList<>();
        results = new ArrayList<>();
        optA = new ArrayList<>();
        optB = new ArrayList<>();
        optC = new ArrayList<>();
        optD = new ArrayList<>();
        Answer = new ArrayList<>();
    }
}
