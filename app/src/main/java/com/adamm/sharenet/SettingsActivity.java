package com.adamm.sharenet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private AppDatabase mDatabase;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mPasswordField;
    private Button mBtnEditPassword;
    private EditText mEmailField;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDatabase = AppDatabase.getAppDatabase(getApplicationContext());

        // Views
        mFirstNameField = findViewById(R.id.editFirstName);
        mLastNameField = findViewById(R.id.editLastName);
        mEmailField = findViewById(R.id.editEmail);
        mPasswordField = findViewById(R.id.editPassword);
        mBtnEditPassword = findViewById(R.id.btnEditPassword);

        ((TextView)findViewById(R.id.txtLoggedUser)).setText(AppDatabase.getCurr_user().username);
        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mEmailField.setText(sharedPref.getString("email", ""));
        mFirstNameField.setText(sharedPref.getString("firstName", ""));
        mLastNameField.setText(sharedPref.getString("lastName", ""));

        editor = sharedPref.edit();
        // Click listeners
        mBtnEditPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to edit the password?")
                .setTitle("Confirm password edit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppDatabase.getCurr_user().password = mPasswordField.getText().toString();
                mDatabase.userDao().editUser(AppDatabase.getCurr_user());
                Toast.makeText(getApplicationContext(), "Password was edited successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    protected void onStop() {
        editor.putString("firstName", mFirstNameField.getText().toString());
        editor.putString("lastName", mLastNameField.getText().toString());
        editor.apply();//Save input data into shared preferences
        AppDatabase.getCurr_user().firstName = mFirstNameField.getText().toString();
        AppDatabase.getCurr_user().lastName = mLastNameField.getText().toString();
        mDatabase.userDao().editUser(AppDatabase.getCurr_user());
        super.onStop();
    }
}
