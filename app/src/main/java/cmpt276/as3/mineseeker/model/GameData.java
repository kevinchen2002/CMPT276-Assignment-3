package cmpt276.as3.mineseeker.model;

import java.util.ArrayList;

public class GameData {
    private int gamesPlayed;
    private ArrayList<Integer> highScores = new ArrayList<>();
    private final int BOARD_CONFIGS = 3;
    private final int MINE_CONFIGS = 4;

    private static GameData instance;

    public GameData() {
        for (int i = 0; i < BOARD_CONFIGS*MINE_CONFIGS; i++) {
            highScores.add(1000);
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

    public ArrayList<Integer> getHighScores() {
        return highScores;
    }

    public void setHighScores(ArrayList<Integer> highScores) {
        this.highScores = highScores;
    }
}
