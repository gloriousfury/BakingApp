package com.gloriousfury.bakingapp.service;


import com.gloriousfury.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


    @GET
    Call<ArrayList<Recipe>> getRecipe(@Url String url);


}