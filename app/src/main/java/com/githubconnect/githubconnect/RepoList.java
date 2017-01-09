package com.githubconnect.githubconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import GithubFiles.GithubApp;
import GithubFiles.GithubSession;
import adapters.RepoAdaptor;
import network.ApiClient;
import network.ApiInterface;
import network.RepoData;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import viewDecorators.CustomItemDecorator;

public class RepoList extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    RecyclerView repolist;
    ProgressBar progress;
    String Url;
    ApiInterface apiInterface;
    Subscription subscription;
    RepoAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);

        Url = getIntent().getStringExtra("Url");

        repolist = (RecyclerView) findViewById(R.id.repolist);
        progress = (ProgressBar) findViewById(R.id.progress);

        System.out.println("GETURLLL : "+Url);
        fetchData();
    }

    private void fetchData() {
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        Observable<List<RepoData>> dataList = apiInterface.getData(Url);
        dataList.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        subscription = dataList.subscribe(new Observer<List<RepoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("RETRO ERROR :" + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Oops. Something went wrong.", Toast.LENGTH_SHORT);

                    }
                });

                //                Snackbar.make(drawer, "There is an error it seems!", Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onNext(final List<RepoData> repoDatas) {
                System.out.println("RESPPPP : "+repoDatas + "--" + repoDatas.size());
                for (int i=0;i<repoDatas.size();i++){
                    System.out.println("DATA : "+ repoDatas.get(i).getName() + "----" + repoDatas.get(i).getDescription());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        repolist.setHasFixedSize(true);
                        LinearLayoutManager lm = new LinearLayoutManager(RepoList.this);
                        repolist.setLayoutManager(lm);
                        adaptor = new RepoAdaptor(repoDatas, getApplicationContext());
                        repolist.setAdapter(adaptor);
                        repolist.addItemDecoration(new CustomItemDecorator(getResources().getDrawable(R.drawable.line_divider)));
                    }
                });
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.settings).setEnabled(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Logging out.", Toast.LENGTH_LONG);
                logout();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent splashScreen = new Intent(RepoList.this, SplashSCreen.class);
                        startActivity(splashScreen);
                        finish();
                    }
                },1000);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    public void logout() {
        sharedPref = getSharedPreferences(GithubSession.SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(GithubSession.API_ID, null);
        editor.putString(GithubSession.API_ACCESS_TOKEN, null);
        editor.putString(GithubSession.API_USERNAME, null);
        editor.putString(GithubSession.API_REPO, null);
        editor.commit();
    }
}
