package cmpt276.as3.mineseeker.model;

import android.util.Log;

import java.util.ArrayList;

public class GameData {
    private int gamesPlayed;
    private ArrayList<Integer> highScores = new ArrayList<>();
    private OptionsManager options = OptionsManager.getInstance();
    private final int BOARD_CONFIGS = options.getTotalDimensions();
    private final int MINE_CONFIGS = options.getTotalMineOptions();

    private static GameData instance;

    private final int TOTAL_GAME_VERSIONS = BOARD_CONFIGS * MINE_CONFIGS;

    public GameData() {
        for (int i = 0; i < TOTAL_GAME_VERSIONS; i++) {
            highScores.add(null);
        }
        gamesPlayed = 0;
    }

    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public void startGame() {
        gamesPlayed++;
    }

    public void clearGames() {
        gamesPlayed = 0;
        for (int i = 0; i < TOTAL_GAME_VERSIONS; i++) {
            highScores.set(i, null);
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setHighScore(int boardOption, int mineOption, int score) {
        int index = MINE_CONFIGS*boardOption + mineOption;
        if (score < highScores.get(index)) {
            highScores.set(index, score);
        }
    }

    public int getHighScore (int boardOption, int mineOption) {
        int index = MINE_CONFIGS*boardOption + mineOption;
        return highScores.get(index);
    }

    public int getHighScore (int index) {
        return highScores.get(index);
    }

    public ArrayList<Integer> getHighScores() {
        return highScores;
    }

    public void setHighScores(ArrayList<Integer> highScores) {
        this.highScores = highScores;
    }

    public boolean isThereScore(int boardOption, int mineOption) {int index = MINE_CONFIGS*boardOption + mineOption;
        return highScores.get(index) != null;
    }
}
