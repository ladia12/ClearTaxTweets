package sample.com.cleartaxtweets.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sample.com.cleartaxtweets.model.AccessToken;
import sample.com.cleartaxtweets.model.TweetList;

public interface TwitterApiService {

	@GET(ApiConstants.TWITTER_HASHTAG_SEARCH_CODE )
	Call<TweetList> getTweetList(
			@Query("q") String hashtag,
            @Query("result_type") String resultType,
            @Query("count") int count
	);

	@FormUrlEncoded
	@POST("/oauth2/token")
	Call<AccessToken> getAccessToken(
			@Field("grant_type") String grantType);
}

