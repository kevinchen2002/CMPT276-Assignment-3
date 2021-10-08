package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Learned basic spinner functionality from https://www.tutorialspoint.com/android/android_spinner_control.htm
 */
public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);
        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);

        boardSizeSpinner();
        numMinesSpinner();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    /**
     * Spinner functionality learned from https://bootstraphunter.com/how-to-add-a-dropdown-menu-in-android-studio/
     */
    void boardSizeSpinner() {
        Spinner boardSizeSpinner = findViewById(R.id.boardSizeDropDown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.board_sizes,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        boardSizeSpinner.setAdapter(adapter);

        boardSizeSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Spinner functionality learned from https://bootstraphunter.com/how-to-add-a-dropdown-menu-in-android-studio/
     */
    void numMinesSpinner() {
        Spinner numMinesSpinner = findViewById(R.id.numMinesDropDown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.num_mines,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        numMinesSpinner.setAdapter(adapter);

        numMinesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int choice = parent.getSelectedItemPosition();
        SharedPreferences.Editor editor = sp.edit();
        if (parent.getId() == R.id.boardSizeDropDown) {
            Log.d("BOARD", "picked " + choice);
            editor.putInt("boardSizeChoice", choice);
        }
        if (parent.getId() == R.id.numMinesDropDown) {
            Log.d("MINES", "picked " + choice);
            editor.putInt("numMinesChoice", choice);
        }
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}