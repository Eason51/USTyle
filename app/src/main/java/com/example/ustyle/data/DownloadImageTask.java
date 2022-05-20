package com.example.ustyle.data;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.context = Global.context;
    }

    public Context context = null;
    public Boolean showAnimation = false;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.context = context;
        this.bmImage = bmImage;
    }

    public DownloadImageTask(ImageView bmImage, Boolean showAnimation) {
        this.context = Global.context;
        this.bmImage = bmImage;
        this.showAnimation = showAnimation;
    }


    public ProgressDialog dialog = null;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        if(!((Activity) context).isFinishing() && showAnimation)
//        {
//            dialog = new ProgressDialog(Global.context);
//            dialog.setMessage("Loading image...");
//            dialog.show();
//        }
    }


    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);

        if(dialog == null)
            return;

        dialog.dismiss();
    }
}