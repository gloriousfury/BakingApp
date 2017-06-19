package com.gloriousfury.bakingapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.adapter.RecipeAdapter;
import com.gloriousfury.bakingapp.adapter.StepAdapter;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.model.Step;
import com.gloriousfury.bakingapp.service.ApiInterface;
import com.gloriousfury.bakingapp.ui.fragment.MasterListFragment;
import com.gloriousfury.bakingapp.ui.fragment.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleRecipeActivity extends AppCompatActivity implements MasterListFragment.OnDescriptionClickListener {



    @BindView(R.id.steps_recycler_view)
    RecyclerView stepRecyclerView;
    @BindView(R.id.layout_ingredients)
    LinearLayout ingredientView;
    private boolean mTwoPane;
    
    StepAdapter adapter;
    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Toast mCurrentToast;
    Recipe singleRecipe;
    ArrayList<Step> stepArrayList = new ArrayList<>();

    String RECIPE_ITEM_KEY = "recipe_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        ButterKnife.bind(this);




//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        setUpViews(savedInstanceState);





    }

    private void setUpViews(Bundle savedInstanceState) {
        Intent getRecipeData = getIntent();
        singleRecipe = getRecipeData.getParcelableExtra(RECIPE_ITEM_KEY);
        stepArrayList = singleRecipe.getSteps();
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if(findViewById(R.id.single_step_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            getSupportActionBar().setTitle(singleRecipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //recycler setup
            stepRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            stepRecyclerView.setLayoutManager(mlayoutManager);

            adapter = new StepAdapter(this, stepArrayList);
            stepRecyclerView.setAdapter(adapter);

                 if(savedInstanceState == null) {
            // In two-pane mode, add initial BodyPartFragments to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Creating a new head fragment
            VideoFragment singleStepFragment = new VideoFragment();
//            headFragment.setImageIds(AndroidImageAssets.getHeads());
            // Add the fragment to its container using a transaction
            fragmentManager.beginTransaction()
                    .add(R.id.single_step_container, singleStepFragment)
                    .commit();

        }
    } else {
        // We're in single-pane mode and displaying fragments on a phone in separate activities
        mTwoPane = false;
    }

    }


    @Override
    public void onDescriptionSelected(Bundle stepParameters, int position) {
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
        Intent stepActivity = new Intent(this,SingleStepActivity.class);
        stepActivity.putExtra("StepBundle",stepParameters);
        startActivity(stepActivity);

    }
}
