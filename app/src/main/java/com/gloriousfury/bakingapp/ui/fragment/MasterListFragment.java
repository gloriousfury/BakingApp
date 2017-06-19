/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.gloriousfury.bakingapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.adapter.StepAdapter;
import com.gloriousfury.bakingapp.model.Ingredient;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.model.Step;
import com.gloriousfury.bakingapp.service.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// This fragment displays all of the AndroidMe images in one large list
// The list appears as a grid of images
public class MasterListFragment extends Fragment {

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnDescriptionClickListener mCallback;
    @BindView(R.id.steps_recycler_view)
    RecyclerView stepRecyclerView;
    @BindView(R.id.layout_ingredients)
    LinearLayout ingredientView;
    @BindView(R.id.txtview_ingredients)
    TextView ingredients;

    StepAdapter adapter;
    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Toast mCurrentToast;
    Recipe singleRecipe;
    ArrayList<Step> stepArrayList = new ArrayList<>();
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    String RECIPE_ITEM_KEY = "recipe_item";

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnDescriptionClickListener {
        void onDescriptionSelected(Bundle stepParameters, int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnDescriptionClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDescriptionClickListener");
        }
    }


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_masterlist, container, false);
        ButterKnife.bind(this, view);

        setUpViews();
        // Return the root view
        return view;
    }

    private void setUpViews() {

//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        Intent getRecipeData = getActivity().getIntent();
        if (getRecipeData != null) {

            singleRecipe = getRecipeData.getParcelableExtra(RECIPE_ITEM_KEY);
            stepArrayList = singleRecipe.getSteps();
            ingredientArrayList = singleRecipe.getIngredients();
            ingredients.setText(getIngredientsString(ingredientArrayList));

        }

//        getActivity().getSupportActionBar().setTitle(singleRecipe.getName());
//        getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //recycler setup
        stepRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stepRecyclerView.setLayoutManager(mlayoutManager);

        adapter = new StepAdapter(getActivity(), stepArrayList);
        stepRecyclerView.setAdapter(adapter);


    }


    String getIngredientsString(ArrayList<Ingredient> ingredients) {
        String ingredientlist ;
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < ingredients.size() - 1; i++) {
            String singleIngredient = ". " + ingredients.get(i).getIngredient() + "(" + ingredients.get(i).getQuantity() + " " +
                    ingredients.get(i).getMeasure() +")"+ "\n";
            sb.append(singleIngredient);
        }

        ingredientlist = sb.toString();

        return ingredientlist;
    }

    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }


}
