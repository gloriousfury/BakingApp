package com.gloriousfury.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.adapter.RecipeAdapter;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.service.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recipeRecyclerView;
    RecipeAdapter adapter;
    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Toast mCurrentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpViews();





    }

    private void setUpViews() {

//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler);
        //recycler setup
        recipeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recipeRecyclerView.setLayoutManager(mlayoutManager);
      ;
        adapter = new RecipeAdapter(this, recipeArrayList);
        recipeRecyclerView.setAdapter(adapter);
        getRecipeList();
        showToast("I am here now");


    }

    // method to get initial list of movies using the retrofit lib.
    public void getRecipeList() {
       
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);
        request.getRecipe("baking.json").enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                if (response.isSuccessful()) {


                    recipeArrayList = response.body();
                    adapter.setRecipeData(recipeArrayList);

                    Log.d("MainActivity", "loaded from API");

                    showToast("I got the data, Yay!");
                } else {
//                    showErrorMessage();

                    showToast("I didn't got the data,still Yay!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
//                showErrorMessage();
                Log.d("TaiyeActivity", t.toString());
                showToast("This is the failue method, I didn't got the data,still Yay!");
            }
        });
    }

    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }


}
