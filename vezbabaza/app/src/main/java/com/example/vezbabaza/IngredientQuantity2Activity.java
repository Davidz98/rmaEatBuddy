package com.example.vezbabaza;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class IngredientQuantity2Activity extends AppCompatActivity {

    IngredientQuantity2Activity that = this;
    Button viewRecipe;
    JSONArray chosenIngredients;
    LinearLayout ingredientsContainer;
    TextView nutValValue;
    TextView proteinValue;
    TextView chhyValue;
    TextView fatValue;
    TextView totalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ingredients_quantity2);

        nutValValue = findViewById(R.id.detailsValue);
        chhyValue = findViewById(R.id.details1Value);
        proteinValue = findViewById(R.id.details2Value);
        fatValue = findViewById(R.id.details3Value);
        totalValue = findViewById(R.id.totalMealValue);

        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        setupViewRecipeButton();

        try{
            chosenIngredients = new JSONArray(getIntent().getStringExtra("chosenIngredients"));
        }catch(JSONException e){
            Toast.makeText(that, "Ops, something isn't working. Pls reload.", Toast.LENGTH_SHORT).show();
        }

        createIngredientViews();
    }

    private void setupViewRecipeButton(){
        viewRecipe = findViewById(R.id.viewRecipeButton);
        viewRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // pravi intent za recipe stranici i prosledjuje podatke za recepat
                Intent intent = new Intent(that, RecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ingredients", chosenIngredients.toString());
                JSONObject mealData = new JSONObject();
                try{
                    mealData.put("nutval", nutValValue.getText());
                    mealData.put("protein", proteinValue.getText());
                    mealData.put("fat", fatValue.getText());
                    mealData.put("chhy", chhyValue.getText());
                    mealData.put("total", totalValue.getText());
                }catch(JSONException e){
                    System.out.println("Greska kod pakovanja za bundle.");
                }
                bundle.putString("mealData", mealData.toString());
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });
    }

    private void createIngredientViews(){
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < chosenIngredients.length(); i++){
            View v = layoutInflater.inflate(R.layout.ingredient_detail, ingredientsContainer, false);

            try{
                JSONObject ingredient = chosenIngredients.getJSONObject(i);
                ingredient.put("grams", 0);
                ImageView ingredientImage = v.findViewById(R.id.ingredientImage);
                TextView ingredientDetailName = v.findViewById(R.id.ingredientDetailName);
                SeekBar ingredientSetQuantity = v.findViewById(R.id.setIngredientQuantity);
                TextView ingredientQuantity = v.findViewById(R.id.ingredientQuantity);
                TextView ingredientNutVal = v.findViewById(R.id.nutvalValue);
                TextView ingredientChHy = v.findViewById(R.id.chhyValue);
                TextView ingredientProtein = v.findViewById(R.id.proteinValue);
                TextView ingredientFat = v.findViewById(R.id.fatValue);

                Picasso.get().load("http://192.168.0.25:8080/images/"+ingredient.getString("slika")).resize(87, 95).centerCrop().into(ingredientImage);
                ingredientDetailName.setText(ingredient.getString("naziv"));
                ingredientQuantity.setText("0g");
                ingredientQuantity.setTag(R.id.value, 0);
                ingredientNutVal.setText("0");
                ingredientNutVal.setTag(R.id.value, 0);
                ingredientChHy.setText("0g");
                ingredientChHy.setTag(R.id.value, 0);
                ingredientProtein.setText("0g");
                ingredientProtein.setTag(R.id.value, 0);
                ingredientFat.setText("0g");
                ingredientFat.setTag(R.id.value, 0);
                ingredientSetQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String ingredientQuantityNew = String.valueOf(progress) + "g";
                        ingredientQuantity.setText(ingredientQuantityNew);
                        ingredientQuantity.setTag(R.id.value, progress);
                        try{
                            ingredient.put("grams", progress);
                        } catch(JSONException e){
                            System.out.println("Greska prilikom dodavanja grama.");
                        }


                        int ingredientNutValNew;
                        int ingredientProteinNew;
                        int ingredientChHyNew;
                        int ingredientFatNew;
                        try{
                            ingredientNutValNew = progress/100*ingredient.getInt("kalorije");
                            ingredientProteinNew = progress/100*ingredient.getInt("proteini");
                            ingredientChHyNew = progress/100*ingredient.getInt("ugljeniHidrati");
                            ingredientFatNew = progress/100*ingredient.getInt("masti");

                            String resNutVal = String.valueOf(ingredientNutValNew);
                            String resProtein = String.valueOf(ingredientProteinNew)+"g";
                            String resChHy = String.valueOf(ingredientChHyNew)+"g";
                            String resFat = String.valueOf(ingredientFatNew)+"g";

                            ingredientNutVal.setText(resNutVal);
                            ingredientNutVal.setTag(R.id.value, ingredientNutValNew);
                            ingredientChHy.setText(resChHy);
                            ingredientChHy.setTag(R.id.value, ingredientChHyNew);
                            ingredientProtein.setText(resProtein);
                            ingredientProtein.setTag(R.id.value, ingredientProteinNew);
                            ingredientFat.setText(resFat);
                            ingredientFat.setTag(R.id.value, ingredientFatNew);

                            updateMealSummary();
                        }catch(JSONException e){
                            Toast.makeText(that, "Cat't parse data. Pls reload.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            } catch(JSONException e){
                Toast.makeText(that, "Cat't parse data. Pls reload.", Toast.LENGTH_SHORT).show();
            }

            ingredientsContainer.addView(v);
        }
    }

    public void updateMealSummary(){
        int totalNutVal = 0;
        int totalChHy = 0;
        int totalProtein = 0;
        int totalFat = 0;
        int total = 0;

        for (int i = 0; i < ingredientsContainer.getChildCount(); i++){
            View child = ingredientsContainer.getChildAt(i);
            System.out.println("Prva:" + Integer.parseInt(Objects.toString(child.findViewById(R.id.nutvalValue).getTag(R.id.value))));
            System.out.println("Druga:" + Integer.parseInt(Objects.toString(child.findViewById(R.id.chhyValue).getTag(R.id.value))));
            System.out.println("Treca:" + Integer.parseInt(Objects.toString(child.findViewById(R.id.proteinValue).getTag(R.id.value))));
            System.out.println("Cetvrta:" + Integer.parseInt(Objects.toString(child.findViewById(R.id.fatValue).getTag(R.id.value))));
            System.out.println("Peta:" + Integer.parseInt(Objects.toString(child.findViewById(R.id.ingredientQuantity).getTag(R.id.value))));
            totalNutVal += Integer.parseInt(Objects.toString(child.findViewById(R.id.nutvalValue).getTag(R.id.value)));
            totalChHy += Integer.parseInt(Objects.toString(child.findViewById(R.id.chhyValue).getTag(R.id.value)));
            totalProtein += Integer.parseInt(Objects.toString(child.findViewById(R.id.proteinValue).getTag(R.id.value)));
            totalFat += Integer.parseInt(Objects.toString(child.findViewById(R.id.fatValue).getTag(R.id.value)));
            total += Integer.parseInt(Objects.toString(child.findViewById(R.id.ingredientQuantity).getTag(R.id.value)));
        }

        String resNutValue = String.valueOf(totalNutVal);
        nutValValue.setText(resNutValue);
        String resChHyValue = String.valueOf(totalChHy) + "g";
        chhyValue.setText(resChHyValue);
        String resProteinValue = String.valueOf(totalProtein) + "g";
        proteinValue.setText(resProteinValue);
        String resFatValue = String.valueOf(totalFat) + "g";
        fatValue.setText(resFatValue);
        String resTotalValue = String.valueOf(total) + "g";
        totalValue.setText(resTotalValue);

    }
}
