package com.example.ustyle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ustyle.data.Global;
import com.example.ustyle.data.PostRequest;

import org.json.JSONObject;

public class AddReviewActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY =
            "AddReviewActivity.REPLY";

    private EditText mEditCommentView;

    private String facilityName = "";
    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mEditCommentView = findViewById(R.id.edit_comment);

        facilityName = getIntent().getStringExtra("facilityName");
        userID = getIntent().getStringExtra("userID");

        final Button button = findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (TextUtils.isEmpty(mEditCommentView.getText())) {
                    Context context = Global.context;
                    CharSequence text = "Empty comment is not allowed!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else {
                    String comment = mEditCommentView.getText().toString();

                    String requestBaseURL = Global.BaseURL.addBaseURL;

                    String queryString = String.format("" +
                            "db.collection(\"comments\").add({" +
                            "    data: [{" +
                            "        userID: \"%s\"," +
                            "        facilityName: \"%s\"," +
                            "        content: \"%s\"" +
                            "    }]" +
                            "})"
                    , userID, facilityName, comment);

                    new PostRequest(
                            new PostRequest.PostRequestResult() {
                                @Override
                                public void processResult(JSONObject resultJSON) {
                                    finish();
                                }
                            }
                    ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryString, requestBaseURL);
//                            .execute();

                }
            }
        });
    }
}