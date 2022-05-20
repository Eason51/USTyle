package com.example.ustyle;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustyle.data.Global;
import com.example.ustyle.data.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Card> mCardData;
    private CardAdapter mAdapter;
    private Context mContext;
    private int mTabPosition;

    public TabFragment(int tabPosition) {
        // Required empty public constructor
        this.mTabPosition = tabPosition;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mContext = getContext();
        // Initialize the RecyclerView.
        mRecyclerView = view.findViewById(R.id.recyclerViewTab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        // Initialize the ArrayList that will contain the data.
        mCardData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new CardAdapter(mContext, mCardData, mTabPosition);
//        Log.d("tabPosition: ", Integer.toString(mTabPosition));
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();
    }

    /**
     * Initialize the Card data from resources.
     */
    private void initializeData() {

        String facilityType = "";
        if(mTabPosition == 0)
            facilityType = "freshman";
        else if(mTabPosition == 1)
            facilityType = "dining";
        else if(mTabPosition == 2)
            facilityType = "hall";
        else if(mTabPosition == 3)
            facilityType = "workout";


        String requestBaseURL = Global.BaseURL.queryBaseURL;
        String queryString = String.format("db.collection(\"facilities\")" +
                ".where({type: \"%s\"})" +
                ".limit(100).get()"
                , facilityType);


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
                                //mCardData.add(new Card(cardList[i], "Info", "Description", cardImageResources.getResourceId(i, 0)));

                                Card card = new Card(dataJSON.getString("name"), dataJSON.getString("shortDescription"),
                                        dataJSON.getString("description"), dataJSON.getString("imageUrl"));
                                mCardData.add(card);


                                // Notify the adapter of the change.
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                Global.context
        ).execute(queryString, requestBaseURL);

    }


//    private void initializeData2() {
//
//
//        // Get the resources from the XML file.
//        String[] cardList = getResources()
//                .getStringArray(R.array.card_titles);
//        TypedArray cardImageResources =
//                getResources().obtainTypedArray(R.array.card_images);
//
//
//
//        // Clear the existing data (to avoid duplication).
//        mCardData.clear();
//
//        // Create the ArrayList of Card objects with titles and
//        // image about each facility.
//        for (int i = 0; i < cardList.length; i++) {
//            mCardData.add(new Card(cardList[i], "Info", "Description", cardImageResources.getResourceId(i, 0)));
//        }
//
//
//        cardImageResources.recycle();
//        // Notify the adapter of the change.
//        mAdapter.notifyDataSetChanged();
//    }
}