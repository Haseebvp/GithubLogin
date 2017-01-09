package com.githubconnect.githubconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import GithubFiles.GithubApp;
import utils.EnvConstants;


public class SplashSCreen extends AppCompatActivity {

    TextView login, appname;
    Handler handler = new Handler();
    private GithubApp mApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        login = (TextView) findViewById(R.id.login);
        appname = (TextView) findViewById(R.id.appname);
        mApp = new GithubApp(this, EnvConstants.CLIENT_ID,
                EnvConstants.CLIENT_SECRET, EnvConstants.CALLBACK_URL);
        mApp.setListener(listener);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mApp.hasAccessToken()){
                    Intent repolist = new Intent(SplashSCreen.this, RepoList.class);
                    repolist.putExtra("Url", mApp.getRepoUrl().substring(GithubApp.API_URL.length()+1,mApp.getRepoUrl().length()));
                    startActivity(repolist);
                    finish();
                }
                else {
                    login.setVisibility(View.VISIBLE);
                }


            }
        }, 2000);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mApp.hasAccessToken()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            SplashSCreen.this);
                    builder.setMessage("Disconnect from GitHub?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            mApp.resetAccessToken();
//                                            login.setText("Connect");
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    mApp.authorize();
                }
            }


        });

    }

    GithubApp.OAuthAuthenticationListener listener = new GithubApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            Intent repolist = new Intent(SplashSCreen.this, RepoList.class);
            repolist.putExtra("Url", mApp.getRepoUrl().substring(GithubApp.API_URL.length()+1,mApp.getRepoUrl().length()));
            startActivity(repolist);
            finish();
        }

        @Override
        public void onFail(String error) {
            Toast.makeText(SplashSCreen.this, error, Toast.LENGTH_SHORT).show();
        }
    };
}
