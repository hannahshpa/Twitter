package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView otherName;
    public TextView tvBody;
    public TextView tvTimestamp;
    public ImageButton favoriteButton;
    public ImageButton retweetButton;
    public EditText replyText;
    public TextView numRetweets;
    public TextView numFaves;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);


        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        favoriteButton = (ImageButton) findViewById(R.id.favoriteButton);
        retweetButton = (ImageButton) findViewById(R.id.retweetButton);
        replyText = (EditText) findViewById(R.id.tvReply);
        numRetweets = (TextView) findViewById(R.id.numRetweets);
        numFaves = (TextView) findViewById(R.id.numFaves);
        client = TwitterApp.getRestClient(getApplicationContext());
        otherName = (TextView) findViewById(R.id.otherName);

        //Unwrap the tweet that is passed in via intent, using its simple name as key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", "tweet"));


        //populate the views according to this data
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimestamp.setText(tweet.relativeDate);
        numRetweets.setText(tweet.numRetweets);
        numFaves.setText(tweet.numFaves);
        otherName.setText("@" + tweet.user.screenName);

        //insert profile pic using Glide
        Glide.with(getApplicationContext())
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);
    }

    public void exitDetails(View view) {
        //create the intent for this activity
        Intent intent = new Intent(this, TimelineActivity.class);

        //show the activity
        this.startActivity(intent);
    }

    public void sendReply(View view) {
        //handle hitting the reply button
        tweet.inReply = true;
        Intent intent = new Intent(this, ComposeActivity.class);
        intent.putExtra("originalTweet", Parcels.wrap(tweet));
        this.startActivity(intent); // brings up the second activity
    }

    public void favorite(final View view) {
        client.favorite(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                tweet.favorited = true;
                view.setSelected(true);
                try {
                    tweet.numFaves = response.getString("favorite_count");
                    numFaves.setText(tweet.numFaves);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TweetAdapter", "successful");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TweetAdapter", "first onfailure");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                super.onFailure(statusCode, headers, throwable, object);
                Log.e("TweetAdapter", "second onfailure");
            }
        });
    }

    public void retweet(final View view) {
        client.retweet(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                tweet.retweeted = true;
                view.setSelected(true);
                try {
                    tweet.numRetweets = response.getString("retweet_count");
                    numRetweets.setText(tweet.numRetweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                super.onFailure(statusCode, headers, throwable, object);
            }
        });
    }
}
