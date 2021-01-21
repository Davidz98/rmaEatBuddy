package com.example.vezbabaza;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewModelIngredients extends ViewModel {
    ViewModelIngredients that = this;
    public static final String MEAT = "meat";
    public static final String FRUIT = "fruit";
    public static final String VEGETABLE = "vegetable";

    //mutable live data se moze promeniti, live data ne moze
    private MutableLiveData<JSONArray> meatIngredients;
    private MutableLiveData<JSONArray> fruitIngredients;
    private MutableLiveData<JSONArray> vegetableIngredients;
    private JSONArray chosenIngredients = new JSONArray();
    private MutableLiveData<String> selectedData = new MutableLiveData<>();


    public LiveData<JSONArray> getData(String ingredientType, Activity context){
        if (ingredientType == "meat"){
            if (meatIngredients == null){
                meatIngredients = new MutableLiveData<>();
                fetchData("meat", context);
            }
            return meatIngredients;
        } else if (ingredientType == "fruit"){
            if (fruitIngredients == null){
                fruitIngredients = new MutableLiveData<>();
                fetchData("fruit", context);
            }
            return fruitIngredients;
        } else {
            if (vegetableIngredients == null){
                vegetableIngredients = new MutableLiveData<>();
                fetchData("vegetable", context);
            }
            return vegetableIngredients;
        }
    }

    // postavlja select data
    public void setSelectData(String item){
        this.selectedData.setValue(item);
    }
    // vraca liveData (koje je observable) da ga mogu "gledati" i na promene menjati (tipa ako klikne povrce i postavi se za selectedData povrce i onda se fragment za povrce pokaze)
    public LiveData<String> getSelected(){
        return this.selectedData;
    }

    public void fetchData(String ingredientType, Activity context){
        // dobavljamo podatke preko API-a ako treba
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.25:8080/ingredients/" + ingredientType;

        JsonArrayRequest getIngredients = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // dobijas response koji je
                switch (ingredientType) {
                    case MEAT:
                        meatIngredients.setValue(response);
                        break;
                    case FRUIT:
                        fruitIngredients.setValue(response);
                        break;
                    case VEGETABLE:
                        vegetableIngredients.setValue(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                System.out.println("Error while fetching data.");
            }
        });

        queue.add(getIngredients);
    }

    public void addToChosenIngredients(JSONObject ingredient){
        chosenIngredients.put(ingredient);
    }

    public void removeFromChosenIngredients(String ingredientName){
        try{
            for (int i = 0; i < chosenIngredients.length(); i++){
                if (chosenIngredients.getJSONObject(i).getString("naziv") == ingredientName){
                    chosenIngredients.remove(i);
                }
            }
        } catch(JSONException e){
            System.out.println("Greska prilikom vracanja JSONObject-a" + e);
        }
    }

    public boolean checkIfChosen(String ingredientName){
        boolean check = false;
        try{
            for (int i = 0; i < chosenIngredients.length(); i++){
                if (chosenIngredients.getJSONObject(i).getString("naziv") == ingredientName){
                    check = true;
                    break;
                }
            }
        } catch(JSONException e){
            System.out.println("Greska prilikom vracanja JSONObject-a" + e);
        }
        return check;
    }

    public boolean isAnyChosen(){
        return chosenIngredients.length() > 0;
    }

    public JSONArray getChosenIngredients(){
        return chosenIngredients;
    }

}
