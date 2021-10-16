package cmpt276.as3.mineseeker.model;

/**
 * CellInfo represents a single cell in the game. It tells if the cell has a mine, has been tapped,
 * and if it had a mine at one point.
 */
public class CellInfo {
    private boolean hasActiveMine;
    private boolean wasMine;
    private boolean hasBeenTapped;

    public CellInfo() {
        hasActiveMine = false;
        hasBeenTapped = false;
        wasMine = false;
    }

    public void setMine() {
        this.hasActiveMine = true;
        this.wasMine = true;
    }

    public boolean hasActiveMine() {
        return hasActiveMine;
    }

    public boolean wasMine() {
        return wasMine;
    }

    public boolean hasBeenTapped() {
        return hasBeenTapped;
    }

    public void setTappedTrue() {
        hasBeenTapped = true;
    }

    public void disableMine() {
        hasActiveMine = false;
    }
}
