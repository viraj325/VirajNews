package com.projectbridge.virajnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<newsModel> newsList = new ArrayList<>();
    private newsAdapter na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsRecycler = findViewById(R.id.newsList);

        na = new newsAdapter(this, newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        newsRecycler.setLayoutManager(mLayoutManager);
        newsRecycler.setItemAnimator(new DefaultItemAnimator());
        newsRecycler.setAdapter(na);

        getNews();
    }

    private void getNews(){
        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=eda78c52fd7a4440984c6671328d2307";
        StringRequest stringRequest = new StringRequest(url, this::newsJson, error -> Toast.makeText(this, "News Error", Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void newsJson(String response){
        try {
            JSONObject feedObj = new JSONObject(response);
            JSONArray articles = feedObj.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject newsData = articles.getJSONObject(i);

                newsModel item = new newsModel();

                item.setAuthor(newsData.getString("author"));
                item.setTitle(newsData.getString("title"));
                item.setDescription(newsData.getString("description"));
                item.setUrl(newsData.getString("url"));
                item.setUrlToImage(newsData.getString("urlToImage"));
                Log.e("iosnkenrpvgwnvkmkpfmv", newsData.getString("urlToImage"));
                item.setTime(newsData.getString("publishedAt"));
                item.setContent(newsData.getString("content"));

                newsList.add(item);
            }
            // notify data changes to list adapter
            na.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}