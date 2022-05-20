package com.example.ustyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ustyle.data.DownloadImageTask;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.backToolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize
        TextView cardTitle = findViewById(R.id.cardTitle_detail);
        TextView cardDescription = findViewById(R.id.cardDescription_detail);
        ImageView cardImage = findViewById(R.id.cardImage_detail);

        cardTitle.setText(getIntent().getStringExtra("title"));
        cardDescription.setText(getIntent().getStringExtra("description"));
//        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
//                .into(cardImage);

        new DownloadImageTask(cardImage, true)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,getIntent()
                        .getStringExtra("image_resource"));
//                .execute();
    }

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