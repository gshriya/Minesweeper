import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;


/**
 * The class <b>GameController</b> is the controller of the game. It is a listener
 * of the view, and has a method <b>play</b> which computes the next
 * step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */


public class GameController implements ActionListener {

    private GameModel gameModel;
    private GameView gameView;
    private boolean firstClicked = true;
    private final String TAG = GameController.class.getSimpleName();

    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param width
     *            the width of the board on which the game will be played
     * @param height
     *            the height of the board on which the game will be played
     * @param numberOfMines
     *            the number of mines hidden in the board
     */
    public GameController(int width, int height, int numberOfMines) {
        gameModel = new GameModel(width, height, numberOfMines);
        gameView = new GameView(gameModel, this);
    }


    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof DotButton) {
            DotButton dot = (DotButton) e.getSource();
            play(dot.getColumn(), dot.getRow());
        } else {
            switch (e.getActionCommand()) {
                case GameView.QUIT:
                    System.exit(0);
                    break;
                case GameView.RESET:
                    reset();
                    break;
                default:
            }
        }
    }

    /**
     * resets the game
     */
    private void reset(){
        firstClicked = true;
        gameModel.reset();
        gameView.update();
    }

    /**
     * <b>play</b> is the method called when the user clicks on a square.
     * If that square is not already clicked, then it applies the logic
     * of the game to uncover that square, and possibly end the game if
     * that square was mined, or possibly uncover some other squares. 
     * It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     * @param width
     *            the selected column
     * @param heigth
     *            the selected line
     */
    private void play(int width, int heigth){
        DotInfo dot = gameModel.get(heigth, width);
        if(dot.hasBeenClicked()) return;

        dot.click();
        gameModel.step();

        if(firstClicked) {
            dot.uncover();
            addMines();
            firstClicked = false;
            clearZone(dot);
            gameView.update();
        } else if(dot.isMined()) {
            openAllMined();
            gameView.update();
            endDialog(0);
        } else {
            dot.uncover();
            clearZone(dot);
            gameView.update();
            if (gameModel.isFinished()) {
                endDialog(1);
            }
        }

    }

   /**
     * <b>clearZone</b> is the method that computes which new dots should be ``uncovered'' 
     * when a new square with no mine in its neighborood has been selected
     * @param initialDot
     *      the DotInfo object corresponding to the selected DotButton that
     * had zero neighbouring mines
     */
    private void clearZone(DotInfo initialDot) {
        GenericArrayStack<DotInfo> stack = new GenericArrayStack<DotInfo>(gameModel.getHeigth() * gameModel.getWidth());
        stack.push(initialDot);

        while (!stack.isEmpty()) {
            DotInfo dot = stack.pop();
            GenericArrayStack<DotInfo> neighbors = getNeighbors(dot);

            while (!neighbors.isEmpty()) {
                DotInfo n = neighbors.pop();
                if(n.isCovered() && !n.isMined()) {
                    n.uncover();
                    if(n.getNeighbooringMines() == 0) {
                        stack.push(n);
                    }
                }
            }
        }
    }

    private void endDialog(int i) {
        Object[] options = {"Play Again",
                "Quit"};
        int n;
        if(i == 0) {
            n = JOptionPane.showOptionDialog(gameView,
                    "Ouch you lost in " + gameModel.getNumberOfSteps()
                            + " steps!\n Would you like to play again?",
                    "Boom!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        } else {
            n = JOptionPane.showOptionDialog(gameView,
                    "Congratulations, you won in " + gameModel.getNumberOfSteps()
                            + " steps!\n Would you like to play again?",
                    "Boom!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        }
        if(n == 0){
            reset();
        } else{
            System.exit(0);
        }
    }

    private void addMines() {
        for (int i = 0; i < gameModel.getNumberOfMines(); i++) {
            mineDot();
        }

        for (int i = 0; i < gameModel.getHeigth(); i++) {
            for (int j = 0; j < gameModel.getWidth(); j++) {
                DotInfo dot = gameModel.get(i, j);
                dot.setNeighbooringMines(getNeighborMinesSize(dot));
            }
        }
    }

    private void mineDot() {
        Random rand = new Random();
        int i = rand.nextInt(gameModel.getHeigth());
        int j = rand.nextInt(gameModel.getWidth());
        DotInfo dot = gameModel.get(i, j);

        if(dot.isMined() || !dot.isCovered()){
            mineDot();
        } else {
            dot.setMined();
            //dot.uncover();
        }

    }

    private void openAllMined() {
        for (int i = 0; i < gameModel.getHeigth(); i++) {
            for (int j = 0; j < gameModel.getWidth(); j++) {
                DotInfo dot = gameModel.get(i, j);
                if(dot.isMined()) {
                    dot.uncover();
                }
            }
        }
    }

    private GenericArrayStack<DotInfo> getNeighbors(DotInfo dot) {
        GenericArrayStack<DotInfo> stack = new GenericArrayStack<DotInfo>(9);
        int x = dot.getX();
        int y = dot.getY();

        int x1 = x-1;
        int x2 = x+1;
        int y1 = y-1;
        int y2 = y+1;

        if(validateIndex(x1, y1)) {
            stack.push(gameModel.get(x1, y1));
        }
        if(validateIndex(x, y1)) {
            stack.push(gameModel.get(x, y1));
        }
        if(validateIndex(x2, y1)) {
            stack.push(gameModel.get(x2, y1));
        }
        if(validateIndex(x1, y)) {
            stack.push(gameModel.get(x1, y));
        }
        if(validateIndex(x2, y)) {
            stack.push(gameModel.get(x2, y));
        }
        if(validateIndex(x1, y2)) {
            stack.push(gameModel.get(x1, y2));
        }
        if(validateIndex(x, y2)) {
            stack.push(gameModel.get(x, y2));
        }
        if(validateIndex(x2, y2)) {
            stack.push(gameModel.get(x2, y2));
        }

        return stack;
    }

    private int getNeighborMinesSize(DotInfo dot) {
        int x = dot.getX();
        int y = dot.getY();
        int size = 0;

        int x1 = x-1;
        int x2 = x+1;
        int y1 = y-1;
        int y2 = y+1;

        if(validateIndex(x1, y1) && gameModel.get(x1, y1).isMined()) {
            size++;
        }
        if(validateIndex(x, y1) && gameModel.get(x, y1).isMined()) {
            size++;
        }
        if(validateIndex(x2, y1) && gameModel.get(x2, y1).isMined()) {
            size++;
        }
        if(validateIndex(x1, y) && gameModel.get(x1, y).isMined()) {
            size++;
        }
        if(validateIndex(x2, y) && gameModel.get(x2, y).isMined()) {
            size++;
        }
        if(validateIndex(x1, y2) && gameModel.get(x1, y2).isMined()) {
            size++;
        }
        if(validateIndex(x, y2) && gameModel.get(x, y2).isMined()) {
            size++;
        }
        if(validateIndex(x2, y2) && gameModel.get(x2, y2).isMined()) {
            size++;
        }

        return size;
    }

    private boolean validateIndex(int x, int y) {
        int h = gameModel.getHeigth();
        int w = gameModel.getWidth();

        if(x < 0 || x >= h){
            return false;
        } else if(y < 0 || y >= w) {
            return false;
        }
        return true;
    }

}
