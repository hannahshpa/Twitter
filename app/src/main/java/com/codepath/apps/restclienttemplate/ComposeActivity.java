package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //resolve the text field from the layout
        etTweet = (EditText) findViewById(R.id.etTweet);

    }
}
