import bagel.*;
import bagel.util.*;

public class Write {
    private final String GAME_TITLE;
    private final Point scorePoint;
    private final Point shadowDance;
    private final Font bigFont;
    private final Font smallFont;
    private final Font scoreFont;
    private final Font messageFont;

    /* Constructor */
    public Write(String GAME_TITLE, Point scorePoint, Point shadowDance, Font bigFont, Font smallFont, Font scoreFont, Font messageFont) {
        this.GAME_TITLE = GAME_TITLE;
        this.scorePoint = scorePoint;
        this.shadowDance = shadowDance;
        this.bigFont = bigFont;
        this.smallFont = smallFont;
        this.scoreFont = scoreFont;
        this.messageFont = messageFont;
    }
    /* Write the menu screen */
    public void menu() {
        bigFont.drawString(GAME_TITLE, shadowDance.x, shadowDance.y);
        smallFont.drawString(ShadowDance.SPACE_START, shadowDance.x + 100, shadowDance.y + 190);
        smallFont.drawString(ShadowDance.ARROW_PLAY, shadowDance.x + 100, shadowDance.y + 220);
    }
    /* Write the end screen when level is success */
    public void clear() {
        bigFont.drawString(ShadowDance.CLEAR,
                Window.getWidth()/2.0 - bigFont.getWidth(ShadowDance.CLEAR) / 2, Window.getHeight()/2.0);
    }
    /* Write the end screen when level is failed */
    public void tryAgain() {
        bigFont.drawString(ShadowDance.TRY_AGAIN,
                Window.getWidth()/2.0 - bigFont.getWidth(ShadowDance.TRY_AGAIN) / 2, Window.getHeight()/2.0);
    }
    /* Write the score value */
    public void score(int score) {
        scoreFont.drawString(ShadowDance.SCORE + " " + score, scorePoint.x, scorePoint.y);
    }
    /* Write the score message whenever note is pressed */
    public void scoreMsg(int frameNum, int worldLength, Note[] notes) {
        for (int i = ShadowDance.NOTE_START; i < worldLength; i++) {
            /* compare current frameNum to each note's starting frame input */
            if (frameNum <= notes[i].getEndMsgFrame() && frameNum >= notes[i].getStartMsgFrame()) {
                int points = notes[i].getPoints();
                msg(points);
            }
        }
    }
    private void msg(int points) {
        if (points == ShadowDance.PERFECT_POINTS) {
            messageFont.drawString(ShadowDance.PERFECT,
                    Window.getWidth()/2.0 - messageFont.getWidth("PERFECT")/2, Window.getHeight()/2.0);
        } else if (points == ShadowDance.GOOD_POINTS) {
            messageFont.drawString(ShadowDance.GOOD,
                    Window.getWidth()/2.0 - messageFont.getWidth("GOOD")/2, Window.getHeight()/2.0);
        } else if (points == ShadowDance.BAD_POINTS) {
            messageFont.drawString(ShadowDance.BAD,
                    Window.getWidth()/2.0 - messageFont.getWidth("BAD")/2, Window.getHeight()/2.0);
        } else if (points == ShadowDance.MISS_POINTS) {
            messageFont.drawString(ShadowDance.MISS,
                    Window.getWidth()/2.0 - messageFont.getWidth("MISS")/2, Window.getHeight()/2.0);
        }
    }
}
