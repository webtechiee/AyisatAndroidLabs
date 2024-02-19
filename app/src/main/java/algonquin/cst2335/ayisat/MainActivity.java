package algonquin.cst2335.ayisat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
        Log.w(TAG, "onCreate: Activity is being created");

        // Initialize SharedPreferences
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        EditText emailEditText = findViewById(R.id.emailEditText);
        String emailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(emailAddress);

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailEditText.getText().toString());
            editor.apply();


            // Start SecondActivity
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailEditText.getText().toString());
            startActivity(nextPage);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart: The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: Activity is partially visible to the user");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: Activity is no longer visible to the user");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: Activity is being destroyed");
    }
}