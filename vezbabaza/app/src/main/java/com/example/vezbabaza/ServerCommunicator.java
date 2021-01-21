package com.example.vezbabaza;

import android.content.Intent;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ServerCommunicator {
    public ServerCommunicator(){};
    public void postRequestLogIn(MainActivity context, JSONObject userData, TextView authMessage){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.25:8080/auth/login";

        JsonObjectRequest post = new JsonObjectRequest(url, userData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ide intent koji prebacuje na biranje namirnica
                context.saveData();
                Intent intent = new Intent(context, ChooseIngredientsActivity.class);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                authMessage.setText("Username or password wrong, pls try again.");
            }
        });
        queue.add(post);
    }

    public void postRequestSignUp(MainActivity context, JSONObject userData, TextView authMessage){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.25:8080/auth/signup";

        JsonObjectRequest post = new JsonObjectRequest(url, userData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // intent koji salje na biranje namirnica
                Intent intent = new Intent(context, ChooseIngredientsActivity.class);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                authMessage.setText("Unsuccessful sign up, pls try again.");
            }
        });

        queue.add(post);
    }

}
