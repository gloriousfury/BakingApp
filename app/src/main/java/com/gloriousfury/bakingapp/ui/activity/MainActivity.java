package com.gloriousfury.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.gloriousfury.bakingapp.adapter.RecipeAdapterTablet;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.service.ApiInterface;
import com.gloriousfury.bakingapp.utils.AutofitRecyclerView;
import com.gloriousfury.bakingapp.utils.RecyclerInsetsDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recipeRecyclerView;
    RecipeAdapter adapter;
    RecipeAdapterTablet tablet_adapter;
    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Toast mCurrentToast;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    private static final String LIFECYCLE_RECIPE_CALLBACKS_KEY = "recipesList";
    private boolean tabletlayout= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinatorlayout);
        setUpViews();
        if (savedInstanceState == null || !savedInstanceState.containsKey(LIFECYCLE_RECIPE_CALLBACKS_KEY)){

            getRecipeList();
        }else{
            recipeArrayList = savedInstanceState.getParcelableArrayList(LIFECYCLE_RECIPE_CALLBACKS_KEY);
            adapter.setRecipeData(recipeArrayList);
        }



        setUpTabletViews();


    }

    private void setUpTabletViews() {


      tablet_adapter  = new RecipeAdapterTablet(this, recipeArrayList);
        recipeRecyclerView.addItemDecoration(new RecyclerInsetsDecoration(this));
        recipeRecyclerView.setAdapter(tablet_adapter);

    }

    private void setUpViews() {

//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recipeRecyclerView = (AutofitRecyclerView) findViewById(R.id.recipe_recycler);
        //recycler setup


        if(findViewById(R.id.tablet_layout_identifier)!=null){
            tabletlayout = true;
            tablet_adapter = new RecipeAdapterTablet(this, recipeArrayList);
            recipeRecyclerView.setAdapter(adapter);

        }else {
            recipeRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recipeRecyclerView.setLayoutManager(mlayoutManager);

            adapter = new RecipeAdapter(this, recipeArrayList);
            recipeRecyclerView.setAdapter(adapter);
        }


    }

    // method to get initial list of movies using the retrofit lib.
    public void getRecipeList() {
        initiateGetRecipeDataView();
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
                    if(tabletlayout){
                        tabletlayout = true;
                        tablet_adapter.setRecipeData(recipeArrayList);

                    }else {
                        tabletlayout = false;
                        adapter.setRecipeData(recipeArrayList);
                    }



                    Log.d("MainActivity", "loaded from API");

                    showRecipeDataView();
                } else {
//                    showErrorMessage();
                    showErrorMessage();
                    showToast("I didn't got the data,still Yay!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                showErrorMessage();
                Log.d("TaiyeActivity", t.toString());
            }
        });
    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LIFECYCLE_RECIPE_CALLBACKS_KEY, recipeArrayList);
        super.onSaveInstanceState(outState);
    }










    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }


    private void showRecipeDataView() {
        /* First, make sure the error is invisible */

        progressBar.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recipeRecyclerView.setVisibility(View.VISIBLE);
    }


    private void initiateGetRecipeDataView() {
        /* First, make sure the error is invisible */

        progressBar.setVisibility(View.VISIBLE);
        /* Then, make sure the weather data is visible */
        recipeRecyclerView.setVisibility(View.INVISIBLE);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        progressBar.setVisibility(View.INVISIBLE);

        /* Then, show the error */
        snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecipeList();
            }
        });
        snackbar.show();
    }


}
