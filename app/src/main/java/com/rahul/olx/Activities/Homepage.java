package com.rahul.olx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rahul.olx.AllInOneResponseClasses.AllInOneResponseClasses;
import com.rahul.olx.AllInOneResponseClasses.DataClasses;
import com.rahul.olx.ApiCallInterfaces.AllInOneApiClient;
import com.rahul.olx.ClickListeners.AllInOneClickListener;
import com.rahul.olx.ClickListeners.BrowseCategoryClickListener;
import com.rahul.olx.ModelClasses.BrowseCategoriesModelClass;
import com.rahul.olx.Networks.Network;
import com.rahul.olx.R;
import com.rahul.olx.ViewAdatpers.AllInOneViewAdapter;
import com.rahul.olx.ViewAdatpers.BrowseCategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends AppCompatActivity implements BrowseCategoryClickListener, AllInOneClickListener {

    private ArrayList<BrowseCategoriesModelClass> modelClassArrayList = new ArrayList<>();
    private RecyclerView rvBrowseCategories, rvForAll;
    private List<DataClasses> dataClassesArrayList = new ArrayList<>();
    private AllInOneViewAdapter adapter;
    private ProgressBar pb;
    private ImageView ivHome, ivChat, ivSell, ivHeart, ivAccount;
    private EditText etSearch;
    private Button btnSearch;
    private TextView tvSeeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        buildBrowseCategory();
        initViewsAndListeners();
        getApiInsideRecyclerView();
        setAdapter();
    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvForAll.setLayoutManager(gridLayoutManager);
        adapter = new AllInOneViewAdapter(dataClassesArrayList, this);
        rvForAll.setAdapter(adapter);
    }

    private void getApiInsideRecyclerView() {
        AllInOneApiClient apiClient = Network.getInstance().create(AllInOneApiClient.class);
        apiClient.fetchApi().enqueue(new Callback<AllInOneResponseClasses>() {
            @Override
            public void onResponse(Call<AllInOneResponseClasses> call, Response<AllInOneResponseClasses> response) {
                if (response.body() != null) {
                    dataClassesArrayList = response.body().getData();
                    adapter.updateDataFromApi(dataClassesArrayList);
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllInOneResponseClasses> call, Throwable t) {

            }
        });
    }

    private void buildBrowseCategory() {
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_car_jade, "OLX Autos (CARS)"));
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_house_jade, "PROPERTIES"));
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_smartphone_jade, "MOBILES"));
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_jobs, "JOBS"));
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_motorbike_jade, "BIKES"));
        modelClassArrayList.add(new BrowseCategoriesModelClass(R.drawable.ic_desktop_jade, "ELECTRONICS"));
    }

    private void initViewsAndListeners() {
        rvBrowseCategories = findViewById(R.id.rvBrowseCategories);
        rvForAll = findViewById(R.id.RVForAll);
        pb = findViewById(R.id.ProgressBar);
        ivHome = findViewById(R.id.ibRefresh);
        ivChat = findViewById(R.id.ibLocation);
        ivSell = findViewById(R.id.ibHeart);
        ivHeart = findViewById(R.id.ibChat);
        ivAccount = findViewById(R.id.ibUser);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvSeeAll = findViewById(R.id.tvSeeAll_home);
        BrowseCategoriesAdapter adapter = new BrowseCategoriesAdapter(modelClassArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvBrowseCategories.setLayoutManager(linearLayoutManager);
        rvBrowseCategories.setAdapter(adapter);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocation()) {
                    btnSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = etSearch.getText().toString();
                Intent intent = new Intent(Homepage.this, LocationSearchActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Categories.class);
                startActivity(intent);
            }
        });


        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Homepage.class);
                startActivity(intent);
            }
        });

        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        ivSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, my_offers_menu.class);
                startActivity(intent);
            }
        });

        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, MyAdsActivity.class);
                startActivity(intent);
            }
        });

        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, AccountPageActivity.class);
                startActivity(intent);
            }
        });
    }


    private boolean checkLocation() {
        boolean condition = true;
        if (!etSearch.getText().toString().contains("Bangalore") && !etSearch.getText().toString().contains("bangalore") &&
                !etSearch.getText().toString().contains("Mumbai") && !etSearch.getText().toString().contains("mumbai") &&
                !etSearch.getText().toString().contains("Delhi") && !etSearch.getText().toString().contains("delhi") &&
                !etSearch.getText().toString().contains("Kolkata") && !etSearch.getText().toString().contains("kolkata")) {
            Toasty.error(Homepage.this, "Services currently available only in select locations", Toast.LENGTH_LONG).show();
            condition = false;
        }
        return condition;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, BrowseCategoryDisplayer.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onItemClick(DataClasses dataClasses) {
        try {
            Intent intent = new Intent(Homepage.this, ItemDetails.class);
            intent.putExtra("price", dataClasses.getPrice().getValue().getDisplay());
            intent.putExtra("title", dataClasses.getTitle());
            intent.putExtra("extras", dataClasses.getMainInfo());
            intent.putExtra("image1", dataClasses.getImages().get(0).getUrl());
            intent.putExtra("image2", dataClasses.getImages().get(1).getUrl());
            intent.putExtra("town", dataClasses.getLocationsResolved().getADMINLEVEL3Name());
            intent.putExtra("city", dataClasses.getLocationsResolved().getADMINLEVEL1Name());
            intent.putExtra("description", dataClasses.getDescription());
            startActivity(intent);
        } catch (Exception e) {
            Toasty.info(Homepage.this, "Failed to fetch results, try again!", Toast.LENGTH_LONG).show();
        }

    }
}