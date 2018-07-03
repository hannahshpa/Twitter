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
//        //set the text field's content from the intent
//        etTweet.setText(getIntent().getStringExtra(TimelineActivity.ITEM_TEXT));
//        //track the position of the item in the list
//        position = getIntent().getIntExtra(MainActivity.ITEM_POSITION,0);
//        //set the title bar to reflect the purpose of the view
//        getSupportActionBar().setTitle("Edit Item");
    }



//    public class EditItemActivity extends AppCompatActivity {
//
//        //text field containing updated item description
//        EditText etItemText;
//        //we need to track the item's position in the list
//        int position;
//
//            //resolve the text field from the layout
//            etItemText = (EditText) findViewById(R.id.etItemText);
//            //set the text field's content from the intent
//            etItemText.setText(getIntent().getStringExtra(MainActivity.ITEM_TEXT));
//            //track the position of the item in the list
//            position = getIntent().getIntExtra(MainActivity.ITEM_POSITION,0);
//            //set the title bar to reflect the purpose of the view
//            getSupportActionBar().setTitle("Edit Item");
//        }
//
//        public void onSaveItem(View v) {
//            //prepare intent to pass back to MainActivity
//            Intent data = new Intent();
//            //pass updated item text and original position
//            data.putExtra(MainActivity.ITEM_TEXT, etItemText.getText().toString());
//            data.putExtra(MainActivity.ITEM_POSITION, position); //ints work too
//            setResult(RESULT_OK, data); //set result code and bundle data for response
//            finish();   //closes the edit activity, passes intent back to main
//        }


}
