
/**
 * The class <b>DotInfo</b> is a simple helper class to store 
 * the state (e.g. clicked, mined, number of neighbooring mines...) 
 * at the dot position (x,y)
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class DotInfo {

    
    private boolean covered;
    private boolean mined = false;
    private int neighbooringMines;
    private boolean wasClicked;
    private int x;
    private int y;
    /**
     * Constructor, used to initialize the instance variables
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public DotInfo(int x, int y){
        this.x = x;
        this.y = y;
        this.covered = true;
        this.mined = false;
        this.neighbooringMines = 0;
        this.wasClicked = false;
    }

    /**
     * Getter method for the attribute x.
     * 
     * @return the value of the attribute x
     */
    public int getX(){
        return this.x;
    }
    
    /**
     * Getter method for the attribute y.
     * 
     * @return the value of the attribute y
     */
    public int getY(){
        return this.y;
    }
    
 
    /**
     * Setter for mined
     */
    public void setMined() {
        this.mined = true;
    }

    /**
     * Getter for mined
     *
     * @return mined
     */
    public boolean isMined() {
        return this.mined;
    }


    /**
     * Setter for covered
     */
    public void uncover() {
        this.covered = false;
    }

    /**
     * Getter for covered
     *
     * @return covered
     */
    public boolean isCovered(){
        return this.covered;
    }



    /**
     * Setter for wasClicked
     */
    public void click() {
        this.wasClicked = true;
    }


    /**
     * Getter for wasClicked
     *
     * @return wasClicked
     */
    public boolean hasBeenClicked() {
        return this.wasClicked;
    }


    /**
     * Setter for neighbooringMines
     *
     * @param neighbooringMines
     *          number of neighbooring mines
     */
    public void setNeighbooringMines(int neighbooringMines) {
        this.neighbooringMines = neighbooringMines;
    }

    /**
     * Get for neighbooringMines
     *
     * @return neighbooringMines
     */
    public int getNeighbooringMines() {
        return this.neighbooringMines;
    }

    @Override
    public String toString() {
        return "DotInfo(covered: " + covered + ", mined: " + mined + ", neighbooringMines: " + neighbooringMines
                + ", wasCliked: " + wasClicked + ", x: " + x + ", y: " + y + ")";
    }
}
