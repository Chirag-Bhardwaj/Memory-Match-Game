//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    P02 Matching Game
// Course:   CS 300 Spring 2021
//
// Author:   Chirag Bhardwaj
// Email:    cbhardwaj2@wisc.edu
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
//
//
//
///////////////////////////////////////////////////////////////////////////////

import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;

/**
 * This class contains the entire program for a memory based card matching game.
 *
 * @author chiragbhardwaj
 */
public class MemoryGame {

    // Congratulations message
    private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
    // Cards not matched message
    private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
    // Cards matched message
    private final static String MATCHED = "CARDS MATCHED! Good Job!";
    // 2D-array which stores cards coordinates on the window display
    private final static float[][] CARDS_COORDINATES =
        new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170}, {170, 324}, {324, 324},
            {478, 324}, {632, 324}, {170, 478}, {324, 478}, {478, 478}, {632, 478}};
    // Array that stores the card images filenames
    private final static String[] CARD_IMAGES_NAMES =
        new String[] {"ball.png", "redFlower.png", "yellowFlower.png", "apple.png", "peach.png",
            "shark.png"};

    private static PApplet processing; // PApplet object that represents
    // the graphic display window
    private static Card[] cards; // one dimensional array of cards
    private static PImage[] images; // array of images of the different cards
    private static Card selectedCard1; // First selected card
    private static Card selectedCard2; // Second selected card
    private static boolean winner; // boolean evaluated true if the game is won,
    // and false otherwise
    private static int matchedCardsCount; // number of cards matched so far
    // in one session of the game
    private static String message; // Displayed message to the display window

    /**
     * This is the main method. It is used to start the application.
     *
     * @param args unused.
     */
    public static void main(String[] args) {

        Utility.startApplication(); // starts the application
    }

    /**
     * Defines the initial environment properties of this game as the program starts
     */
    public static void setup(PApplet processing) {

        MemoryGame.processing = processing;

        MemoryGame.images = new PImage[CARD_IMAGES_NAMES.length];
        //loading images into the images array.
        images[0] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[0]);
        images[1] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[1]);
        images[2] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[2]);
        images[3] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[3]);
        images[4] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[4]);
        images[5] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[5]);

        startNewGame(); //Starts new game.
    }

    /**
     * Initializes the Game.
     */
    public static void startNewGame() {

        //Initialising the static fields.
        MemoryGame.selectedCard1 = null;
        MemoryGame.selectedCard2 = null;
        MemoryGame.matchedCardsCount = 0;
        MemoryGame.winner = false;
        MemoryGame.message = "";

        // Shuffles the cards so that a different set of cards appear to the screen each time a
        // new game is started.
        MemoryGame.cards = new Card[CARDS_COORDINATES.length];
        int[] mixedUp = Utility.shuffleCards(cards.length);
        for (int i = 0; i < cards.length; ++i) {
            cards[i] =
                new Card(images[mixedUp[i]], CARDS_COORDINATES[i][0], CARDS_COORDINATES[i][1]);
        }

    }

    /**
     * Callback method called each time the user presses a key
     */
    public static void keyPressed() {

        // Start a new game each time user presses 'n' or 'N' key.
        if (processing.key == 'n' || processing.key == 'N') {
            startNewGame();
        }
    }

    /**
     * Callback method draws continuously this application window display
     */
    public static void draw() {

        // Set the color used for the background of the Processing window
        processing.background(245, 255, 250); // Mint cream color

        // Draws the cards to the screen.
        for (int j = 0; j < cards.length; ++j) {
            cards[j].draw();
        }

        displayMessage(message);
    }

    /**
     * Displays a given message to the display window
     *
     * @param message to be displayed to the display window
     */
    public static void displayMessage(String message) {

        processing.fill(0);
        processing.textSize(20);
        processing.text(message, processing.width / 2, 50);
        processing.textSize(12);
    }

    /**
     * Checks whether the mouse is over a given Card
     *
     * @return true if the mouse is over the storage list, false otherwise
     */
    public static boolean isMouseOver(Card card) {

        // Check if mouse if over the card
        if (processing.mouseX <= card.getX() + card.getWidth() / 2
            && processing.mouseX >= card.getX() - card.getWidth() / 2
            && processing.mouseY <= card.getY() + card.getHeight() / 2
            && processing.mouseY >= card.getY() - card.getHeight() / 2) {
            return true;
        }
        return false;
    }

    /**
     * Callback method called each time the user presses the mouse
     */
    public static void mousePressed() {

        // If two non-matching cards have been selected they are turned over in the next click
        // (on any card) by this.
        if (selectedCard1 != null && selectedCard2 != null) {
            selectedCard1.setVisible(false);
            selectedCard2.setVisible(false);

            selectedCard1.deselect();
            selectedCard2.deselect();

            selectedCard1 = null;
            selectedCard2 = null;
        }

        // Populates the selectedCard1 and selectedCards2. Also resets the message to empty and
        // sets selected cards to visible.
        if (selectedCard1 == null || selectedCard2 == null) {
            for (int i = 0; i < cards.length; ++i) {
                if (isMouseOver(cards[i]) && !cards[i].isVisible()) {
                    message = "";
                    cards[i].select();
                    cards[i].setVisible(true);
                    if (selectedCard1 == null) {
                        selectedCard1 = cards[i];
                    } else {
                        selectedCard2 = cards[i];
                    }
                }
            }
        }

        if (selectedCard1 != null && selectedCard2 != null) {
            if (matchingCards(selectedCard1, selectedCard2)) {
                // If cards match.
                selectedCard1.setVisible(true);
                selectedCard1.deselect();

                selectedCard2.setVisible(true);
                selectedCard2.deselect();

                selectedCard1.setMatched(true);
                selectedCard2.setMatched(true);

                selectedCard1 = null;
                selectedCard2 = null;

                message = MATCHED;

                matchedCardsCount = matchedCardsCount + 1;
            } else if (!matchingCards(selectedCard1, selectedCard2)) {
                // If cards do not match.
                selectedCard1.setMatched(false);
                selectedCard2.setMatched(false);

                message = NOT_MATCHED;
            }
        }

        // If the user wins. For example if the number of cards is 12 the no. of matched pairs
        // required to win would be 6.
        if (matchedCardsCount == cards.length / 2) {
            winner = true;
            message = CONGRA_MSG;
        }
    }

    /**
     * Checks whether two cards match or not
     *
     * @param card1 reference to the first card
     * @param card2 reference to the second card
     * @return true if card1 and card2 image references are the same, false otherwise
     */
    public static boolean matchingCards(Card card1, Card card2) {

        if (card1.getImage() == card2.getImage()) {
            return true;
        }
        return false;
    }
}
