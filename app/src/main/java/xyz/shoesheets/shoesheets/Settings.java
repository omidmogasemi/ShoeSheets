package xyz.shoesheets.shoesheets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        logoutButton = (Button) findViewById(R.id.logout_button);

        Switch onOffSwitch = (Switch) findViewById(R.id.predictedSwitch);
        setSwitch();
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePreferences();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });
    }

    private void setSwitch(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean wantPredicted = prefs.getBoolean("wantPredicted", false);
        Switch onOffSwitch = (Switch)findViewById(R.id.predictedSwitch);
        if (wantPredicted)
            onOffSwitch.setChecked(true);
        else
            onOffSwitch.setChecked(false);

    }

    private void updatePreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean wantPredicted = prefs.getBoolean("wantPredicted", false);
        SharedPreferences.Editor editor = prefs.edit();
        Switch onOffSwitch = (Switch)findViewById(R.id.predictedSwitch);
        // the on-off switch for some reason works inversely - if it isn't checked then the user has really checked it and wants it to appear as true
        if (onOffSwitch.isChecked())
            editor.putBoolean("wantPredicted", true);
        else
            editor.putBoolean("wantPredicted", false);
        Log.d("BOOLEAN_VALUE", onOffSwitch.isChecked() + "");
        editor.apply();
    }

    private void sendUserToLoginActivity() {
        // this is bad design as it adds another intent to the stack
        // need to come back and fix later once working, perhaps with finish()?
        Intent loginIntent = new Intent(Settings.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}