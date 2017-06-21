package com.gloriousfury.bakingapp.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.adapter.StepAdapter;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.model.Step;
import com.gloriousfury.bakingapp.ui.fragment.MasterListFragment;
import com.gloriousfury.bakingapp.ui.fragment.SingleStepFragment;
import com.gloriousfury.bakingapp.utils.RecipeContract;
import com.gloriousfury.bakingapp.utils.RecipeDatabaseHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleRecipeActivity extends AppCompatActivity implements MasterListFragment.OnDescriptionClickListener {


    @BindView(R.id.steps_recycler_view)
    RecyclerView stepRecyclerView;
    @BindView(R.id.layout_ingredients)
    LinearLayout ingredientView;
    private boolean mTwoPane;

    StepAdapter adapter;

    Recipe singleRecipe;
    ArrayList<Step> stepArrayList = new ArrayList<>();

    String RECIPE_ITEM_KEY = "recipe_item";
    String STEP_ITEM_KEY = "step_item";
    RecipeDatabaseHelper recipeDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        ButterKnife.bind(this);
        recipeDatabaseHelper = new RecipeDatabaseHelper(this);


//
//        getSupportActionBar().setTitle(singleRecipe.getName() +"Recipe");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViews(savedInstanceState);


    }

    private void setUpViews(Bundle savedInstanceState) {
        Intent getRecipeData = getIntent();
        singleRecipe = getRecipeData.getParcelableExtra(RECIPE_ITEM_KEY);
        stepArrayList = singleRecipe.getSteps();
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipesEntry.COLUMN_NAME, singleRecipe.getName());
        values.put(RecipeContract.RecipesEntry.COLUMN_INGREDIENT_LIST, singleRecipe.getIngredient_String());

        if (recipeIsPresent()) {
           recipeDatabaseHelper.updateRecipe(singleRecipe);
//            recipeDatabaseHelper.addLastViewedRecipe(singleRecipe);
        } else {
            recipeDatabaseHelper.addLastViewedRecipe(singleRecipe);
        }


//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (findViewById(R.id.single_step_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            String title = singleRecipe.getName();
            if(title!=null) {

                getSupportActionBar().setTitle(title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            //recycler setup
            stepRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            stepRecyclerView.setLayoutManager(mlayoutManager);

            adapter = new StepAdapter(this, stepArrayList);
            stepRecyclerView.setAdapter(adapter);

            if (savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
                SingleStepFragment singleStepFragment = new SingleStepFragment();
                singleStepFragment.setStepData(stepArrayList.get(0));
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


        if (mTwoPane) {

            SingleStepFragment newFragment = new SingleStepFragment();
            Step step = stepParameters.getParcelable(STEP_ITEM_KEY);
            newFragment.setStepData(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.single_step_container, newFragment)
                    .commit();

        } else {

            Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
            Intent stepActivity = new Intent(this, SingleStepActivity.class);
            stepActivity.putExtra("StepBundle", stepParameters);
            stepActivity.putExtra("StepPosition", position);
            stepActivity.putParcelableArrayListExtra("StepArrayList", stepArrayList);
            startActivity(stepActivity);


        }


    }

    private boolean recipeIsPresent() {
        int cursorSize = recipeDatabaseHelper.cursorSize(0);
        if (cursorSize == 0) {
            Toast.makeText(this, "Recipe is not Stored, Cursor Size" + String.valueOf(cursorSize), Toast.LENGTH_LONG).show();

            return false;

        } else {
            Toast.makeText(this, "Movie is Stored" + String.valueOf(cursorSize), Toast.LENGTH_LONG).show();
            return true;

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
