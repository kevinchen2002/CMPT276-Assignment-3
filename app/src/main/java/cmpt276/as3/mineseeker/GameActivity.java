package cmpt276.as3.mineseeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cmpt276.as3.mineseeker.model.GameData;
import cmpt276.as3.mineseeker.model.MineManager;
import cmpt276.as3.mineseeker.model.OptionsManager;

/**
 * GameActivity manages all the gameplay of the app. It creates the buttons based on OptionsActivity
 * and decorates them. It uses its private list of Pokemon to randomly show a Pokemon. The backbone
 * is a MineManager.
 */
public class GameActivity extends AppCompatActivity {
    private final GameData gameData = GameData.getInstance();
    private final OptionsManager options = OptionsManager.getInstance();

    SharedPreferences sp;

    private final int MINE_VIBE_AMPLITUDE = 255;
    private final int MINE_VIBE_TIME_MS = 25;
    private final int EMPTY_VIBE_MULTIPLIER = 3;
    private final int EMPTY_VIBE_TIME = 127;

    private int NUM_ROWS;
    private int NUM_COLUMNS;
    private int NUM_MINES;

    private MediaPlayer mpGrass;

    private final ArrayList pokemonList = new ArrayList<>(Arrays.asList("aron", "bidoof",
            "blitzle", "cubone", "cyndaquil", "drilbur", "ducklett", "electrike", "gligar", "hoppip",
            "horsea", "joltik", "larvesta", "litwick", "mantyke", "omanyte", "paras", "poliwag",
            "roggenrola", "skorupi", "snivy", "snover", "spinarak", "starly", "taillow", "tangela",
            "tirtouga", "togepi", "treecko", "turtwig", "tynamo", "wailmer", "yanma"));

    private MineManager gameMineManager;

    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);
        mpGrass = MediaPlayer.create(this, R.raw.grass_sfx);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        gameData.startGame();
        showBestScore();
        initializeMines();
        updateMineCount();
        updateScanCount();
        populateMines();
    }

    private void initializeMines() {
        NUM_ROWS = options.getRow();
        NUM_COLUMNS = options.getCol();

        NUM_MINES = options.getMine();

        gameMineManager = new MineManager(NUM_ROWS, NUM_COLUMNS, NUM_MINES);
        buttons = new Button[NUM_ROWS][NUM_COLUMNS];
    }

    @SuppressLint("SetTextI18n")
    private void populateMines() {
        TableLayout table = findViewById(R.id.mine_table);
        for (int row = 0; row < NUM_ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLUMNS; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                        ));


                //make clip on small buttons
                button.setPadding(0,0,0,0);

                button.setOnClickListener(v -> {

                    if (gameMineManager.isTappedAt(FINAL_ROW, FINAL_COL)) {
                        return;
                    }
                    vibrateByCell(FINAL_ROW, FINAL_COL);
                    if (gameMineManager.isMineAt(FINAL_ROW, FINAL_COL)) {
                        revealMine(FINAL_ROW, FINAL_COL);
                    } else {
                        mpGrass.start();
                        scanMineAt(FINAL_ROW, FINAL_COL);
                    }
                    updateScanCount();
                });

                MineManager.MineScanObserver obs = () -> scanMineAt(FINAL_ROW, FINAL_COL);
                gameMineManager.registerChangeCallBack(obs);

                button.setBackgroundResource(R.drawable.grasstile);
                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void scanMineAt(int x, int y) {
        Button button = buttons[x][y];
        if (gameMineManager.isTappedAt(x, y)) {
            if (!gameMineManager.wasMineAt(x, y)) {
                button.setBackground(null);
            }
            int mineCount = gameMineManager.getNearbyMines(x, y);
            lockButtonSize();
            button.setText("" + mineCount);

            setButtonStyle(button);
        }
    }

    private void setButtonStyle(Button button) {
        Typeface pokeFont = getResources().getFont(R.font.pokemon_font);
        button.setTypeface(pokeFont);
        button.setTextColor(Color.WHITE);
        button.setTextSize(11);
    }

    private void revealMine(int x, int y) {
        Button button = buttons[x][y];
        updateMineCount();
        lockButtonSize();
        String pokemonName = getRandomPokemon();
        scaleImageToButton(button, pokemonName);
        playCry(pokemonName);
        if (gameMineManager.isGameWon()) {
            winGame();
        }
    }

    private void vibrateByCell(int x, int y) {
        Vibrator mineFeedback = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameMineManager.checkMineAt(x, y)) {
            mineFeedback.vibrate(VibrationEffect.createOneShot(MINE_VIBE_AMPLITUDE,
                                                                MINE_VIBE_TIME_MS));
        } else {
            int mineCount = gameMineManager.getNearbyMines(x, y);
            mineFeedback.vibrate(VibrationEffect.createOneShot(
                    ((long) (mineCount + 1) * EMPTY_VIBE_MULTIPLIER),
                    EMPTY_VIBE_TIME));
        }
    }

    @SuppressLint("SetTextI18n")
    void showBestScore() {
        int boardOption = options.getCurrentBoardOption();
        int mineOption = options.getCurrentMineOption();
        TextView bestScoreView = findViewById(R.id.gameBestScoreView);
        if (gameData.isThereScore(boardOption, mineOption)) {
            bestScoreView.setText("Best score: " + gameData.getHighScore(boardOption, mineOption));
        }
    }

    @SuppressLint("DefaultLocale")
    public void updateScanCount() {
        TextView scanCount = findViewById(R.id.showScansUsed);
        scanCount.setText(String.format("# Scans Used: %d",
                gameMineManager.getMinesChecked()));
    }

    @SuppressLint("DefaultLocale")
    public void updateMineCount() {
        TextView mineCount = findViewById(R.id.showMinesFound);
        mineCount.setText(String.format("%d of %d Pokémon caught!",
                gameMineManager.getMinesFound(),
                gameMineManager.getNumMines()));
    }

    /*
     * All Pokemon sprites taken from https://pokemondb.net/sprites
     */
    private void scaleImageToButton (Button button, String imageName) {
        int scaleWidth = button.getWidth();
        int scaleHeight = button.getHeight();

        int imageID = getResources().getIdentifier(imageName, "drawable", GameActivity.this.getPackageName());
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageID);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaleWidth, scaleHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    /*
     * All Pokemon cries taken from https://play.pokemonshowdown.com/audio/cries/
     */
    private void playCry(String soundName) {
        int soundID = getResources().getIdentifier(soundName, "raw", GameActivity.this.getPackageName());
        MediaPlayer mp = MediaPlayer.create(this, soundID);
        mp.start();
    }

    private String getRandomPokemon() {
        Random random = new Random();
        int randomIndex = random.nextInt(pokemonList.size());
        return (String) pokemonList.get(randomIndex);
    }

    private void lockButtonSize() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                Button button =  buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void winGame() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.win_sfx);
        mp.start();
        FragmentManager manager = getSupportFragmentManager();
        WinMessageFragment winnerMessage = new WinMessageFragment();
        winnerMessage.show(manager, "You did it!");
    }

    private void checkWinCondition() {
        if (gameMineManager.isGameWon()) {
            int boardSize = options.getCurrentBoardOption();
            int mines = options.getCurrentMineOption();
            GameData storeResults = GameData.getInstance();
            storeResults.setHighScore(boardSize, mines, gameMineManager.getMinesChecked());
        }
    }

    @Override
    public void finish() {
        super.finish();
        checkWinCondition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameMineManager.deRegisterAllChangeCallBack();
    }

    @Override
    public boolean onSupportNavigateUp() {
        checkWinCondition();
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}
