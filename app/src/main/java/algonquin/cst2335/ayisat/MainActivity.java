package algonquin.cst2335.ayisat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity allows the user to input a password and checks its complexity.
 * It provides feedback to the user based on the complexity of the entered password.
 *
 * @author Ayisat Bakare
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** EditText for user input */
    private EditText edt;

    /** Button for triggering password complexity check */
    private Button btn;

    /** TextView for displaying the result of password complexity check */
    private TextView textView;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       TextView txtv=findViewById(R.id.textView);
       Button btn = findViewById(R.id.button);
        EditText edt = findViewById(R.id.editTextText2);

        btn.setOnClickListener( clk ->{
            String password = edt.getText().toString();
            boolean isComplex = checkPasswordComplexity(password);

            if (isComplex) {
                txtv.setText("Your password meets the requirements");
            } else {
                txtv.setText("You shall not pass!");
            }
        });
        }

    /** This function check if the password meets the complexity requirement
     *
     * @param pw The string that we are checking
     * @return Returns true if the password is complex enough
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }

    }
}



