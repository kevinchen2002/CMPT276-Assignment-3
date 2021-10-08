package cmpt276.as3.mineseeker.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MineManager {
    //TODO: change these two grid into one
    Boolean[][] mineCoordinates;
    Boolean[][] hasBeenTapped;
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

    private void updateNearbyMines() {
        for (MineScanObserver obs : observers) {
            obs.scanForMines();
        }
    }

    public interface MineScanObserver {
        void scanForMines();
    }

    public MineManager(int rows, int columns, int mines) {
        NUM_ROWS = rows;
        NUM_COLUMNS = columns;
        NUM_MINES = mines;
        mineCoordinates = new Boolean[NUM_ROWS][NUM_COLUMNS];
        hasBeenTapped = new Boolean[NUM_ROWS][NUM_COLUMNS];
        minesFound = 0;
        minesChecked = 0;
        for (Boolean[] row : mineCoordinates) {
            Arrays.fill(row, false);
        }
        for (Boolean[] row : hasBeenTapped) {
            Arrays.fill(row, false);
        }

        plantMines();
    }


    private void plantMines() {
        Random rand = new Random();
        int i = 0;
        while (i < NUM_MINES) {
            int randRow = rand.nextInt(NUM_ROWS);
            int randCol = rand.nextInt(NUM_COLUMNS);
            if (!mineCoordinates[randRow][randCol]) {
                mineCoordinates[randRow][randCol] = true;
                i++;
            }
        }
    }

    public int checkMineAt(int x, int y) {
        if (mineCoordinates[x][y]) {
            minesFound++;
            mineCoordinates[x][y] = false;

            if (minesFound == NUM_MINES) {
                System.out.println("You did it!");
            }
            updateNearbyMines();
        } else {
            hasBeenTapped[x][y] = true;
            return getNearbyMines(x, y);
        }
        return -1;
    }

    public int getNearbyMines(int x, int y) {
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

        if (hasBeenTapped[x][y]) {
            return foundMines;
        } else {
            //returns negative 1 if it hasn't been tapped and shouldn't update
            return -1;
        }
    }

}
