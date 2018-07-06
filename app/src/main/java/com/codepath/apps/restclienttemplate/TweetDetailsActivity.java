package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTimestamp;
    public ImageButton favoriteButton;
    public ImageButton retweetButton;

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


        //Unwrap the tweet that is passed in via intent, using its simple name as key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", "tweet"));


        //populate the views according to this data
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimestamp.setText(tweet.relativeDate);

        //insert profile pic using Glide
        Glide.with(getApplicationContext())
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);



    }

    public void exitDetails(View view) {
        //create the intent for this activity
        Intent intent = new Intent(this, TimelineActivity.class);

        //show the activity
        this.startActivity(intent);
    }


}
