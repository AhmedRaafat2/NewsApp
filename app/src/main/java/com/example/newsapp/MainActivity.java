package com.example.newsapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView newsList;
    private ArrayList<New> newArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsList = findViewById(R.id.news_recycler_list);
        newsList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


        newArrayList.add(new New("dddddddd","ggggggg","hhhhhhhhhh","ggggggggg"));
        newArrayList.add(new New("dddddddd","ggggggg","hhhhhhhhhh","ggggggggg"));
        newArrayList.add(new New("dddddddd","ggggggg","hhhhhhhhhh","ggggggggg"));

        NewsListAdapter adapter = new NewsListAdapter(this,newArrayList);
        newsList.setAdapter(adapter);
    }
}
