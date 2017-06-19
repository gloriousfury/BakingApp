package com.gloriousfury.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.ui.activity.SingleRecipeActivity;
import com.gloriousfury.bakingapp.ui.activity.SingleStepActivity;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    Context context;
    private List<Recipe> recipe_list;
    String RECIPE_ITEM_KEY = "recipe_item";



    public RecipeAdapter(Context context,
                         List<Recipe> RecipeList
                         ) {
        this.context = context;
        this.recipe_list = RecipeList;


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView recipeName;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            recipeName = (TextView) view.findViewById(R.id.recipe_name);


        }

        @Override
        public void onClick(View view) {
            Recipe recipe = recipe_list.get(getAdapterPosition());

            Intent intent = new Intent(context,SingleRecipeActivity.class);
            intent.putExtra(RECIPE_ITEM_KEY, recipe);
            context.startActivity(intent);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String name = recipe_list.get(position).getName();

        holder.recipeName.setText(name);


//        Picasso.with(context).load(poster_img_path).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {

        if(recipe_list.size()==0){

            return 0;
        }else {
            return recipe_list.size();
        }
    }


    public void setRecipeData(List<Recipe> recipeData) {
        recipe_list = recipeData;
        notifyDataSetChanged();
    }

    public void appendRecipe(ArrayList<Recipe> recipeList) {
        recipe_list.addAll(recipeList);
        notifyDataSetChanged();
    }

}