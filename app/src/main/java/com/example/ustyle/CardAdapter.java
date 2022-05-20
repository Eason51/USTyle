package com.example.ustyle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ustyle.data.DownloadImageTask;
import com.example.ustyle.data.Global;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    // Member variables.
    private ArrayList<Card> mCardData;
    private Context mContext;
    private int mTabPosition;

    /**
     * Constructor that passes in the card data and the context.
     *
     * @param context Context of the application.
     * @param cardData ArrayList containing the sports data.
     */
    CardAdapter(Context context, ArrayList<Card> cardData, int tabPosition) {
        this.mCardData = cardData;
        this.mContext = context;
        this.mTabPosition = tabPosition;


    }

    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent The ViewGroup into which the new View will be added
     *               after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_card, parent, false));
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {



        // Get current sport.
        Card currentCard = mCardData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentCard);
    }

    @Override
    public int getItemCount() {
        return mCardData.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Member Variables for the Card
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mCardImage;

        ViewHolder(View itemView){

            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.cardTitle);
            mInfoText = itemView.findViewById(R.id.cardInfo);
            mCardImage = itemView.findViewById(R.id.cardImage);



            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);


        }

        /**
         *  to get the image resource from the Sport object and load it into the ImageView using Glide
         * @param currentCard
         */
        void bindTo(Card currentCard){
            // Populate the textviews with data.
            mTitleText.setText(currentCard.getTitle());
            mInfoText.setText(currentCard.getInfo());
//            Glide.with(mContext).load(currentCard.getImageResource()).into(mCardImage);

            new DownloadImageTask(mCardImage)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentCard.getImageResource());


        }

        @Override
        public void onClick(View view) {

            if(Global.User.checked == false)
            {
                Context context = Global.context;
                CharSequence text = "Waiting for internet connection!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }

//            Log.d("tabPosition2: ", Integer.toString(mTabPosition));

            //get the Card object for the item that was clicked
            Card currentCard = mCardData.get(getAdapterPosition());

            if(mTabPosition==0) {
                //add an Intent that launches DetailActivity, put the title and image_resource as extras in the Intent,
                //and call startActivity() on the mContext variable, passing in the new Intent.
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("title", currentCard.getTitle());
                detailIntent.putExtra("description", currentCard.getDescription());
                detailIntent.putExtra("image_resource",
                        currentCard.getImageResource());

                mContext.startActivity(detailIntent);
            }else{
                Intent reviewIntent = new Intent(mContext, ReviewActivity.class);
                reviewIntent.putExtra("title", currentCard.getTitle());
                reviewIntent.putExtra("description", currentCard.getDescription());
                reviewIntent.putExtra("image_resource",
                        currentCard.getImageResource());

                mContext.startActivity(reviewIntent);
            }
        }
    }
}
