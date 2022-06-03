package com.example.trucksharing.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.trucksharing.R;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.fragment.CreateOrderFragment;
import com.example.trucksharing.model.Truck;
import com.example.trucksharing.model.User;
import com.example.trucksharing.util.Util;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper database;

    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);

        List<Truck> trucks = database.fetchAllTrucks();
        if (trucks.size() == 0) {
            // Add Trucks to database
            String[] imageNames = {"ford", "chevrolet", "hyundai", "nissan"};
            String[] names = {"Ford", "Chevrolet", "Hyundai", "Nissan"};
            String[] status = {
                    "3.5L EcoBoost® High-Output V6",
                    "6.5L EcoBoost® High-Performance",
                    "4.5L EcoBoost® High-Output V8",
                    "2.0L EcoBoost® High-Output V6"
            };
            for (int i = 0; i < imageNames.length; i++) {
                Truck truck = new Truck(imageNames[i], names[i], status[i]);
                database.insertTruck(truck);
            }
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
    }

    public void loginClick(View view) {

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("");
            alertDialog.setMessage("Please enter username and password!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            return;
        }

        int userId = database.fetchUser(username, password);
        if (userId >= 0) {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            homeIntent.putExtra(Util.USER_ID, userId);
            startActivity(homeIntent);
        } else {
            Log.d(MainActivity.this.toString(), "The user does not exist");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Oops!");
            alertDialog.setMessage("The user does not exist");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
    }

    public void signupClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}