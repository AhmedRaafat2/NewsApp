package com.example.newsapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnRecylerItemClickListner {

    private RecyclerView newsList;
    private ArrayList<New> newArrayList = new ArrayList<>();
    private NewsListAdapter adapter;
    private TextView offline_text;
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
            NewAsync newAsync = new NewAsync();
            newAsync.execute(NEWS_REQUEST_URL);
            try {
                newArrayList = newAsync.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            adapter = new NewsListAdapter(this, newArrayList);
            adapter.setClickListener(this);
            newsList.setAdapter(adapter);
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


    private class NewAsync extends AsyncTask<String, Void, ArrayList<New>> {

        @Override
        protected ArrayList<New> doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                stream.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONObject response = root.getJSONObject("response");
                JSONArray newsArray = response.getJSONArray("results");

                for (int i = 0; i < newsArray.length(); i++) {

                    JSONObject newItem = newsArray.getJSONObject(i);

                    String new_title = newItem.getString("webTitle");
                    String new_topic = newItem.getString("sectionName");
                    String new_full_date = newItem.getString("webPublicationDate");
                    String[] date = new_full_date.split("T");
                    String[] time = date[1].split("Z");
                    String new_url = newItem.getString("webUrl");

                    JSONArray tags = newItem.getJSONArray("tags");
                    String authorName = null;
                    if (!tags.isNull(0)) {
                        JSONObject gettinnTags = tags.getJSONObject(0);
                        authorName = gettinnTags.getString("firstName") + " " + gettinnTags.getString("lastName");

                    } else {
                        authorName = "";
                    }
                    newArrayList.add(new New(new_title, new_topic, date[0], time[0], new_url, authorName));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return newArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<New> newArrayList) {

        }
    }
}