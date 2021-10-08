package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as3.mineseeker.model.MineManager;


public class GameActivity extends AppCompatActivity {
    SharedPreferences sp;

    private final int DEFAULT_ROWS = 5;
    private final int DEFAULT_COLUMNS = 7;
    private final int DEFAULT_MINES = 3;

    private int NUM_ROWS;
    private int NUM_COLUMNS;
    private int NUM_MINES;

    //TODO: convert to singleton model
    private MineManager gameMineManager;

    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("MineSeeker", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        initializeMines();
        populateMines();



    }

    private void initializeMines() {
        int boardSize = sp.getInt("boardSizeChoice", -1);
        int mines = sp.getInt("numMinesChoice", -1);

        //TODO: make new elegant
        switch (boardSize) {
            case(0):
                NUM_ROWS = 4;
                NUM_COLUMNS = 6;
                break;
            case (1):
                NUM_ROWS = 5;
                NUM_COLUMNS = 10;
                break;
            case(2):
                NUM_ROWS = 6;
                NUM_COLUMNS = 15;
                break;
            default:
                NUM_ROWS = DEFAULT_ROWS;
                NUM_COLUMNS = DEFAULT_COLUMNS;
        }

        switch (mines) {
            case(0):
                NUM_MINES = 6;
                break;
            case(1):
                NUM_MINES = 10;
                break;
            case(2):
                NUM_MINES = 15;
                break;
            case(3):
                NUM_MINES = 20;
                break;
            default:
                NUM_MINES = DEFAULT_MINES;
        }

        gameMineManager = new MineManager(NUM_ROWS, NUM_COLUMNS, NUM_MINES);


        buttons = new Button[NUM_ROWS][NUM_COLUMNS];

    }

    private void populateMines() {
        TableLayout table = (TableLayout) findViewById(R.id.mine_table);
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

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int result = gameMineManager.checkMineAt(FINAL_ROW, FINAL_COL);
                        if (result == -1) {
                            //this is where the action happens
                            findMine(FINAL_ROW, FINAL_COL);
                        } else {
                            button.setText("" + result);
                        }
                    }
                });

                MineManager.MineScanObserver obs = new MineManager.MineScanObserver() {
                    @Override
                    public void scanForMines() {
                        int result = gameMineManager.getNearbyMines(FINAL_ROW, FINAL_COL);
                        if (result != -1) {
                            button.setText("" + result);
                        }
                    }
                };
                gameMineManager.registerChangeCallBack(obs);

                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    private void findMine(int x, int y) {
        Toast.makeText(this, "You clicked the button " + x + ", " + y, Toast.LENGTH_LONG).show();
        Button button = buttons[x][y];

        lockButtonSize();
        scaleImageToButton(button, R.drawable.coolguy);
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}
