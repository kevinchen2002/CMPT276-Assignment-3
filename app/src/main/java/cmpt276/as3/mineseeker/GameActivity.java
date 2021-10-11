package cmpt276.as3.mineseeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import cmpt276.as3.mineseeker.model.GameData;
import cmpt276.as3.mineseeker.model.MineManager;
import cmpt276.as3.mineseeker.model.OptionsManager;


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

    private MineManager gameMineManager;

    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);
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
                        scanMineAt(FINAL_ROW, FINAL_COL);
                    }
                    updateScanCount();
                });

                MineManager.MineScanObserver obs = () -> {
                    scanMineAt(FINAL_ROW, FINAL_COL);
                };
                gameMineManager.registerChangeCallBack(obs);

                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void scanMineAt(int x, int y) {
        if (gameMineManager.isTappedAt(x, y)) {
            int mineCount = gameMineManager.getNearbyMines(x, y);
            buttons[x][y].setText("" + mineCount);
        }
    }

    private void vibrateByCell(int x, int y) {
        //TODO: convert into constants or local variables? As Brian which to do
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

    private void revealMine(int x, int y) {
        Button button = buttons[x][y];
        updateMineCount();
        lockButtonSize();
        scaleImageToButton(button, R.drawable.coolguy);
        if (gameMineManager.isGameWon()) {
            winGame();
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
        mineCount.setText(String.format("Found %d of %d mines",
                gameMineManager.getMinesFound(),
                gameMineManager.getNumMines()));
    }

    private void scaleImageToButton (Button button, int imageID) {
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageID);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
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
        FragmentManager manager = getSupportFragmentManager();
        WinMessageFragment winnerMessage = new WinMessageFragment();
        winnerMessage.show(manager, "You did it!");
    }

    @Override
    public void finish() {
        if (gameMineManager.isGameWon()) {
            int boardSize = options.getCurrentBoardOption();
            int mines = options.getCurrentMineOption();
            GameData storeResults = GameData.getInstance();
            storeResults.setHighScore(boardSize, mines, gameMineManager.getMinesChecked());
        }

        super.finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}
