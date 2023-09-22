import bagel.*;
import bagel.util.*;
public class Draw {
    private final Image BACKGROUND_IMAGE;
    private final Image LANE_DOWN;
    private final Image LANE_UP;
    private final Image LANE_RIGHT;
    private final Image LANE_LEFT;
    private final Image NOTE_DOWN;
    private final Image NOTE_UP;
    private final Image NOTE_RIGHT;
    private final Image NOTE_LEFT;
    private final Image HOLD_DOWN;
    private final Image HOLD_UP;
    private final Image HOLD_RIGHT;
    private final Image HOLD_LEFT;

    /* Drawing constructor */
    public Draw(Image BACKGROUND_IMAGE, Image LANE_DOWN, Image LANE_UP, Image LANE_RIGHT,
                Image LANE_LEFT, Image NOTE_DOWN, Image NOTE_UP, Image NOTE_RIGHT, Image NOTE_LEFT,
                Image HOLD_DOWN, Image HOLD_UP, Image HOLD_RIGHT, Image HOLD_LEFT) {
        this.BACKGROUND_IMAGE = BACKGROUND_IMAGE;
        this.LANE_DOWN = LANE_DOWN;
        this.LANE_UP = LANE_UP;
        this.LANE_RIGHT = LANE_RIGHT;
        this.LANE_LEFT = LANE_LEFT;
        this.NOTE_DOWN = NOTE_DOWN;
        this.NOTE_UP = NOTE_UP;
        this.NOTE_RIGHT = NOTE_RIGHT;
        this.NOTE_LEFT = NOTE_LEFT;
        this.HOLD_DOWN = HOLD_DOWN;
        this.HOLD_UP = HOLD_UP;
        this.HOLD_RIGHT = HOLD_RIGHT;
        this.HOLD_LEFT = HOLD_LEFT;
    }

    /* Methods */
    /* Draw the background */
    public void background() {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }
    /* Draw the lanes */
    public void lanes(String leftX, String rightX, String upX, String downX, int y) {
        LANE_LEFT.draw(Integer.parseInt(leftX), y);
        LANE_RIGHT.draw(Integer.parseInt(rightX), y);
        LANE_UP.draw(Integer.parseInt(upX), y);
        LANE_DOWN.draw(Integer.parseInt(downX), y);
    }
    /* Draw a pressed not off-screen */
    public void removeNote(int worldLength, Note[] notes) {
        for (int i = 4; i < worldLength; i++) {
            if (notes[i].isBeenPressed() && notes[i].getNoteType().equals(ShadowDance.NORMAL_NOTE)) {
                notes[i] = note(notes[i], Window.getWidth(), Window.getWidth(), 0);
            }
        }
    }
    /* Draw the notes */
    public void allNotes(int worldLength, Note[] notes, int frameNum, int noteSpeed) {
        for (int i = 4; i < worldLength; i++) {
            if (notes[i].getNoteFrame() <= frameNum) {
                /* Only draw notes that have not yet been pressed for */
                if ((notes[i].getNoteType().equals(ShadowDance.NORMAL_NOTE) && !notes[i].isBeenPressed() ||
                        (notes[i].getNoteType().equals(ShadowDance.HOLD_NOTE)))) {
                    notes[i] = note(notes[i], (int) notes[i].getNoteCoord().x, (int) notes[i].getNoteCoord().y, noteSpeed);
                }
            }
        }

    }
    private Note note(Note note, int x, int y, int noteSpeed) {
        if (note.getNoteType().equals(ShadowDance.NORMAL_NOTE)) {
            switch (note.getNoteDirection()) {
                case ShadowDance.DOWN:
                    NOTE_DOWN.draw(x, y);
                    break;
                case ShadowDance.UP:
                    NOTE_UP.draw(x, y);
                    break;
                case ShadowDance.LEFT:
                    NOTE_LEFT.draw(x, y);
                    break;
                default:
                    NOTE_RIGHT.draw(x, y);
                    break;
            }
        } else {
            switch (note.getNoteDirection()) {
                case ShadowDance.DOWN:
                    HOLD_DOWN.draw(x, y);
                    break;
                case ShadowDance.UP:
                    HOLD_UP.draw(x, y);
                    break;
                case ShadowDance.LEFT:
                    HOLD_LEFT.draw(x, y);
                    break;
                default:
                    HOLD_RIGHT.draw(x, y);
                    break;
            }
        }

        /* Update the new coordinates of the note for movement */
        Point newCoord = new Point(note.getNoteCoord().x, note.getNoteCoord().y + noteSpeed);
        note = new Note(note.getNoteType(), note.getNoteDirection(),
                newCoord, note.getNoteFrame(), note.isBeenPressed(), note.getPoints(),
                note.getStartMsgFrame(), note.getEndMsgFrame(), note.isBeenReleased(), note.isLeftScreen());
        return note;
    }

}
