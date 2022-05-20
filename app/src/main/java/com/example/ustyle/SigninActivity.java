package com.example.ustyle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ustyle.data.CloudFunctionRequest;
import com.example.ustyle.data.Global;
import com.example.ustyle.data.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SigninActivity extends AppCompatActivity {
    EditText mUsernameText, mItscText, mVerifyCodeText;
    Button signInButton;
    private String username;
    private String itsc;
    private String verifyCode;
    private String generatedCode = "";

    public SigninActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_signin);

        mUsernameText = findViewById(R.id.usernameText);
        mItscText = findViewById(R.id.emailText);
        mVerifyCodeText = findViewById(R.id.verifyCodeText);
        signInButton = findViewById(R.id.signInButton);

        mUsernameText.addTextChangedListener(textWatcher);
        mItscText.addTextChangedListener(textWatcher);
        mVerifyCodeText.addTextChangedListener(textWatcher);

    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mUsernameText.getText().toString().length()!=0
                    && mItscText.getText().toString().length()!=0
                    && mVerifyCodeText.getText().toString().length()!=0){
                signInButton.setEnabled(true);
            }
        }
    };



    public void getVerifyCode(View view) {
        itsc = mItscText.getText().toString();
        if(itsc.length() == 0)
        {

            Context context = Global.context;
            CharSequence text = "Input your itsc";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        String itscAddress = itsc + "@connect.ust.hk";
        int randomNum = new Random().nextInt(900000) + 100000;

        String randomNumString = Integer.toString(randomNum);
        generatedCode = randomNumString;

        String requestBaseURL = Global.BaseURL.cloudFunctionURL;

//        String queryString = String.format("           " +
//                "                \"emailAddress\": \"%s\",\n" +
//                "                \"code\": \"%s\"\n",
//                itscAddress, randomNumString);
        String queryString = "";

        String functionName = "hkust_emailRegistration";

        Log.d("itsc: ", itscAddress);

        new CloudFunctionRequest(
                new CloudFunctionRequest.PostRequestResult() {
                    @Override
                    public void processResult(JSONObject resultJSON) {
                        Log.d("function result: ", resultJSON.toString());
                    }
                }
        ).execute(queryString, requestBaseURL, functionName, randomNumString, itscAddress);
    }




    public void signIn(View view) {
        username = mUsernameText.getText().toString();
        itsc = mItscText.getText().toString();
        verifyCode = mVerifyCodeText.getText().toString();

        if(username.length() == 0)
        {
            Context context = Global.context;
            CharSequence text = "Input your userName";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        else if(itsc.length() == 0)
        {
            Context context = Global.context;
            CharSequence text = "Input your itsc";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        else if(verifyCode.length() == 0)
        {
            Context context = Global.context;
            CharSequence text = "Input your verification code";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        else if(generatedCode.length() == 0)
        {
            Context context = Global.context;
            CharSequence text = "Request a verification code";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        else if(!generatedCode.equals(verifyCode))
        {
            Context context = Global.context;
            CharSequence text = "wrong verification code";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }



        String requestBaseURL = Global.BaseURL.addBaseURL;
        String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String queryString = String.format("" +
                        "db.collection(\"users\").add({" +
                        "    data: [{" +
                        "        userName: \"%s\"," +
                        "        itsc: \"%s\"," +
                        "        androidID: \"%s\"" +
                        "    }]" +
                        "})"
                , username, itsc, androidID);

        new PostRequest(
                new PostRequest.PostRequestResult() {
                    @Override
                    public void processResult(JSONObject resultJSON) {
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        ).execute(queryString, requestBaseURL);



//
//        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
//        startActivity(intent);
    }
}