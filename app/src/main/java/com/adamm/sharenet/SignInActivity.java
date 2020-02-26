package com.adamm.sharenet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.entities.User;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private AppDatabase mDatabase;

    String TAG ="";
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = AppDatabase.getAppDatabase(getApplicationContext());

        // Views
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        mSignInButton = findViewById(R.id.buttonSignIn);
        mSignUpButton = findViewById(R.id.buttonSignUp);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
            signIn();
        } else if (i == R.id.buttonSignUp) {
            signUp();
        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        User user = mDatabase.userDao().getLoginDetails(email);
        if(user == null || !(user.password.equals(password)))
            Toast.makeText(this, "Invalid login details!", Toast.LENGTH_SHORT).show();
        else {
            AppDatabase.setCurr_user(user);
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if((mDatabase.userDao().getLoginDetails(email)) != null)
            Toast.makeText(this, "Registration failed user already exists", Toast.LENGTH_SHORT).show();
        else {
            String username = email.split("@")[0];
            writeNewUser(username, email, password);

            // Go to MainActivity
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else if (!isValidEmail(mEmailField.getText().toString())) {
            mEmailField.setError("Invalid email format!");
            result = false;
        } else
            mEmailField.setError(null);

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else
            mPasswordField.setError(null);

        return result;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void writeNewUser(String userName, String email, String password) {//Write user to database
        User newUser = new User(userName, email, password);
        mDatabase.userDao().addUser(newUser);
        AppDatabase.setCurr_user(mDatabase.userDao().getUser(email));
    }
}