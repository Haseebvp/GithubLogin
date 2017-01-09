package com.githubconnect.githubconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import GithubFiles.GithubApp;
import GithubFiles.GithubSession;
import adapters.RepoAdaptor;
import network.ApiClient;
import network.ApiInterface;
import network.RepoData;
import network.SingleRepoData;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import viewDecorators.CustomItemDecorator;

public class SingleRepo extends AppCompatActivity {

    TextView title, desc, errorText;
    LinearLayout commits, files;
    ScrollView scrollView;
    ProgressBar progressBar;

    String name, description;
    ApiInterface apiInterface;
    Subscription subscription;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public static final String API_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_repo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");

        title = (TextView) findViewById(R.id.title);
        errorText = (TextView) findViewById(R.id.errorText);
        desc = (TextView) findViewById(R.id.desc);
        commits = (LinearLayout) findViewById(R.id.commits_placeholder);
        files = (LinearLayout) findViewById(R.id.files_placeholder);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        getData();


    }

    public void getData() {
        System.out.println("GETURL : " + getUrl());
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        Observable<SingleRepoData> dataList = apiInterface.getRepoData(getUrl());
        dataList.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        subscription = dataList.subscribe(new Observer<SingleRepoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("RETRO ERROR :" + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Oops. Something went wrong.", Toast.LENGTH_SHORT);

                    }
                });

            }

            @Override
            public void onNext(final SingleRepoData singleRepoData) {
                System.out.println("DATAAAA : " + singleRepoData + "-----" + singleRepoData.getFiles().size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        DisplayData(singleRepoData);
                    }
                });
            }


        });
    }

    public void DisplayData(SingleRepoData data) {
        title.setText(name);
        desc.setText(description);
        System.out.println("INSIDE DATA");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < data.getFiles().size(); i++) {
            System.out.println("INSIDE DATA : " + data.getFiles().get(i).getFilename());
            TextView tx = new TextView(this);
            tx.setPadding(5, 5, 5, 5);
            tx.setLayoutParams(lp);
            tx.setText(data.getFiles().get(i).getFilename());
            files.addView(tx);
        }
        TextView tx = new TextView(this);
        tx.setPadding(5, 5, 5, 5);
        tx.setLayoutParams(lp);
        tx.setText(data.getCommit().getMessage());
        commits.addView(tx);

    }


    public String getUrl() {
        String url = "/repos/" +getUsername() +"/" + name + "/commits/master";
        return url;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public String getUsername() {
        sharedPref = getSharedPreferences(GithubSession.SHARED, Context.MODE_PRIVATE);
        return sharedPref.getString(API_USERNAME, null);
    }
}
