package sample.com.cleartaxtweets.api;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by ladia on 21/07/16.
 */

public class ClearTaxApplication extends Application {

    private static ClearTaxApplication mInstance;
    private volatile SharedPreferences mSharedPrefs = null;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String TOKEN_TYPE = "token_type";

    public static ClearTaxApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mSharedPrefs = getApplicationContext().getSharedPreferences("clear_tax_prefs", 0);
    }

    public String getAccessToken() {
        return mSharedPrefs.getString(ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getTokenType() {
        return mSharedPrefs.getString(TOKEN_TYPE, null);
    }

    public void setTokenType(String tokenType) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(TOKEN_TYPE, tokenType);
        editor.apply();
    }

}
