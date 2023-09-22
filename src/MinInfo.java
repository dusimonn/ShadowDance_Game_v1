import bagel.Window;
/* MinInfo corresponds to the note that the input was for */
public class MinInfo {
    private final int minIndex;
    private final int minDist;

    /* Constructors and getters */
    public MinInfo(int minIndex, int minDist) {
        this.minIndex = minIndex;
        this.minDist = minDist;
    }
    public int getMinIndex() {
        return minIndex;
    }
    public int getMinDist() {
        return minDist;
    }

    /* finds the note the input was for, which will be the closest note */
    public MinInfo findMinNote(int worldLength, Note[] notes, String inputDir, String inputType, int NOTE_CENTRE) {
        int minIndex = 0;
        int minDist = Window.getHeight();
        for (int i = ShadowDance.NOTE_START; i < worldLength; i++) {
            int dist = -1;
            /* Only check notes which match the arrow input direction */
            if (notes[i].getNoteDirection().equals(inputDir)) {
                /* input note is normal */
                if (notes[i].getNoteType().equals(ShadowDance.NORMAL_NOTE) && !notes[i].isBeenPressed()) {
                    dist = distance((int)notes[i].getNoteCoord().y, NOTE_CENTRE);
                }
                /* input note is hold */
                else if (notes[i].getNoteType().equals(ShadowDance.HOLD_NOTE)) {
                    /* input corresponds to the start of the hold note */
                    if (inputType.equals(ShadowDance.PRESSED)) {
                        if (!notes[i].isBeenPressed()) {
                            dist = distance((int)notes[i].getNoteCoord().y + ShadowDance.HOLD_NOTE_LENGTH, NOTE_CENTRE);
                        }

                    }
                    /* input corresponds to the release of the hold note */
                    else {
                        /* (inputType.equals("Released") */
                        if (notes[i].isBeenPressed()) {
                            dist = distance((int)notes[i].getNoteCoord().y - ShadowDance.HOLD_NOTE_LENGTH, NOTE_CENTRE);
                        }

                    }
                }
                if (dist < minDist && dist != -1) {
                    minDist = dist;
                    minIndex = i;
                }
            }
        }

        return new MinInfo(minIndex, minDist);
    }

    /* Finds distance between 2 notes by comparing y values */
    private int distance(int noteY, int stationaryY) {
        return Math.abs(noteY - stationaryY);
    }
}
