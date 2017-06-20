package com.gloriousfury.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gloriousfury.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Created by OLORIAKE KEHINDE on 4/29/2017.
 */

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = RecipeDatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "recipes_database";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE "
            + RecipeContract.RecipesEntry.TABLE_NAME + "("
            + RecipeContract.RecipesEntry._ID + " INTEGER PRIMARY KEY,"
            + RecipeContract.RecipesEntry.COLUMN_NAME + " TEXT,"
            + RecipeContract.RecipesEntry.COLUMN_INGREDIENT_LIST + " TEXT"

             + ")";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipesEntry.TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    // ------------------------ "recipes" table methods ----------------//

    /**
     * Creating a recipe
     */
    public long addLastViewedRecipe(Recipe recipe) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipesEntry.COLUMN_NAME, recipe.getName());
        values.put(RecipeContract.RecipesEntry.COLUMN_INGREDIENT_LIST, recipe.getIngredient_String());

        // insert row
        long recipe_id = db.insert(RecipeContract.RecipesEntry.TABLE_NAME, null, values);
        return recipe_id;
    }


    public long updateRecipe(Recipe recipe) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipesEntry.COLUMN_NAME, recipe.getName());
        values.put(RecipeContract.RecipesEntry.COLUMN_INGREDIENT_LIST, recipe.getIngredient_String());

        String whereClause = RecipeContract.RecipesEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(0)};
       long updateId = db.update(
                RecipeContract.RecipesEntry.TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );
        db.close();
        return  updateId;
    }

    public Recipe getLastViewedRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = RecipeContract.RecipesEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                RecipeContract.RecipesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Recipe recipe = new Recipe();

        if (cursor.moveToFirst()) {
            do {

                recipe.setName(cursor.getString((cursor.getColumnIndex(RecipeContract.RecipesEntry.COLUMN_NAME))));
                recipe.setIngredient_String((cursor.getString(cursor.getColumnIndex(RecipeContract.RecipesEntry.COLUMN_INGREDIENT_LIST))));

            } while (cursor.moveToNext());


        }
        return recipe;
    }




    public int cursorSize(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = RecipeContract.RecipesEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                RecipeContract.RecipesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
         int cursor_size = cursor.getCount();
        cursor.close();

        return cursor_size;
    }



}
