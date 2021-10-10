package cmpt276.as3.mineseeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cmpt276.as3.mineseeker.model.GameData;

public class MainMenuActivity extends AppCompatActivity {
    private final GameData gameData = GameData.getInstance();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);
        loadGame();
        continueToGameButton();
        continueToOptionsButton();
        continueToHelpButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveGame();
        updateText();
    }

    //@SuppressLint("SetTextI18n")
    @SuppressLint("SetTextI18n")
    void updateText() {

        TextView gamesPlayedView = findViewById(R.id.gamesPlayedView);
        Log.d("update", "games played = "+gameData.getGamesPlayed());
        gamesPlayedView.setText("Games played: " + gameData.getGamesPlayed());

        int boardSize = sp.getInt("boardSizeChoice", -1);
        int mines = sp.getInt("numMinesChoice", -1);
        TextView boardConfigView = findViewById(R.id.boardConfigView);
        TextView mineNumView = findViewById(R.id.mineNumView);

        switch (boardSize) {
            case(0):
                boardConfigView.setText("Board size: 4 x 6");
                break;
            case (1):
                boardConfigView.setText("Board size: 5 x 10");
                break;
            case(2):
                boardConfigView.setText("Board size: 6 x 15");
                break;
            default:
                boardConfigView.setText("Board size: 5 x 7");
        }

        switch (mines) {
            case(0):
                mineNumView.setText("Mine count: 6");
                break;
            case(1):
                mineNumView.setText("Mine count: 10");
                break;
            case(2):
                mineNumView.setText("Mine count: 15");
                break;
            case(3):
                mineNumView.setText("Mine count: 20");
                break;
            default:
                mineNumView.setText("Mine count: 3");
        }

        TextView highScoreView = findViewById(R.id.highScoreView);
        highScoreView.setText("Best score for this configuration: "+gameData.getHighScore(boardSize, mines));
    }

    void continueToGameButton() {
        Button btn = findViewById(R.id.main_to_game_btn);
        btn.setOnClickListener(v -> {
            Intent launchGame = GameActivity.makeIntent(MainMenuActivity.this);
            startActivity(launchGame);
        });
    }

    void continueToOptionsButton() {
        Button btn = findViewById(R.id.main_to_options_btn);
        btn.setOnClickListener(v -> {
            Intent launchOptions = OptionsActivity.makeIntent(MainMenuActivity.this);
            startActivity(launchOptions);
        });
    }

    void continueToHelpButton() {
        Button btn = findViewById(R.id.main_to_help_btn);
        btn.setOnClickListener(v -> {
            Intent launchHelp = HelpActivity.makeIntent(MainMenuActivity.this);
            startActivity(launchHelp);
        });
    }

    void saveGame() {
        SharedPreferences.Editor editor = sp.edit();
        Gson myGson = new GsonBuilder().create();

        String jsonHighScores = myGson.toJson(gameData.getHighScores());
        editor.putString("MineSeeker highscores", jsonHighScores);

        int gamesPlayed = gameData.getGamesPlayed();
        Log.d("save", "games played = "+gamesPlayed);
        editor.putInt("MineSeeker games played", gamesPlayed);

        editor.apply();
    }

    void loadGame() {
        Gson myGson = new GsonBuilder().create();

        String jsonHighScores = sp.getString("MineSeeker highscores", "");
        if (!jsonHighScores.equals("")) {
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            gameData.setHighScores(myGson.fromJson(jsonHighScores, listType));
        }

        int gamesPlayed = sp.getInt("MineSeeker games played", 0);
        //if (gamesPlayed != 0) {
            Log.d("load", "games played = "+gamesPlayed);
            gameData.setGamesPlayed(gamesPlayed);
        //}
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }
}
