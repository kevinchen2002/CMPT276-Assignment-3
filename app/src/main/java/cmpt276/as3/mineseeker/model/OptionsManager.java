package cmpt276.as3.mineseeker.model;

import java.util.ArrayList;
import java.util.List;

public class OptionsManager {
    private List<int[]> boardDimensions = new ArrayList<>();
    private List<Integer> mineOptions = new ArrayList<>();

    private int currentBoardOption = 0;
    private int currentMineOption = 0;

    private static OptionsManager instance;

    private OptionsManager() {
        boardDimensions.add(new int[]{4, 6});
        boardDimensions.add(new int[]{5, 10});
        boardDimensions.add(new int[]{6, 15});

        mineOptions.add(6);
        mineOptions.add(10);
        mineOptions.add(15);
        mineOptions.add(20);
    }

    public static OptionsManager getInstance() {
        if (instance == null) {
            instance = new OptionsManager();
        }

        return instance;
    }

    public void setCurrentBoardOption(int currentBoardOption) {
        this.currentBoardOption = currentBoardOption;
    }

    public void setCurrentMineOption(int currentMineOption) {
        this.currentMineOption = currentMineOption;
    }

    public int getCurrentBoardOption() {
        return currentBoardOption;
    }

    public int getCurrentMineOption() {
        return currentMineOption;
    }

    public int getRow() {
        return boardDimensions.get(currentBoardOption)[0];
    }

    public int getCol() {
        return boardDimensions.get(currentBoardOption)[1];
    }

    public int getMine() {
        return mineOptions.get(currentMineOption);
    }

    public int getTotalDimensions() {
        return boardDimensions.size();
    }

    public int getTotalMineOptions() {
        return mineOptions.size();
    }

    public List<String> getStringDimensions() {
        List<String> stringOptions = new ArrayList<>();
        for (int[] dimensions : boardDimensions) {
            String stringDimension = (dimensions[0] + " x " + dimensions[1]);
            stringOptions.add(stringDimension);
        }
        return stringOptions;
    }

    public List<String> getStringMines() {
        List<String> stringOptions = new ArrayList<>();
        for (int mines : mineOptions) {
            stringOptions.add(mines + " Pokémon");
        }
        return stringOptions;
    }

    public String getStringCurrentDimensions() {
        int[] dimensions = boardDimensions.get(currentBoardOption);
        return (dimensions[0] + " x " + dimensions[1]);
    }

    public String getStringCurrentMine() {
        int mines = mineOptions.get(currentMineOption);
        return (Integer.toString(mines));
    }
}
