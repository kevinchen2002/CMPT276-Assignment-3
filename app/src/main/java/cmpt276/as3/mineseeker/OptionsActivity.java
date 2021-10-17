package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as3.mineseeker.model.GameData;
import cmpt276.as3.mineseeker.model.OptionsManager;

/**
 * This activity uses spinners to allow the user to select preset board and mine options.
 * This information is stored in a singleton, and the game is constructed in GameActivity.
 * Learned basic spinner functionality from https://www.tutorialspoint.com/android/android_spinner_control.htm
 */
public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final GameData gameData = GameData.getInstance();
    private final OptionsManager options = OptionsManager.getInstance();

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);
        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);

        resetDataButton();
        boardSizeSpinner();
        numMinesSpinner();
    }

    void resetDataButton() {
        Button btn = findViewById(R.id.resetBtn);
        btn.setOnClickListener(v -> {
            gameData.clearGames();
            gameData.setGamesPlayed(0);
            Toast.makeText(getApplicationContext(),
                    "All data cleared.", Toast.LENGTH_LONG).show();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }


    // Spinner functionality learned from https://bootstraphunter.com/how-to-add-a-dropdown-menu-in-android-studio/
    void boardSizeSpinner() {
        Spinner boardSizeSpinner = findViewById(R.id.boardSizeDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options.getStringDimensions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        boardSizeSpinner.setAdapter(adapter);

        boardSizeSpinner.setSelection(options.getCurrentBoardOption());
        boardSizeSpinner.setOnItemSelectedListener(this);
    }

    void numMinesSpinner() {
        Spinner numMinesSpinner = findViewById(R.id.numMinesDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options.getStringMines());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        numMinesSpinner.setAdapter(adapter);

        numMinesSpinner.setSelection(options.getCurrentMineOption());
        numMinesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Changing selected font and colour learned from stackoverflow.com/questions/15379851/change-text-color-of-selected-item-in-spinner
         * and from https://stackoverflow.com/questions/52129212/spinner-with-custom-text-font-and-color
         */
        Typeface pokeFont = getResources().getFont(R.font.pokemon_font);
        ((TextView) parent.getSelectedView()).setTextColor(Color.BLACK);
        ((TextView) parent.getSelectedView()).setTypeface(pokeFont);

        int choice = parent.getSelectedItemPosition();
        if (parent.getId() == R.id.boardSizeDropDown) {
            options.setCurrentBoardOption(choice);
        }
        if (parent.getId() == R.id.numMinesDropDown) {
           options.setCurrentMineOption(choice);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}