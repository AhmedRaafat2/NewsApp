package com.example.newsapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
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
import java.util.ArrayList;

public class NewAsyncLoader extends AsyncTaskLoader<ArrayList<New>> {

    private ArrayList<New> requestedArrayListNews = new ArrayList<>();
    private String urlconn;
    public NewAsyncLoader(@NonNull Context context,String url) {
        super(context);
        this.urlconn = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<New> loadInBackground() {

        try {
            URL url = new URL(urlconn);
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
                requestedArrayListNews.add(new New(new_title, new_topic, date[0], time[0], new_url, authorName));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestedArrayListNews;
    }
}