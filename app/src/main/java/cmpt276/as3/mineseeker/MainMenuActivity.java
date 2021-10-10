package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

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

        int gamesPlayed =sp.getInt("MineSeeker games played", 0);
        gameData.setGamesPlayed(gamesPlayed);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }
}
