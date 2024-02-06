package algonquin.cst2335.ayisat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import algonquin.cst2335.ayisat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        // Initialize ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Button btn = variableBinding.button;
        EditText myedit = variableBinding.myedittext;
        TextView mytext = variableBinding.textView;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current text in the EditText
                String editString = myedit.getText().toString();
                // Set the string on the TextView
                mytext.setText("Your edit text has: " + editString);
            }
        });

        // Observe changes in isSomethingEnabled and update CompoundButtons
        mainViewModel.isSomethingEnabled.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isChecked) {
                // Show a Toast message with the new value
                String toastMessage = "The value is now: " + isChecked;
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                // Update all CompoundButtons to the new value
                variableBinding.checkBox.setChecked(isChecked);
                variableBinding.radioButton2.setChecked(isChecked);
                variableBinding.switch2.setChecked(isChecked);
            }
        });

        // Set OnCheckedChangeListener for each CompoundButton using lambda expression
        variableBinding.checkBox.setOnCheckedChangeListener((button, isChecked) -> {
            mainViewModel.isSomethingEnabled.postValue(isChecked);
        });

        variableBinding.radioButton2.setOnCheckedChangeListener((button, isChecked) -> {
            mainViewModel.isSomethingEnabled.postValue(isChecked);
        });

        variableBinding.switch2.setOnCheckedChangeListener((button, isChecked) -> {
            mainViewModel.isSomethingEnabled.postValue(isChecked);
        });

        // Access ImageView using ViewBinding
        ImageView imageView = variableBinding.imageView;

        // Set onClick listener for ImageView using lambda expression
        imageView.setOnClickListener(v -> {
            // Handle onClick event
            // For example, show a toast message
            Toast.makeText(MainActivity.this, "ImageView clicked", Toast.LENGTH_SHORT).show();
        });

       // ActivityMainBinding binding = null;
        variableBinding.imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get width and height of the ImageButton
                int width = v.getWidth();
                int height = v.getHeight();

                // Show Toast message with width and height
                String toastMessage = "The width = " + width + " and height = " + height;
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }
}
