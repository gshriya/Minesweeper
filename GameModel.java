import java.util.Random;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the following information:
 * - the state of all the ``dots'' on the board (mined or not, clicked
 * or not, number of neighbooring mines...)
 * - the size of the board
 * - the number of steps since the last reset
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class GameModel {

    private Random random;
    private int widthOfGame;
    private int heigthOfGame;
    private int numberOfMines;
    private DotInfo[][] model;
    private int numberUncovered;
    private int numberOfSteps;
    private String TAG = GameModel.class.getSimpleName();

    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param width
     *            the width of the board
     * 
     * @param heigth
     *            the heigth of the board
     * 
     * @param numberOfMines
     *            the number of mines to hide in the board
     */
    public GameModel(int width, int heigth, int numberOfMines) {
        this.random = new Random();
        this.widthOfGame = width;
        this.heigthOfGame = heigth;
        this.numberOfMines = numberOfMines;
        this.model = new DotInfo[heigth][width];
        this.numberUncovered = 0;
        this.numberOfSteps = 0;
        initButtons();
    }

    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){
      this.model = new DotInfo[this.heigthOfGame][this.widthOfGame];
      this.numberOfSteps = 0;
      this.numberUncovered = 0;
      initButtons();
    }

    /**
     * Getter method for the heigth of the game
     * 
     * @return the value of the attribute heigthOfGame
     */   
    public int getHeigth(){
        return this.heigthOfGame;
    }

    /**
     * Getter method for the width of the game
     * 
     * @return the value of the attribute widthOfGame
     */   
    public int getWidth(){
        return this.widthOfGame;
    }

    /**
     * returns true if the dot at location (i,j) is mined, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isMined(int i, int j){
        return this.model[i][j].isMined();
    }

    /**
     * returns true if the dot  at location (i,j) has 
     * been clicked, false otherwise
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean hasBeenClicked(int i, int j){
        return this.model[i][j].hasBeenClicked();
    }

  /**
     * returns true if the dot  at location (i,j) has zero mined 
     * neighboor, false otherwise
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isBlank(int i, int j){
        
      if(this.model[i][j].getNeighbooringMines() == 0){
        return true;
      }

      return false;

    }

    /**
     * returns true if the dot is covered, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isCovered(int i, int j){
        
      return this.model[i][j].isCovered();

    }

    /**
     * returns the number of neighbooring mines os the dot  
     * at location (i,j)
     *
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the number of neighbooring mines at location (i,j)
     */   
    public int getNeighbooringMines(int i, int j){
      return get(i, j).getNeighbooringMines();
    }


    /**
     * Sets the status of the dot at location (i,j) to uncovered
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void uncover(int i, int j){
      this.model[i][j].uncover();
      this.numberUncovered++;
    }

    /**
     * Sets the status of the dot at location (i,j) to clicked
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void click(int i, int j){
        if (!get(i, j).hasBeenClicked()){
          get(i, j).click();
          step();
        }
    }

    /**
     * Uncover all remaining covered dot
     */   
    public void uncoverAll(){
        for (int i = 0; i < heigthOfGame; i++){
            for (int j = 0; j < widthOfGame; j++) {
                get(i, j).uncover();
            }
        }
    }

    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
        return this.numberOfSteps;
    }

    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {
      return this.model[i][j];
    }

   /**
     * The metod <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new square.
     */
   public void step(){
        this.numberOfSteps++;
    }

    public int getNumberOfMines() {
         return this.numberOfMines;
    }
 
   /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the nonmined dots are uncovered.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                DotInfo dot = get(i, j);
                if (!dot.isMined() && dot.isCovered()) {
                    return false;
                }
            }
        }
        return true;
    }

   /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        
     return "";

    }

    private void initButtons() {
        for (int i = 0; i < getHeigth(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                model[i][j] = new DotInfo(i, j);
            }
        }
    }
}
