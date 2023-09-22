import bagel.*;
import bagel.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @author
 * Du-Simon Nguyen; 1352062
 */
public class ShadowDance extends AbstractGame {
    /* Game constants */
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int LANE_Y = 384;
    private final static int NOTE_CENTRE = 657;
    private final static int NOTE_Y = 100;
    private final static int MAX_LANE_NOTE = 100;
    private final static int MAX_LANE_HOLD = 20;
    private final static int HOLD_Y = 24;
    private final static int NOTE_SPEED = 2;
    private final static int MSG_FRAME = 30;
    private final static int WIN_SCORE = 150;
    public final static int MISS_POINTS = -5;
    public final static int BAD_POINTS = -1;
    public final static int GOOD_POINTS = 5;
    public final static int PERFECT_POINTS = 10;
    public final static int MISS_DIST = 200;
    public final static int BAD_DIST = 100;
    public final static int GOOD_DIST = 50;
    public final static int PERFECT_DIST = 15;
    public final static int HOLD_NOTE_LENGTH = 82;

    /* Game variables */
    private boolean startGame = true;
    private int frameNum = 0;
    private int score = 0;

    /* Storage variables */
    private final String[][] worldInfo = new String[4 * (MAX_LANE_HOLD + MAX_LANE_NOTE)][3];
    /* Stores the csv file in a double string array */

    private final int worldLength;
    private final Note[] notes = new Note[4 * (MAX_LANE_HOLD + MAX_LANE_NOTE)];
    /* from worldInfo store each note in a note[] array */
    public final static int NOTE_START = 4;
    /* skip the first 4 lines in csv when dealing with notes */

    /* Drawing variables */
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Image LANE_DOWN = new Image("res/laneDown.png");
    private final Image LANE_UP = new Image("res/laneUp.png");
    private final Image LANE_RIGHT = new Image("res/laneRight.png");
    private final Image LANE_LEFT = new Image("res/laneLeft.png");
    private final Image NOTE_DOWN = new Image("res/noteDown.png");
    private final Image NOTE_UP = new Image("res/noteUp.png");
    private final Image NOTE_RIGHT = new Image("res/noteRight.png");
    private final Image NOTE_LEFT = new Image("res/noteLeft.png");
    private final Image HOLD_DOWN = new Image("res/holdNoteDown.png");
    private final Image HOLD_UP = new Image("res/holdNoteUp.png");
    private final Image HOLD_RIGHT = new Image("res/holdNoteRight.png");
    private final Image HOLD_LEFT = new Image("res/holdNoteLeft.png");
    private final Draw draw = new Draw(BACKGROUND_IMAGE, LANE_DOWN, LANE_UP, LANE_RIGHT, LANE_LEFT, NOTE_DOWN, NOTE_UP, NOTE_RIGHT, NOTE_LEFT, HOLD_DOWN, HOLD_UP, HOLD_RIGHT, HOLD_LEFT);

    /* Writing variables */
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final static Point scorePoint = new Point(35, 35);
    private final Point shadowDance = new Point(220, 250);
    private final Font bigFont = new Font("res/FSO8BITR.TTF", 64);
    private final Font smallFont = new Font("res/FSO8BITR.TTF", 24);
    private final Font scoreFont = new Font("res/FSO8BITR.TTF", 30);
    private final Font messageFont = new Font("res/FSO8BITR.TTF", 40);
    public final static String UP = "Up";
    public final static String DOWN = "Down";
    public final static String RIGHT = "Left";
    public final static String LEFT = "Right";
    public final static String PRESSED = "Pressed";
    public final static String RELEASED = "Released";
    public final static String NORMAL_NOTE = "Normal";
    public final static String HOLD_NOTE = "Hold";
    public final static String TRY_AGAIN = "TRY AGAIN";
    public final static String CLEAR = "CLEAR!";
    public final static String ARROW_PLAY = "USE ARROW KEYS TO PLAY";
    public final static String SPACE_START = "PRESS SPACE TO START";
    public final static String SCORE = "SCORE";
    public final static String PERFECT = "PERFECT";
    public final static String GOOD = "GOOD";
    public final static String BAD = "BAD";
    public final static String MISS = "MISS";

    private final Write write = new Write(GAME_TITLE, scorePoint, shadowDance, bigFont, smallFont, scoreFont, messageFont);

