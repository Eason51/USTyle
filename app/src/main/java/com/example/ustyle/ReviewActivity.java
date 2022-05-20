package com.example.ustyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ustyle.data.DownloadImageTask;
import com.example.ustyle.data.Global;
import com.example.ustyle.data.PostRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<ReviewCard> mReviewData;
    private ReviewCardAdapter mAdapter;
    public static final int NEW_REVIEW_ACTIVITY_REQUEST_CODE = 1;

    private String facilityName = "";


    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").

        mRecyclerView = findViewById(R.id.recyclerViewReview);

        // Initialize the ArrayList that will contain the data.
        mReviewData = new ArrayList<>();
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ReviewCardAdapter(this, mReviewData);
        mRecyclerView.setAdapter(mAdapter);

        initializeReviewData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = findViewById(R.id.backToolbar_review);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize
        TextView cardTitle = findViewById(R.id.cardTitle_review);
        TextView cardDescription = findViewById(R.id.cardDescription_review);
        ImageView cardImage = findViewById(R.id.cardImage_review);

        facilityName = getIntent().getStringExtra("title");
        cardTitle.setText(getIntent().getStringExtra("title"));
        cardDescription.setText(getIntent().getStringExtra("description"));
//        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
//                .into(cardImage);

        new DownloadImageTask(cardImage, true)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,getIntent()
                        .getStringExtra("image_resource"));
//                .execute(getIntent().getStringExtra("image_resource"));

        // set up recycler view
        mRecyclerView = findViewById(R.id.recyclerViewReview);

        // Initialize the ArrayList that will contain the data.
        mReviewData = new ArrayList<>();
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ReviewCardAdapter(this, mReviewData);
        mRecyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Global.User.loggedIn == false)
                {
                    Context context = Global.context;
                    CharSequence text = "Log in first before commenting";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
                intent.putExtra("facilityName", facilityName);
                intent.putExtra("userID", Global.User.userID);
                startActivityForResult(intent, NEW_REVIEW_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Initialize the review data from resources.
     */
    private void initializeReviewData() {

        String requestBaseURL = Global.BaseURL.queryBaseURL;
        String queryString = String.format("db.collection(\"comments\")" +
                        ".where({facilityName: \"%s\"})" +
                        ".limit(100).get()"
                , facilityName);

        new PostRequest(
                new PostRequest.PostRequestResult() {
                    @Override
                    public void processResult(JSONObject resultJSON) {
                        try {
                            JSONArray dataJSONArr = resultJSON.getJSONArray("data");
                            for(int i = 0; i != dataJSONArr.length(); ++i)
                            {
                                String dataJSONString = dataJSONArr.getString(i);
                                JSONObject dataJSON = new JSONObject(dataJSONString);
                                //mReviewData.add(new ReviewCard(userList[i], getResources().getString(R.string.comments_example)));
                                String userID = dataJSON.getString("userID");
                                String content = dataJSON.getString("content");



                                String requestBaseURL = Global.BaseURL.queryBaseURL;
                                String queryString = String.format("db.collection(\"users\")" +
                                                ".where({_id: \"%s\"})" +
                                                ".limit(100).get()"
                                        , userID);

                                new PostRequest(
                                        new PostRequest.PostRequestResult() {
                                            @Override
                                            public void processResult(JSONObject resultJSON) {
                                                try {
                                                    JSONArray dataJSONArr = resultJSON.getJSONArray("data");
                                                    String dataJSONString = dataJSONArr.getString(0);
                                                    JSONObject dataJSON = new JSONObject(dataJSONString);

                                                    String userName = dataJSON.getString("userName");

                                                    //mReviewData.add(new ReviewCard(userList[i], getResources().getString(R.string.comments_example)));
                                                    ReviewCard reviewCard = new ReviewCard(
                                                            userName, content
                                                    );

                                                    mReviewData.add(reviewCard);
                                                    mAdapter.notifyDataSetChanged();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        },
                                        ReviewActivity.this
                                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryString, requestBaseURL);
//                                        .execute(queryString, requestBaseURL);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                this
        )
                .execute(queryString, requestBaseURL);
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,queryString, requestBaseURL);

    }




//    private void initializeReviewData2() {
//        // Get the resources from the XML file.
//        String[] userList = getResources()
//                .getStringArray(R.array.usernames);
//
//        // Clear the existing data (to avoid duplication).
//        mReviewData.clear();
//
//        // Create the ArrayList of Card objects with titles and
//        // image about each facility.
//        for (int i = 0; i < userList.length; i++) {
//            mReviewData.add(new ReviewCard(userList[i], getResources().getString(R.string.comments_example)));
//        }
//
//        // Notify the adapter of the change.
//        mAdapter.notifyDataSetChanged();
//    }



    // this event will enable the back function to the nav button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}