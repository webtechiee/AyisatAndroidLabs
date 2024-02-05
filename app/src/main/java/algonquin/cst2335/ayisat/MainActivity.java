package algonquin.cst2335.ayisat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.ayisat.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

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

    }
}