package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Piece Class. It is an abstract class from which all the actual
 * pieces are inherited. It defines all the function common to all the pieces
 * The move() function an abstract function that has to be overridden in all the
 * inherited class It implements Cloneable interface as a copy of the piece is
 * required very often
 */
public abstract class Piece implements Cloneable {

    // Member Variables
    private int color;
    private String id = null;
    private String path;
    protected ArrayList<Cell> possiblemoves = new ArrayList<Cell>(); // Protected (access from child classes)
    private int x, y;

    private int moveCount = 0;
    
    public abstract ArrayList<Cell> move(Cell pos[][], int x, int y); // Abstract Function. Must be overridden
    
    public ArrayList<Cell> move(Cell pos[][]){
    	return move(pos, this.x, this.y);
    }

    // Id Setter
    public void setId(String id) {
        this.id = id;
    }

    // Path Setter
    public void setPath(String path) {
        this.path = path;
    }

    // Color Setter
    public void setColor(int c) {
        this.color = c;
    }

    // Path getter
    public String getPath() {
        return path;
    }

    // Id getter
    public String getId() {
        return id;
    }

    // Color Getter
    public int getcolor() {
        return this.color;
    }
    
    // general value access functions
    public void setx(int x) {
        this.x = x;
    }

    public void sety(int y) {
        this.y = y;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    // Function to return the a "shallow" copy of the object. The copy has exact
    // same variable value but different reference
    public Piece getcopy() throws CloneNotSupportedException {
        return (Piece) this.clone();
    }

	public void incrementMoveCount() {
		moveCount++;
	}
	
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public String getPieceType() {
		return id.substring(0, 2);
	}
}