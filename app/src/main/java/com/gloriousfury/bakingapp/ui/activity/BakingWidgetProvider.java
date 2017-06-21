package com.gloriousfury.bakingapp.ui.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.model.Recipe;
import com.gloriousfury.bakingapp.utils.RecipeDatabaseHelper;



public class BakingWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        RecipeDatabaseHelper db = new RecipeDatabaseHelper(context);

        Recipe recipe = db.getLastViewedRecipe(0);




        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            String recipeName;
            String recipeIngredient;
            if(recipe!=null) {
             recipeName =   recipe.getName();
              recipeIngredient=   recipe.getIngredient_String();
            }else{
                recipeName = context.getString(R.string.recipe_error_name);
                recipeIngredient = context.getString(R.string.recipe_error_message);

            }


            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.recipe_ingredients, recipeIngredient);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}