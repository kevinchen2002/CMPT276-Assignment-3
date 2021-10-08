package cmpt276.as3.mineseeker.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineManager {
    Boolean[][] mineCoordinates;
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

    private void notifyMineScan() {
        for (MineScanObserver obs : observers) {
            obs.notifyMineScan();
        }
    }

    public interface MineScanObserver {
        void notifyMineScan();
    }

    public MineManager(int rows, int columns, int mines) {
        NUM_ROWS = rows;
        NUM_COLUMNS = columns;
        NUM_MINES = mines;
        mineCoordinates = new Boolean[NUM_ROWS][NUM_COLUMNS];
        minesFound = 0;
        minesChecked = 0;
        for (Boolean[] row : mineCoordinates) {
            Arrays.fill(row, false);
        }

        plantMines();
    }

//    public static MineManager getInstance() {
//        if (instance == null) {
//            instance = new MineManager();
//        }
//        return instance;
//    }

    private void plantMines() {
        //TODO: randomly choose value in the array to set to true
        mineCoordinates[1][1] = true;
    }

    public int checkMine(int x, int y) {
        if (mineCoordinates[x][y]) {
            minesFound++;
            mineCoordinates[x][y] = false;

            if (minesFound == NUM_MINES) {
                System.out.println("You did it!");
            }
        } else {
            return getMineCount(x, y);
        }

        return -1;
    }

    public int getMineCount(int x, int y) {
        int foundMines = 0;

        for (int row = 0; row < NUM_ROWS; row++) {
            if (mineCoordinates[row][y]) {
                foundMines++;
            }
        }

        for (int col = 0; col < NUM_COLUMNS; col++) {
            if (mineCoordinates[x][col]) {
                foundMines++;
            }
        }

        return foundMines;
    }

}
