package sample.com.cleartaxtweets;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.cleartaxtweets.api.ClearTaxApplication;
import sample.com.cleartaxtweets.api.TwitterApiService;
import sample.com.cleartaxtweets.api.TwitterServiceProvider;
import sample.com.cleartaxtweets.model.AccessToken;
import sample.com.cleartaxtweets.model.Tweet;
import sample.com.cleartaxtweets.model.TweetList;

public class TweetActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ClearTaxApplication mApp;
    private TwitterApiService mService;
    private Context mContext;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ArrayList<String> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ClearTax tweets");
        setSupportActionBar(toolbar);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mApp = ClearTaxApplication.getInstance();
        final String access_token = mApp.getAccessToken();
        boolean isConfigured = access_token != null && !access_token.isEmpty();
        mService = TwitterServiceProvider.getService(isConfigured);
        if(isConfigured){
            Call<TweetList> tweetListCall = mService.getTweetList("cleartax", "recent", 100);
            tweetListCall.enqueue(mTweetListCallback);
        }else {
            Call<AccessToken> accessTokenCall = mService.getAccessToken("client_credentials");
            accessTokenCall.enqueue(mTokenCallback);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This feature is coming soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private Callback<AccessToken> mTokenCallback = new Callback<AccessToken>() {
        @Override
        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
            AccessToken accessToken = response.body();
            if(accessToken != null){
                mApp.setAccessToken(accessToken.accessToken);
                mApp.setTokenType(accessToken.tokenType);
                Call<TweetList> tweetListCall = TwitterServiceProvider.getService(true).getTweetList("cleartax","recent", 100);
                tweetListCall.enqueue(mTweetListCallback);
            }
        }

        @Override
        public void onFailure(Call<AccessToken> call, Throwable t) {

        }
    };

    private Callback<TweetList> mTweetListCallback = new Callback<TweetList>() {
        @Override
        public void onResponse(Call<TweetList> call, Response<TweetList> response) {
            final TweetList tweetList = response.body();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        mList = calculateFrequency(tweetList);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCollapsingToolbar.post(new Runnable() {
                        @Override
                        public void run() {
                            mCollapsingToolbar.setTitle(mList.get(0)+" " + mList.get(1) + " " + mList.get(2));
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            new Thread(runnable).start();
            mAdapter = new TweetAdapter(tweetList, mContext);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onFailure(Call<TweetList> call, Throwable t) {

        }
    };

    public ArrayList<String> calculateFrequency(TweetList tweetList) {
        String str1="", str2="", str3="";
        int f1=0, f2=0, f3=0;
        HashMap<String, Integer> mMap = new HashMap<>();
        for(Tweet tweet : tweetList.tweets) {
            String[] words = tweet.text.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].replaceAll("[^\\w]", "");
            }
            for(String string: words) {
                Integer count = mMap.get(string);
                if(count == null) count = 0;
                mMap.put(string, ++count);
            }
        }

        for(Map.Entry entry: mMap.entrySet()) {
            Integer val = (int) entry.getValue();
            String key = (String) entry.getKey();
            if(val > f1){
                f3 = f2;
                str3 = str2;
                f2=f1;
                str2=str1;
                f1=val;
                str1 = key;

            }
            else if(val > f2) {
                f3 = f2;
                str3 = str2;
                f2 = val;
                str2 = key;
            }
            else if(val > f3 ) {
                f3 = val;
                str3 = key;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(str1);
        list.add(str2);
        list.add(str3);
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
