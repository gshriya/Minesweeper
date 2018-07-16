import javax.swing.*;
import java.awt.*;
import static java.awt.BorderLayout.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out a matrix of <b>DotButton</b> (the actual game) and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class GameView extends JFrame {

     private DotButton[][] dots;
     private GameModel gameModel;
     private GameController gameController;
     private JLabel steps;
     private JButton reset, quit;
     private JPanel panel, controls, mineBoard;
     private final String TAG = GameView.class.getSimpleName();
     static final String QUIT = "QUIT";
     static final String RESET = "RESET";

    /**
     * Constructor used for initializing the Frame
     * 
     * @param gameModel
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel gameModel, GameController gameController) {
        this.gameModel = gameModel;
        this.gameController = gameController;
        this.dots = new DotButton[gameModel.getWidth()][gameModel.getHeigth()];

        this.setTitle("MineSweeper It -- the ITI 1121 version");       

        setSize(800,800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        steps = new JLabel("Number of steps: 0");

        reset = new JButton("RESET");
        reset.setActionCommand(RESET);
        reset.addActionListener(gameController);

        quit = new JButton("QUIT");
        quit.setActionCommand(QUIT);
        quit.addActionListener(gameController);

        //panel = new JPanel(new GridLayout(400, 400));
        controls = new JPanel();
        mineBoard = new JPanel(new GridLayout(600, 600));

        controls.add(steps, CENTER);
        controls.add(reset, WEST);
        controls.add(quit,EAST);

        update();

        add(mineBoard, CENTER);
        add(controls, SOUTH);

        pack();
        setVisible(true);

    }

    /**
     * update the status of the board's DotButton instances based 
     * on the current game model, then redraws the view
     */

    public void update(){
        int rSize = gameModel.getHeigth();
        int cSize = gameModel.getWidth();

        remove(mineBoard);
        mineBoard = new JPanel(new GridLayout(gameModel.getHeigth(), gameModel.getNumberOfSteps()));

        for (int i  = 0; i < rSize; i++) {
            for (int j = 0; j < cSize; j++) {
                DotButton dot = new DotButton(j, i, getIcon(i, j));
                dot.addActionListener(gameController);
                mineBoard.add(dot);
            }
        }
        add(mineBoard, CENTER);
        revalidate();
        steps.setText("Number of steps: " + gameModel.getNumberOfSteps());
    }

    /**
     * returns the icon value that must be used for a given dot 
     * in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the icon to use for the dot at location (i,j)
     */   
    private int getIcon(int i, int j){
        
     //ADD YOU CODE HERE
        DotInfo info = gameModel.get(i, j);
        int neighbors = info.getNeighbooringMines();


        if(info.hasBeenClicked() && info.isMined()) {
            return DotButton.CLICKED_MINE;
        } else if(info.isMined() && !info.isCovered()) {
            return DotButton.MINED;
        } else if(!info.isCovered()) {
            switch (neighbors) {
                case 1:
                    return DotButton.ONE_NEIGHBOURS;
                case 2:
                    return DotButton.TWO_NEIGHBOURS;
                case 3:
                    return DotButton.THREE_NEIGHBOURS;
                case 4:
                    return DotButton.FOUR_NEIGHBOURS;
                case 5:
                    return DotButton.FIVE_NEIGHBOURS;
                case 6:
                    return DotButton.SIX_NEIGHBOURS;
                case 7:
                    return DotButton.SEVEN_NEIGHBOURS;
                case 8:
                    return DotButton.EIGHT_NEIGHBOURS;
                default:
                    return DotButton.ZERO_NEIGHBOURS;
            }
        }
        return DotButton.COVERED;
    }


}
