package algonquin.cst2335.ayisat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentCallbacks;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Switch;
import java.util.Locale;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity {

    ImageView imgView;
    Switch sw;
    private ImageView flagImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.imageView);
        sw = findViewById(R.id.spin_switch);
        sw.setOnCheckedChangeListener((btn, isChecked) -> {
        });

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(5000);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());
                imgView.startAnimation(rotate);
            } else {
                imgView.clearAnimation();
            }
        });

        // Initialize flagImageView
        flagImageView = findViewById(R.id.imageView);

        // Register a configuration change listener
        getApplicationContext().registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                ComponentCallbacks.super.toString();
                // Check if the language has changed
                if (!newConfig.locale.equals(Locale.getDefault())) {
                    // Language has changed, update flag image
                    updateFlagImage();
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
    }

    private void updateFlagImage() {
        Locale locale = Locale.getDefault();
        int flagImageResource;
        if (locale.getLanguage().equals("en")) {
            flagImageResource = R.drawable.flag; // Default flag image
        } else {
            flagImageResource = R.drawable.flag2; // Second flag image for other languages
        }
        flagImageView.setImageResource(flagImageResource);
    }
}
