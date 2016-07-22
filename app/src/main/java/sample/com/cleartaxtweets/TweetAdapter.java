package sample.com.cleartaxtweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sample.com.cleartaxtweets.model.Tweet;
import sample.com.cleartaxtweets.model.TweetList;

/**
 * Created by ladia on 21/07/16.
 */
public class TweetAdapter extends RecyclerView.Adapter {

    private TweetList mTweetList;
    private Context mContext;
    public TweetAdapter(TweetList tweetList, Context context) {
        mTweetList = tweetList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_card, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TweetViewHolder vh = ((TweetViewHolder)holder);
        Tweet tweet = mTweetList.tweets.get(position);
        vh.textTweet.setText(tweet.text);
        vh.textUser.setText(tweet.user.name);
        Picasso.with(mContext)
                .load(tweet.user.profileImageUrl)
                .fit()
                .centerCrop()
                .into(vh.imageLogo);
    }

    @Override
    public int getItemCount() {
        if (mTweetList.tweets != null) {
            return mTweetList.tweets.size();
        } else {
            return 0;
        }
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {

        TextView textTweet;
        TextView textUser;
        ImageView imageLogo;

        public TweetViewHolder(View itemView) {
            super(itemView);
            textTweet = (TextView) itemView.findViewById(R.id.card_text);
            textUser = (TextView) itemView.findViewById(R.id.card_title);
            imageLogo = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }
}
