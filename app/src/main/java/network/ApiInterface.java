package network;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by haseeb on 7/1/17.
 */

public interface ApiInterface {

    @GET
    Observable<List<RepoData>> getData(@Url String url);


    @GET
    Observable<SingleRepoData> getRepoData(@Url String url);

}
