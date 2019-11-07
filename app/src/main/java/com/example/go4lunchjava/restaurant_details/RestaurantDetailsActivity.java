package com.example.go4lunchjava.restaurant_details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.go4lunchjava.R;
import com.example.go4lunchjava.di.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String RESTAURANT_ID_KEY = "restaurant_id";
    private String mPlaceId;

    //UI
    private FloatingActionButton mFab;
    private ImageView mPictureView;
    private TextView mNameView;
    private ImageView mRatingView;
    private TextView mAddressView;
    private ImageView mPhoneImageView;
    private ImageView mFavoriteImageView;
    private ImageView mWebImageView;

    //DATA
    private String mPhoneNumber;
    private String mWebUrl;


    public static Intent newIntent(Context context, String restaurantId){
        Intent intent = new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtra(RESTAURANT_ID_KEY, restaurantId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        mFab = findViewById(R.id.fab_restaurant_details);
        mPictureView = findViewById(R.id.iv_restaurant_details);
        mNameView = findViewById(R.id.tv_name_restaurant_details);
        mRatingView = findViewById(R.id.iv_rating_details);
        mAddressView = findViewById(R.id.tv_address_restaurant_details);
        mPhoneImageView = findViewById(R.id.iv_phone_details);
        mFavoriteImageView = findViewById(R.id.iv_favorite_details);
        mWebImageView = findViewById(R.id.iv_web_details);

        mPlaceId = getIntent().getStringExtra(RESTAURANT_ID_KEY);

        ViewModelFactory factory = new ViewModelFactory(getApplication());
        RestaurantDetailsViewModel detailsViewModel = ViewModelProviders.of(this, factory).get(RestaurantDetailsViewModel.class);

        if (mPlaceId != null) detailsViewModel.launchDetailsRequest(mPlaceId); //Provide the id to our ViewModel

        detailsViewModel.mDetailsLiveData.observe(this, this::updateUi);
    }

    private void updateUi(RestaurantDetails restaurant){

        if (!restaurant.getPictureUri().isEmpty()) {
            Glide.with(this)
                    .load(restaurant.getPictureUri())
                    .centerCrop()
                    .into(mPictureView);
        }

        mNameView.setText(restaurant.getName());
        mAddressView.setText(restaurant.getAddress());
        mRatingView.setImageResource(restaurant.getRatingResource());
        mPhoneNumber = restaurant.getPhoneNumber();
        mWebUrl = restaurant.getWebSiteUrl();


        mPhoneImageView.setOnClickListener(iconClickListener);
        mFavoriteImageView.setOnClickListener(iconClickListener);
        mWebImageView.setOnClickListener(iconClickListener);

    }

    private View.OnClickListener iconClickListener = view -> {
        switch (view.getId()){
            case R.id.iv_phone_details:
                if (mPhoneNumber != null)
                    dialPhoneNumber(mPhoneNumber);
                else
                    Toast.makeText(this, "No phone number communicated.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_favorite_details:
                Toast.makeText(this, "Like !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_web_details:
                if (mWebUrl != null)
                    openWebPage(mWebUrl);
                else
                    Toast.makeText(this, "No website communicated.", Toast.LENGTH_SHORT).show();
                break;
        }
    };

    private void dialPhoneNumber(String phoneNUmber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNUmber));
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void openWebPage(String url){
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
