package com.example.ustyle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewCardAdapter extends RecyclerView.Adapter<ReviewCardAdapter.ViewHolder>{
    // Member variables.
    private ArrayList<ReviewCard> mReviewData;
    private Context mContext;
    /**
     * Constructor that passes in the card data and the context.
     *
     * @param context Context of the application.
     * @param reviewData ArrayList containing the review data.
     */
    ReviewCardAdapter(Context context, ArrayList<ReviewCard> reviewData) {
        this.mReviewData = reviewData;
        this.mContext = context;
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
    public ReviewCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewCardAdapter.ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.review_card, parent, false));
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewCardAdapter.ViewHolder holder, int position) {
        // Get current sport.
        ReviewCard currentReview = mReviewData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentReview);
    }

    @Override
    public int getItemCount() {
        return mReviewData.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        // Member Variables for the Card
        private TextView mUsernameText;
        private TextView mContentText;

        ViewHolder(View itemView){
            super(itemView);

            // Initialize the views.
            mUsernameText = itemView.findViewById(R.id.reviewUsername);
            mContentText = itemView.findViewById(R.id.reviewContent);
        }

        /**
         *  to get the image resource from the Sport object and load it into the ImageView using Glide
         * @param currentReview
         */
        void bindTo(ReviewCard currentReview){
            // Populate the textviews with data.
            mUsernameText.setText(currentReview.getUsername());
            mContentText.setText(currentReview.getContent());
        }
    }
}
