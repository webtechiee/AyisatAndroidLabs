package algonquin.cst2335.ayisat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cameraResult;
    private ImageView profileImageView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();

        TextView txtWelcome = findViewById(R.id.textView2);

        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        txtWelcome.setText(String.format("Welcome Back: %s", emailAddress));

        EditText phoneNumberEditText = findViewById(R.id.editTextPhone);

        // Load the saved phone number and set it to phoneNumberEditText
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("PhoneNumber", "");
        phoneNumberEditText.setText(phoneNumber);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // You can directly request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

        Button callButton = findViewById(R.id.button2);
        callButton.setOnClickListener(v -> {
            String phoneNumberStr = phoneNumberEditText.getText().toString();

            // Create the intent to dial the phone number
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumberStr));

            startActivity(callIntent);
        });

        // Set up the ActivityResultLauncher for camera intent
        cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Get the captured image as a Bitmap
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap thumbnail = (Bitmap) extras.get("data");
                                // Set the Bitmap as the profile picture
                                profileImageView.setImageBitmap(thumbnail);
                                saveImageToFile(thumbnail);
                            }
                        }
                    }
                });

        // Set up the click listener for the "Change Profile Picture" button
        findViewById(R.id.button3).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, launch the camera intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraResult.launch(cameraIntent);
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        });

        // Initialize the profileImageView
        profileImageView = findViewById(R.id.imageView);

        // Check if the saved image file exists and load it into the ImageView
        File file = new File(getFilesDir(), "ProfilePicture.png");
        if (file.exists()) {
            Bitmap savedBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImageView.setImageBitmap(savedBitmap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneNumberEditText = findViewById(R.id.editTextPhone);
        // Save the phone number when the activity goes off screen
        SharedPreferences.Editor editor = getSharedPreferences("MyData", Context.MODE_PRIVATE).edit();
        editor.putString("PhoneNumber", phoneNumberEditText.getText().toString());
        editor.apply();
    }

    private void saveImageToFile(Bitmap bitmap) {
        FileOutputStream fOut = null;
        try {
            File file = new File(getFilesDir(), "ProfilePicture.png");
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Log.d("SecondActivity", "File saved: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("SecondActivity", "Error saving file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Show the captured image in the ImageView
            if (resultCode == Activity.RESULT_OK) {
                ImageView img = findViewById(R.id.imageView);
                Bitmap thumbnail = data.getParcelableExtra("data");
                img.setImageBitmap(thumbnail);

                // Save the captured image in the device
                saveImageToFile(thumbnail);
            } else {
                // Handle cancel or failed capture
                Toast.makeText(this, "Camera capture canceled or failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
