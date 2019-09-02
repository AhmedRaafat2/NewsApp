package com.example.newsapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRecylerItemClickListner, LoaderManager.LoaderCallbacks<ArrayList<New>> {

    private RecyclerView newsList;
    private ArrayList<New> newArrayList = new ArrayList<>();
    private NewsListAdapter adapter;
    private TextView offline_text;
    LoaderManager loaderManager;

    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?q=debate&tag=politics/politics&show-tags=contributor&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsList = findViewById(R.id.news_recycler_list);
        offline_text = findViewById(R.id.offline_text);

        newsList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();
        } else {
            newsList.setVisibility(View.INVISIBLE);
            offline_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view, int position) {

        New selectedNew = newArrayList.get(position);
        String url = selectedNew != null ? selectedNew.getNew_url() : null;
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<New>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewAsyncLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<New>> loader, ArrayList<New> data) {
        newArrayList = data;
        adapter = new NewsListAdapter(this, newArrayList);
        adapter.setClickListener(this);
        newsList.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<New>> loader) {
        newArrayList = null;
    }
}