import bagel.Window;
import bagel.util.*;

public class Note {
    private final String noteType;
    private final String noteDirection;
    private final Point noteCoord;
    private final int noteFrame;
    private boolean beenPressed;
    private int points;
    private int startMsgFrame;
    private int endMsgFrame;
    private boolean beenReleased;
    private boolean leftScreen;

    /* Constructors, setters and getters */
    public Note(String noteType, String noteDirection, Point noteCoord, int noteFrame,
                boolean beenPressed, int points, int startMsgFrame, int endMsgFrame, boolean beenReleased, boolean leftScreen) {
        this.noteType = noteType;
        this.noteDirection = noteDirection;
        this.noteCoord = noteCoord;
        this.noteFrame = noteFrame;
        this.beenPressed = beenPressed;
        this.points = points;
        this.startMsgFrame = startMsgFrame;
        this.endMsgFrame = endMsgFrame;
        this.beenReleased = beenReleased;
        this.leftScreen = leftScreen;
    }

    public boolean isLeftScreen() {
        return leftScreen;
    }
    public void setLeftScreen(boolean leftScreen) {
        this.leftScreen = leftScreen;
    }
    public boolean isBeenReleased() {
        return beenReleased;
    }
    public void setBeenReleased(boolean beenReleased) {
        this.beenReleased = beenReleased;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getStartMsgFrame() {
        return startMsgFrame;
    }
    public void setStartMsgFrame(int startMsgFrame) {
        this.startMsgFrame = startMsgFrame;
    }
    public int getEndMsgFrame() {
        return endMsgFrame;
    }
    public void setEndMsgFrame(int endMsgFrame) {
        this.endMsgFrame = endMsgFrame;
    }
    public int getNoteFrame() {
        return noteFrame;
    }
    public String getNoteType() {
        return noteType;
    }
    public String getNoteDirection() {
        return noteDirection;
    }
    public Point getNoteCoord() {
        return noteCoord;
    }
    public boolean isBeenPressed() {
        return beenPressed;
    }
    public void setPressed(boolean pressed) {
        this.beenPressed = pressed;
    }

    /* Methods */

    /* Once a note has been pressed for via input, must update its properties */
    public void updatePressedNote(int frameNum, int points, int MSG_FRAME) {
        /* Change the note.BeenPressed to true, capture frame which msg should print until and corresponding points */
        if (points != 0) {
            /* Pressed note was a normal note */
            if (this.getNoteType().equals("Normal") && !this.isBeenPressed()) {
                this.setPressed(true);
                this.setStartMsgFrame(frameNum);
                this.setEndMsgFrame(frameNum + MSG_FRAME);
                this.setPoints(points);
            }
            /* Input note was hold */
            else if (this.getNoteType().equals("Hold")) {
                /* input was first press */
                if (!this.isBeenPressed()) {
                    this.setPressed(true);
                    this.setStartMsgFrame(frameNum);
                    this.setEndMsgFrame(frameNum + MSG_FRAME);
                    this.setPoints(points);
                }
                else {
                    /* input was release */
                    this.setStartMsgFrame(frameNum);
                    this.setEndMsgFrame(frameNum + MSG_FRAME);
                    this.setPoints(points);
                    this.setBeenReleased(true);
                }
            }
        }
    }
    /* check whether a note has left a screen without being pressed and update its properties */
    public boolean checkLeftScreen(int frameNum, int MISS, int MSG_FRAME) {
        if (!this.isBeenPressed() && !this.isLeftScreen()) {
            if (this.getNoteType().equals("Normal") && this.getNoteCoord().y > Window.getHeight()) {
                this.setStartMsgFrame(frameNum);
                this.setEndMsgFrame(frameNum + MSG_FRAME);
                this.setPoints(MISS);
                this.setLeftScreen(true);
                return true;

            } else if (this.getNoteType().equals("Hold") && this.getNoteCoord().y > Window.getHeight() + 82) {
                this.setStartMsgFrame(frameNum);
                this.setEndMsgFrame(frameNum + MSG_FRAME);
                this.setPoints(MISS);
                this.setLeftScreen(true);
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "Note{" +
                "noteType='" + noteType + '\'' +
                ", noteDirection='" + noteDirection + '\'' +
                ", noteCoord=" + noteCoord +
                ", noteFrame=" + noteFrame +
                ", beenPressed=" + beenPressed +
                ", points=" + points +
                ", startMsgFrame=" + startMsgFrame +
                ", endMsgFrame=" + endMsgFrame +
                ", beenReleased=" + beenReleased +
                '}';
    }
}
