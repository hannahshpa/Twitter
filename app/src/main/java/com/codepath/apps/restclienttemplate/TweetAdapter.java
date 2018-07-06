package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    //class variables
    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;

    //pass in the Tweets array in the constructor
    public TweetAdapter (List<Tweet> tweets) {
        mTweets = tweets;
    }

    //for each row, inflate the layout and cache references into ViewHolder class
    @NonNull
    @Override   //invoked when you need to create a new row
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        client = TwitterApp.getRestClient(context);

        //inflate the tweet row
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    //called by the adapter most of the time as the user scrolls down
    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data for specific tweet object based on position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(tweet.relativeDate);
        holder.numRetweets.setText(tweet.numRetweets);
        holder.numFaves.setText(tweet.numFaves);


        //insert profile pic using Glide
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create ViewHolder class that contains the findViewById lookups
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public TextView numRetweets;
        public TextView numFaves;
        public ImageButton favoriteButton;
        public ImageButton retweetButton;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            numRetweets = (TextView) itemView.findViewById(R.id.numRetweets);
            numFaves = (TextView) itemView.findViewById(R.id.numFaves);
            favoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
            retweetButton = (ImageButton) itemView.findViewById(R.id.retweetButton);
            itemView.setOnClickListener(this);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int position = getAdapterPosition();
                    final Tweet tweet = mTweets.get(position);

                    client.favorite(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.favorited = true;
                            v.setSelected(true);
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
            });


            retweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int position = getAdapterPosition();
                    final Tweet tweet = mTweets.get(position);
                    client.retweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.retweeted = true;
                            v.setSelected(true);
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
            });
        }

        @Override
        public void onClick(View v) {
            //get the position of the item
            int position = getAdapterPosition();
            //check if the position is valid and exists in the view
            if(position != RecyclerView.NO_POSITION) {
                //get movie at selected position
                Tweet tweet = mTweets.get(position);
                //create the intent for this activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                //serialize the movie using parceler, use its short name as a key
                intent.putExtra("tweet", Parcels.wrap(tweet));

                //show the activity
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
