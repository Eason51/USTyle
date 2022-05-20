package com.example.ustyle.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class Loading extends AsyncTask<Void, Void, Void> {

    public Context context = null;
    public ProgressDialog dialog = null;

    public Loading(){
        context = Global.context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(!((Activity) context).isFinishing())
        {
            dialog = new ProgressDialog(Global.context);
            dialog.setMessage("Loading image...");
            dialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(Global.threadCount != 0);
        return null;
    }

    @Override
    protected void onPostExecute(Void voids){
        if(dialog == null)
            return;

        dialog.dismiss();
    }
}
