package cmpt276.as3.mineseeker.model;

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
