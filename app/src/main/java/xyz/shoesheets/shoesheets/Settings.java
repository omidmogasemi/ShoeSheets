package xyz.shoesheets.shoesheets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch onOffSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        onOffSwitch = (Switch)findViewById(R.id.predictedSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePreferences();
            }
        });
    }

    private void updatePreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean wantPredicted = prefs.getBoolean("wantPredicted", false);
        SharedPreferences.Editor editor = prefs.edit();
        // the on-off switch for some reason works inversely - if it isn't checked then the user has really checked it and wants it to appear as true
        if (onOffSwitch.isChecked())
            editor.putBoolean("wantPredicted", false);
        else
            editor.putBoolean("wantPredicted", true);
        editor.apply();
    }
}
