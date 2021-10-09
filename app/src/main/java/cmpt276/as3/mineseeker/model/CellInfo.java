package cmpt276.as3.mineseeker.model;

public class CellInfo {
    private boolean hasMine;
    private boolean hasBeenTapped;

    public CellInfo() {
        hasMine = false;
        hasBeenTapped = false;
    }

    public void setMine() {
        this.hasMine = true;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public boolean hasBeenTapped() {
        return hasBeenTapped;
    }

    public void setTappedTrue() {
        hasBeenTapped = true;
    }

    public void disableMine() {
        hasMine = false;
    }
}
