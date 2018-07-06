package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweet;
    private TwitterClient client;
    String itemText;
    private TextView charCount;

    public ImageView ivProfileImage;
    public TextView tvUsername;
    TweetAdapter adapter;
    public Button tweetButton;
    boolean isReplying;
    Tweet tweet;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            charCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        //resolve the text field from the layout
        etTweet = (EditText) findViewById(R.id.etTweet);
        charCount = (TextView) findViewById(R.id.charCount);
        etTweet.addTextChangedListener(mTextEditorWatcher);

        //perform findViewById lookups
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tweetButton = (Button) findViewById(R.id.tweetButton);
        isReplying = false;

        //Unwrap the boolean that is passed in via intent
        if (getIntent().hasExtra("originalTweet")) {
            Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("originalTweet"));
            etTweet.setText("@" + tweet.user.screenName);
            isReplying = true;
        }
    }
    public void composeTweet(View view) {
        itemText = etTweet.getText().toString();
        client.sendTweet(itemText, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet tweet = null;
                try {
                    tweet = tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                intent.putExtra("Tweet", Parcels.wrap(tweet));

                // Activity finished ok, return the data
                setResult(2, intent); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
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


    public void returnScreen(View view) {
        Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
        getApplicationContext().startActivity(intent);
    }
}
