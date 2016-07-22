package sample.com.cleartaxtweets.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ladia on 21/07/16.
 */
public class AccessToken {

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("access_token")
    public String accessToken;
}
