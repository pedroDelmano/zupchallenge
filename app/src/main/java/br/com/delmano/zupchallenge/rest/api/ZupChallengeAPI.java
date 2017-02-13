package br.com.delmano.zupchallenge.rest.api;

import br.com.delmano.zupchallenge.model.Movie;
import br.com.delmano.zupchallenge.model.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

public interface ZupChallengeAPI {

    @GET("/")
    Call<Search> search(@Query("s") String query, @Query("page") int page);

    @GET("/")
    Call<Movie> details(@Query("i") String id);
}
