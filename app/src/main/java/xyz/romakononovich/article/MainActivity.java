package xyz.romakononovich.article;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import xyz.romakononovich.article.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private User user = null;
    private FrameLayout f1;
    private TextView textView;
    private View container;
    private List<Article> articleList = new ArrayList<>();
    private Spinner spinner;
    private String source;
    private RecyclerView recyclerView;
    private RvAdaptepSearchItem adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> list = new ArrayList<>();
        list.add("CNBC");
        list.add("Al Jazeera English");
        list.add("Ars Technica");
        list.add("Associated Press");
        list.add("BBC News");
        list.add("BBC Sport");

        final List<String> listAPI = new ArrayList<>();
        listAPI.add("cnbc");
        listAPI.add("al-jazeera-english");
        listAPI.add("ars-technica");
        listAPI.add("associated-press");
        listAPI.add("bbc-news");
        listAPI.add("bbc-sport");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        f1 = (FrameLayout) findViewById(R.id.progress_bar);
        //textView = (TextView) findViewById(R.id.myText);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgressBar();

                source=listAPI.get(position);
                parse(source);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void parse(String source) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        String url = "https://newsapi.org/v1/articles?source=";
        String api = "&apiKey=c0b21a0e305f4ad494f9b55db4e9d43d";


        builder.url(url+source+api);


        //c0b21a0e305f4ad494f9b55db4e9d43d
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "call: " + call.toString() + "error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d(TAG, "source: " + jsonObject.get("source"));
                    JSONArray articles = jsonObject.getJSONArray("articles");
                    articleList.clear();
                    for(int i = 0; i < articles.length(); i++){
                        JSONObject jsonObject1 = new JSONObject(articles.get(i).toString());
                        Article article = new Article();
                        article.setAuthor(jsonObject1.get("author").toString());
                        article.setDescription(jsonObject1.get("description").toString());
                        article.setTitle(jsonObject1.get("title").toString());
                        article.setPublishedAt(formateDate(jsonObject1.get("publishedAt").toString()));
                        article.setUrlToImage(jsonObject1.get("urlToImage").toString());
                        articleList.add(article);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgressBar();
                            setArticles();
                            hideProgressBar();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showProgressBar() {
        f1.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        f1.setVisibility(View.GONE);
    }
    public String formateDate(String date) {
        if (date.length()>5) {
            String month = date.substring(5, 7);
            String dateI = date.substring(8, 10);
            String hour = date.substring(11, 13);
            String minute = date.substring(14, 16);
            return month + "/" + dateI + "  " + hour + ":" + minute;
        } else return "-----";
    }


    public static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private void setArticles(){
        adapter = new RvAdaptepSearchItem(articleList, getBaseContext());
        recyclerView.setAdapter(adapter);
//        StringBuilder stringBuilder = new StringBuilder(articleList.size()*2);
//        for(Article article: articleList){
//            stringBuilder.append(article.toString());
//            stringBuilder.append("\n");
//        }
//        textView.setText(stringBuilder.toString());
    }
}