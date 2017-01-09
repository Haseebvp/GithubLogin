package network;

import android.content.Context;

import GithubFiles.GithubApp;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by haseeb on 7/01/16.
 */
public class ApiClient {

    private static Retrofit retrofit = null;



    public static Retrofit getClient(final Context context) {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxAdapter)
                    .baseUrl(GithubApp.API_URL)
                    .build();
        }
        return retrofit;
    }
}