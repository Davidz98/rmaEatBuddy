package com.example.vezbabaza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;

public class ChooseIngredientsActivity extends AppCompatActivity {

    ChooseIngredientsActivity that = this;
    TextView showMeatIngredients;
    TextView showFruitIngredients;
    TextView showVegetableIngredients;
    Button nextButton;
    ViewModelIngredients viewModelIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ingredients);

         viewModelIngredients = new ViewModelProvider(this).get(ViewModelIngredients.class);

        //postavljanje fragmenata
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
        IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance();
        ft.add(R.id.frameForFragments, ingredientsFragment, null);
        ft.commit();

        setupNextButton();
        setupShowMeatIngredients();
        setupShowFruitIngredients();
        setupShowVegetableIngredients();
    }

    private void setupShowMeatIngredients(){
        showMeatIngredients = findViewById(R.id.meatGroup);
        // postavljam boju teksta kao da je vec kliknut posto pocetno pokazujem kao da jeste
//        showMeatIngredients.setTextColor(getResources().getColor(R.color.clickedWhite));
        showMeatIngredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewModelIngredients.setSelectData(viewModelIngredients.MEAT);
                setClicksColor();
                showMeatIngredients.setTextColor(getResources().getColor(R.color.clickedWhite));
            }
        });
    }

    private void setupShowFruitIngredients(){
        showFruitIngredients = findViewById(R.id.fruitsGroup);
        showFruitIngredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewModelIngredients.setSelectData(viewModelIngredients.FRUIT);
                setClicksColor();
                showFruitIngredients.setTextColor(getResources().getColor(R.color.clickedWhite));
            }
        });
    }

    private void setupShowVegetableIngredients(){
        showVegetableIngredients = findViewById(R.id.vegetablesGroup);
        showVegetableIngredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewModelIngredients.setSelectData(viewModelIngredients.VEGETABLE);
                setClicksColor();
                showVegetableIngredients.setTextColor(getResources().getColor(R.color.clickedWhite));
            }
        });
    }

    private void setClicksColor(){
        showMeatIngredients.setTextColor(getResources().getColor(R.color.white));
        showFruitIngredients.setTextColor(getResources().getColor(R.color.white));
        showVegetableIngredients.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupNextButton(){
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (viewModelIngredients.isAnyChosen()){
                    Intent intent = new Intent(that, IngredientQuantity2Activity.class);
                    JSONArray chosenIngredients = viewModelIngredients.getChosenIngredients();
                    intent.putExtra("chosenIngredients", chosenIngredients.toString());
                    that.startActivity(intent);
                } else {
                    Toast.makeText(that, "Choose at least one ingredient.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
