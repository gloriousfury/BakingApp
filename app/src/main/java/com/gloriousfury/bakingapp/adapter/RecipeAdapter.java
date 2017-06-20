package com.gloriousfury.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.model.Ingredient;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.ui.activity.SingleRecipeActivity;
import com.gloriousfury.bakingapp.ui.activity.SingleStepActivity;
import com.squareup.picasso.Picasso;

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
        public final TextView recipeName, recipeServings;
        ImageView recipeImage;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            recipeName = (TextView) view.findViewById(R.id.recipe_name);
            recipeServings = (TextView) view.findViewById(R.id.recipe_servings_quantity);
            recipeImage = (ImageView) view.findViewById(R.id.img_recipe);

        }

        @Override
        public void onClick(View view) {
            Recipe recipe = recipe_list.get(getAdapterPosition());

            Intent intent = new Intent(context, SingleRecipeActivity.class);
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
        String servings = String.valueOf(recipe_list.get(position).getServings());
        String img_url = recipe_list.get(position).getImage();

         String ingredients = getIngredientsString(recipe_list.get(position).getIngredients());
        recipe_list.get(position).setIngredient_String(ingredients);
        holder.recipeName.setText(name);
        holder.recipeServings.setText(servings+ " Servings");

        if ((!TextUtils.isEmpty(img_url))){
        Picasso.with(context).load(img_url).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {

        if (recipe_list.size() == 0) {

            return 0;
        } else {
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


}