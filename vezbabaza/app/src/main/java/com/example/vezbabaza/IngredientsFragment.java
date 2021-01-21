package com.example.vezbabaza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.GridLayout;
import androidx.gridlayout.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {

    ViewModelIngredients viewModelIngredients;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    //Factory za kreiranje nove instance fragmenta
    public static IngredientsFragment newInstance() {
        IngredientsFragment fragment = new IngredientsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("usao.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_ingredients, container, false);
        GridLayout grid = v.findViewById(R.id.ingredientsGridLayout);

        viewModelIngredients = new ViewModelProvider(getActivity()).get(ViewModelIngredients.class);

        viewModelIngredients.getData("vegetable", getActivity()).observe(getViewLifecycleOwner(), data->{
            changeGridIngredients(grid, data);
        });

        viewModelIngredients.getData("fruit", getActivity()).observe(getViewLifecycleOwner(), data->{
            changeGridIngredients(grid, data);
        });

        viewModelIngredients.getData("meat", getActivity()).observe(getActivity(), data->{
            changeGridIngredients(grid, data);
        });


        viewModelIngredients.getSelected().observe(getViewLifecycleOwner(), ingredientType->{
            changeGridIngredients(grid, viewModelIngredients.getData(ingredientType, getActivity()).getValue());
        });

        return v;
    }

    private void changeGridIngredients(ViewGroup container, JSONArray data){
        System.out.println("U fillView.");

        container.removeAllViews();
//        System.out.println(data);
        LayoutInflater layoutInflater = getLayoutInflater();
        int i;
        for (i = 0; i < data.length(); i++){
//            System.out.println(data.length());
            View ingredientView = layoutInflater.inflate(R.layout.ingredient_look, container, false);

            TextView ingredientName = ingredientView.findViewById(R.id.ingredientName);
            ImageView ingredientImage = ingredientView.findViewById(R.id.ingredientPicture);
            JSONObject ingredient;
            try{
                ingredient = data.getJSONObject(i);
                ingredientName.setText(ingredient.getString("naziv"));
                Picasso.get().load("http://192.168.0.25:8080/images/"+ingredient.getString("slika")).resize(100, 100).centerCrop().into(ingredientImage);

                ingredientView.setTag(R.id.ingredientObject, ingredient);
                if (viewModelIngredients.checkIfChosen(ingredient.getString("naziv"))){
                    ingredientView.setTag(R.id.isSelected, "chosen");
                    ingredientView.findViewById(R.id.ingredientView).setBackgroundColor(getResources().getColor(R.color.ingredientSelected));
                } else {
                    ingredientView.setTag(R.id.isSelected, "not chosen");
                }

                ingredientName.setTag(ingredient.getString("naziv"));
            } catch(JSONException e){
                // da prekine program
                throw new Error("Greska prilikom pristupanja JSONObjektu" + e);
            }
            ingredientView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if ((String)v.getTag(R.id.isSelected) == "not chosen"){
                        viewModelIngredients.addToChosenIngredients((JSONObject)v.getTag(R.id.ingredientObject));
                        // treba da promeni boji ili nesto
                        v.findViewById(R.id.ingredientView).setBackgroundColor(getResources().getColor(R.color.ingredientSelected));
                        v.setTag(R.id.isSelected,"chosen");
                    } else {
                        viewModelIngredients.removeFromChosenIngredients((String)v.findViewById(R.id.ingredientName).getTag());
                        v.findViewById(R.id.ingredientView).setBackgroundColor(getResources().getColor(R.color.ingredientUnselected));
                        v.setTag(R.id.isSelected, "not chosen");
                    }
                }
            });

            container.addView(ingredientView);
        }
    }

}
