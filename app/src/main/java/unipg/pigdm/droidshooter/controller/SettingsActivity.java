package unipg.pigdm.droidshooter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import unipg.pigdm.droidshooter.R;

public class SettingsActivity extends AppCompatActivity {

    private View.OnClickListener backToMenuClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            backToMenu();
        }

    };

    //prevents the user from inputting wrong values, only numbers are allowed for numeric settings
    private static void setOnBindEditTextListener(EditTextPreference editTextPreference) {
        if (editTextPreference != null) {
            editTextPreference.setOnBindEditTextListener(
                    new EditTextPreference.OnBindEditTextListener() {
                        @Override
                        public void onBindEditText(@NonNull EditText editText) {
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    });
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            EditTextPreference crosshairSpeed = findPreference("crosshair_speed");
            SettingsActivity.setOnBindEditTextListener(crosshairSpeed);
            EditTextPreference enemySpeed = findPreference("enemy_speed");
            SettingsActivity.setOnBindEditTextListener(enemySpeed);
            EditTextPreference enemyNumber = findPreference("enemy_number");
            SettingsActivity.setOnBindEditTextListener(enemyNumber);
            EditTextPreference timer = findPreference("game_timer");
            SettingsActivity.setOnBindEditTextListener(timer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button menuButton = findViewById(R.id.backToMenuButton);
        menuButton.setOnClickListener(backToMenuClickListener);

    }

    private void backToMenu() {
        Intent intent = new Intent(SettingsActivity.this, StartGameActivity.class);
        startActivity(intent);
    }


}