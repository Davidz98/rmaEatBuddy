package com.example.vezbabaza;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeActivity extends AppCompatActivity {

    RecipeActivity that = this;
    JSONArray ingredients;
    JSONObject mealData;
    Button goToStart;
    LinearLayout recipesLayout;
    Boolean fromCreate;
    TextView recipeNutVal;
    TextView recipeChHy;
    TextView recipeProtein;
    TextView recipeFat;
    TextView recipeTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipesLayout = findViewById(R.id.recipesLayout);
        recipeNutVal = findViewById(R.id.recipeNutritiveValueValue);
        recipeChHy = findViewById(R.id.recipeCarbohydratesValueText);
        recipeProtein = findViewById(R.id.recipeProteinValueText);
        recipeFat = findViewById(R.id.recipeFatValueText);
        recipeTotal = findViewById(R.id.recipeTotalValueText);


        try{
            ingredients = new JSONArray(getIntent().getBundleExtra("bundle").getString("ingredients"));
            mealData = new JSONObject(getIntent().getBundleExtra("bundle").getString("mealData"));
        }catch(JSONException e){
            Toast.makeText(this, "Can't parse data, pls reload.", Toast.LENGTH_SHORT).show();
        }

        setupGoToStart();
        createViews();
        fillMealData();
    }

    private void createViews(){
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < ingredients.length(); i++){
            View v = layoutInflater.inflate(R.layout.ingredient_in_recipe_look, recipesLayout, false);
            ImageView ingredientImage = v.findViewById(R.id.imageView);
            TextView ingredientName = v.findViewById(R.id.ingredientRecipeName);
            TextView ingredientGrams = v.findViewById(R.id.ingredientRecipeQuantity);

            try{
                JSONObject ingredient = ingredients.getJSONObject(i);
                Picasso.get().load("http://192.168.0.25:8080/images/"+ingredient.getString("slika")).resize(87, 95).centerCrop().into(ingredientImage);
                ingredientName.setText(ingredient.getString("naziv"));
                String ingredientG = String.valueOf(ingredient.getInt("grams"))+"g";
                ingredientGrams.setText(ingredientG);
            } catch(JSONException e){
                System.out.println("Greska kod RecipeActivity.");
            }
            recipesLayout.addView(v);
        }

    }

    private void setupGoToStart(){
        goToStart = findViewById(R.id.goToStartButton);
        goToStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(that, ChooseIngredientsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillMealData(){
        try{
            recipeNutVal.setText(mealData.getString("nutval"));
            recipeProtein.setText(mealData.getString("protein"));
            recipeChHy.setText(mealData.getString("chhy"));
            recipeFat.setText(mealData.getString("fat"));
            recipeTotal.setText(mealData.getString("total"));
        } catch(JSONException e){
            System.out.println("Greska prilikom citanja iz mealData.");
        }

    }
}
