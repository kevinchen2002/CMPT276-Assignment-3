package cmpt276.as3.mineseeker.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineManager {
    //TODO: change these two grid into one
    CellInfo[][] mineCoordinates;

    private final int NUM_ROWS;
    private final int NUM_COLUMNS;
    private final int NUM_MINES;

    private int minesFound;

    private int minesChecked;
    private static MineManager instance;

    private List<MineScanObserver> observers = new ArrayList<>();

    public void registerChangeCallBack(MineScanObserver obs) {
        observers.add(obs);
    }

    private void refreshScreen() {
        for (MineScanObserver obs : observers) {
            obs.gotCallBack();
        }
    }

    public interface MineScanObserver {

        void gotCallBack();
    }
    public MineManager(int rows, int columns, int mines) {
        NUM_ROWS = rows;
        NUM_COLUMNS = columns;
        NUM_MINES = mines;
        mineCoordinates = new CellInfo[NUM_ROWS][NUM_COLUMNS];

        minesFound = 0;
        minesChecked = 0;
        for (CellInfo[] row : mineCoordinates) {
            for (int i = 0; i < NUM_COLUMNS; i++) {
                row[i] = new CellInfo();
            }
        }

        plantMines();
    }


    private void plantMines() {
        Random rand = new Random();
        int i = 0;
        while (i < NUM_MINES) {
            int randRow = rand.nextInt(NUM_ROWS);
            int randCol = rand.nextInt(NUM_COLUMNS);
            if (!mineCoordinates[randRow][randCol].hasMine()) {
                mineCoordinates[randRow][randCol].setMine();
                i++;
            }
        }
    }

    public boolean isMineAt(int x, int y) {
        CellInfo currentCell = mineCoordinates[x][y];
        minesChecked++;
        if (currentCell.hasMine()) {
            minesFound++;
            currentCell.disableMine();


            refreshScreen();
            return true;
        }


        currentCell.setTappedTrue();
        return false;
    }

    public boolean isTappedAt(int x, int y) {
        return mineCoordinates[x][y].hasBeenTapped();
    }

    public int getNearbyMines(int x, int y) {
        int foundMines = 0;

        for (int row = 0; row < NUM_ROWS; row++) {
            if (mineCoordinates[row][y].hasMine()) {
                foundMines++;
            }
        }
        for (int col = 0; col < NUM_COLUMNS; col++) {
            if (mineCoordinates[x][col].hasMine()) {
                foundMines++;
            }
        }

        return foundMines;
    }

    public int getNumMines() {
        return NUM_MINES;
    }

    public int getMinesChecked() {
        return minesChecked;
    }

    public int getMinesFound() {
        return minesFound;
    }
}