    public ShadowDance(String worldFile) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        worldLength = readCSV(worldFile);
        /* load in all the notes into an array, start from index 4 as 0-3 are lanes */
        storeNotes();
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    private int readCSV(String worldFile) {
        int length = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(worldFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                /* Store each csv field value into the array */
                worldInfo[length][0] = lineSplit[0]; /* lane type */
                worldInfo[length][1] = lineSplit[1]; /* type of note */
                worldInfo[length][2] = lineSplit[2]; /* x-coord/frame-number */
                length++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        String worldFile = "res/level1.csv";
        // String worldFile1 = "res/test1.csv";
        ShadowDance game = new ShadowDance(worldFile);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        /* Draw in the background */
        draw.background();

        /* Check if user quit the game */
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if (startGame) {
            /* Draw in the menu texts */
            write.menu();
            if (input.wasPressed(Keys.SPACE)) {
                startGame = false;
            }
        }
        else {
            /* Draw in the lanes */
            draw.lanes(worldInfo[0][2], worldInfo[1][2], worldInfo[2][2], worldInfo[3][2], LANE_Y);

            /* Draw in the notes */
            draw.allNotes(worldLength, notes, frameNum, NOTE_SPEED);

            /* Check if input was received */
            String inputDir = receivedInput(input);
            String inputType = receivedType(input);

            if (inputDir != null) {
                /* Check which note input was for */
                int minIndex = 0;
                int minDist = Window.getHeight();
                MinInfo minInfo = new MinInfo(minIndex, minDist);
                minInfo = minInfo.findMinNote(worldLength, notes, inputDir, inputType, NOTE_CENTRE);

                /* update the minNote and adjust score */
                minIndex = minInfo.getMinIndex();
                minDist = minInfo.getMinDist();
                int points = calcPoints(minDist);
                if (notes[minIndex] != null) {
                    notes[minIndex].updatePressedNote(frameNum, points, MSG_FRAME);
                }
                score += points;
            }
            else {
                /* if there was no input, need to check if a note has left the screen without being pressed */
                /* and output "MISS" if this is the case */
                for (int i = NOTE_START; i < worldLength; i++) {
                    boolean leftScreen = notes[i].checkLeftScreen(frameNum, MISS_POINTS, MSG_FRAME);
                    if (leftScreen) {
                        score += MISS_POINTS;
                    }
                }
            }

            /* Print the corresponding msg */
            write.scoreMsg(frameNum, worldLength, notes);
            write.score(score);
            frameNum++;

            /* if note has been pressed, remove it from the screen */
            draw.removeNote(worldLength, notes);
        }

        /* check whether game has ended by seeing if last note has left screen*/
        if (gameEnd()) {
            draw.background();
            if (score >= WIN_SCORE) {
                write.clear();
            } else {
                write.tryAgain();
            }
        }
    }
    /* store the notes information from csv file into note array */
    private void storeNotes() {
        for (int i = NOTE_START; i < worldLength; i++) {
            int noteFrame = Integer.parseInt(worldInfo[i][2]);
            String noteType = worldInfo[i][1];
            String noteDirection = worldInfo[i][0];
            Point noteCoord;
            if (noteType.equals(NORMAL_NOTE)) {
                noteCoord = new Point(getNoteX(noteDirection), NOTE_Y);
            } else {
                /* noteType.equals(HOLD_NOTE) */
                noteCoord = new Point(getNoteX(noteDirection), HOLD_Y);
            }
            Note note = new Note(noteType, noteDirection, noteCoord, noteFrame,
                    false, 0, 0,0,
                    false, false);
            notes[i] = note;
        }
    }
    /* get the corresponding x coord for each note in the csv file */
    private int getNoteX(String noteDirection) {
        switch (noteDirection) {
            case DOWN:
                return Integer.parseInt(worldInfo[3][2]);
            case UP:
                return Integer.parseInt(worldInfo[2][2]);
            case LEFT:
                return Integer.parseInt(worldInfo[0][2]);
            default:
                return Integer.parseInt(worldInfo[1][2]);
        }
    }
    /* Check whether the game state has ended */
    private boolean gameEnd() {
        /* game has ended if either last note has been pressed and released (if it is hold) or has left the screen */
        Note lastNote = notes[worldLength - 1];
        if (lastNote.getNoteType().equals(NORMAL_NOTE)) {
            if (lastNote.isBeenPressed()) {
                return true;
            } else {
                return lastNote.getNoteCoord().y > Window.getHeight();
            }
        }
        else {
            /* lastNote.getNoteType().equals("Hold") */
            if (lastNote.isBeenPressed() && lastNote.isBeenReleased()) {
                return true;
            } else {
                return lastNote.getNoteCoord().y - HOLD_NOTE_LENGTH > Window.getHeight();
            }
        }
    }
    /* Check whether input was a press or release */
    private String receivedType(Input input) {
        if (input.wasPressed(Keys.UP) || input.wasPressed(Keys.DOWN) ||
                input.wasPressed(Keys.LEFT) || input.wasPressed(Keys.RIGHT)) {
            return PRESSED;
        } else if (!input.isDown(Keys.UP) || !input.isDown(Keys.DOWN) ||
                !input.isDown(Keys.LEFT) || !input.isDown(Keys.RIGHT)) {
            return RELEASED;
        }  else {
            return null;
        }
    }
    /* check which key was pressed */
    private String receivedInput(Input input) {
        if (input.wasPressed(Keys.UP)) {
            return UP;
        } else if (input.wasPressed(Keys.DOWN)) {
            return DOWN;
        } else if ((input.wasPressed(Keys.LEFT))) {
            return LEFT;
        } else if (input.wasPressed(Keys.RIGHT)) {
            return RIGHT;
        } else {
            return null;
        }
    }
    /* calculate points by looking at the distance when input received */
    private int calcPoints(int dist) {
        int points;
        if (dist <= PERFECT_DIST) {
            points = PERFECT_POINTS;
        } else if (dist <= GOOD_DIST) {
            points = GOOD_POINTS;
        } else if (dist <= BAD_DIST) {
            points = BAD_POINTS;
        } else if (dist <= MISS_DIST){
            points = MISS_POINTS;
        } else {
            /* the input does not count, not close to any note */
            points = 0;
        }
        return points;
    }
}