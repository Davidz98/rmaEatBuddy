package com.example.vezbabaza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

// Starting loading activity
public class MainActivity extends AppCompatActivity {

    private final static String APP_DATA_PREFIX = "MainActivityAppDataPrefix";
    private final static String APP_DATA_USERNAME = "username";
    private final static String APP_DATA_PASSWORD = "password";

    MainActivity that = this;
    TextView authMessage;
    EditText username;
    EditText password;
    ImageButton nextButton;
    TextView question;
    Button actionButton;
    boolean isLogIn = true;
    ServerCommunicator communicator = new ServerCommunicator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNextButton();
        actionButton = findViewById(R.id.actionButton);
        setupActionButton();

        authMessage = findViewById(R.id.loginsignupNotification);
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);
        question = findViewById(R.id.question);

        loadData();

    }

    private void setupNextButton(){
        nextButton = findViewById(R.id.imageButton);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pickUserData();
            }
        });
    }

    private void setupActionButton(){
        actionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (actionButton.getText().toString().equals("Sign up!")){
                    isLogIn = false;
                    authMessage.setText(R.string.signupFields);
                    question.setText(R.string.question2);
                    actionButton.setText(R.string.login);
                } else {
                    isLogIn = true;
                    authMessage.setText(R.string.allGood);
                    question.setText(R.string.question);
                    actionButton.setText(R.string.signup);
                }
            }
        });


    }

    private void pickUserData(){
        if (username.getText().toString().equals("") || password.getText().toString().equals("")){
            authMessage.setText(R.string.emptyFields);
        } else {
            JSONObject userData = new JSONObject();
            try{
                userData.put("username", username.getText().toString().toLowerCase().trim());
                userData.put("password", password.getText().toString().toLowerCase().trim());
                if (isLogIn){
                    System.out.println("Pozvao log in.");
                    communicator.postRequestLogIn(that ,userData, authMessage);
                } else {
                    System.out.println("Pozvao sign up.");
                    communicator.postRequestSignUp(that, userData, authMessage);
                }
            } catch(Exception e){
                System.out.println("Greska prilikom prebacivanja u userData");
            }
        }
    }

    public void saveData(){
        String usersUsername = username.getText().toString();
        String usersPassword = password.getText().toString();
        try{
            FileOutputStream f = openFileOutput(APP_DATA_PREFIX, Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(f);
            pw.println(usersUsername);
            pw.println(usersPassword);
            pw.flush();
            pw.close();
            f.close();
        }
        catch(Exception e)
        {
            System.out.println("Greska prilikom zapisivanja korisnikovih podataka.");
        }
    }

    private void loadData(){
        try {
            FileInputStream f = openFileInput(APP_DATA_PREFIX);
            BufferedReader bf = new BufferedReader(new InputStreamReader(f));

            String usersUsername = bf.readLine();
            String usersPassword = bf.readLine();

            bf.close();
            f.close();

            username.setText(usersUsername);
            password.setText(usersPassword);
        }
        catch(Exception e){
            System.out.println("Greska prilikom citanja podataka.");
        }
    }

}