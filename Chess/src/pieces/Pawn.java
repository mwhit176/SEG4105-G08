package pieces;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import pieces.Queen;
import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 */
public class Pawn extends Piece {
    
    private boolean justSkipped = false;

    // COnstructors
    public Pawn(String i, String p, int c, int x, int y) {
        setx(x);
        sety(y);
        setId(i);
        setPath(p);
        setColor(c);
    }

    // Move Function Overridden
    public ArrayList<Cell> move(Cell state[][], int x, int y) {
        // Pawn can move only one step except the first chance when it may move 2 steps
        // It can move in a diagonal fashion only for attacking a piece of opposite
        // color. It cannot move backward or move forward to attack a piece

        possiblemoves.clear();
        
        if (getcolor() == 0) { // White pawn
            if (x == 0) {
            	Piece promotedPiece = promote(); //Promote white pawn if it makes it to the other side
                if (promotedPiece != null) {
                    possiblemoves.clear();
                    Cell promotionCell = new Cell(promotedPiece.getx(), promotedPiece.gety(), promotedPiece);
                    promotionCell.setPiece(promotedPiece); // Set promoted piece only if not null
                    possiblemoves.add(promotionCell);
                }
            	return possiblemoves;
            }
            if (state[x - 1][y].getpiece() == null) {
                possiblemoves.add(state[x - 1][y]);
                if (x == 6 && state[4][y].getpiece() == null)
                    possiblemoves.add(state[4][y]);
            }
            if ((y > 0) && (state[x - 1][y - 1].getpiece() != null) && (state[x - 1][y - 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x - 1][y - 1]);
            if ((y < 7) && (state[x - 1][y + 1].getpiece() != null) && (state[x - 1][y + 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x - 1][y + 1]);
            // En Passant White Left
            if ((x == 3) && (y > 0) && (state[x][y - 1].getpiece() != null)
                    && (state[x][y - 1].getpiece().getcolor() != this.getcolor())
                    && (state[x][y - 1].getpiece() instanceof Pawn)
                    && ((Pawn)state[x][y - 1].getpiece()).getJustSkipped()) {
                possiblemoves.add(state[x - 1][y - 1]);
            }
            // En Passant White Right
            if ((x == 3) && (y < 7) && (state[x][y + 1].getpiece() != null)
                    && (state[x][y + 1].getpiece().getcolor() != this.getcolor())
                    && (state[x][y + 1].getpiece() instanceof Pawn)
                    && ((Pawn)state[x][y + 1].getpiece()).getJustSkipped()) {
                possiblemoves.add(state[x - 1][y + 1]);
            }
        } else { //black pawn
            if (x == 7) {
            	Piece promotedPiece = promote(); //Promote Black Pawn if it makes it to the other side
                if (promotedPiece != null) {
                    possiblemoves.clear();
                    Cell promotionCell = new Cell(promotedPiece.getx(), promotedPiece.gety(), promotedPiece);
                    promotionCell.setPiece(promotedPiece); // Set promoted piece only if not null
                    possiblemoves.add(promotionCell);
                }
                return possiblemoves;
            }
            if (state[x + 1][y].getpiece() == null) {
                possiblemoves.add(state[x + 1][y]);
                if (x == 1 && state[3][y].getpiece() == null)
                    possiblemoves.add(state[3][y]);
            }
            if ((y > 0) && (state[x + 1][y - 1].getpiece() != null) && (state[x + 1][y - 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x + 1][y - 1]);
            if ((y < 7) && (state[x + 1][y + 1].getpiece() != null) && (state[x + 1][y + 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x + 1][y + 1]);
            // En Passant Black Left
            if ((x == 4) && (y > 0) && (state[x][y - 1].getpiece() != null)
                    && (state[x][y - 1].getpiece().getcolor() != this.getcolor())
                    && (state[x][y - 1].getpiece() instanceof Pawn)
                    && ((Pawn)state[x][y - 1].getpiece()).getJustSkipped()) {
                possiblemoves.add(state[x + 1][y - 1]);
            }
            // En Passant Black Right
            if ((x == 4) && (y < 7) && (state[x][y + 1].getpiece() != null)
                    && (state[x][y + 1].getpiece().getcolor() != this.getcolor())
                    && (state[x][y + 1].getpiece() instanceof Pawn)
                    && ((Pawn)state[x][y + 1].getpiece()).getJustSkipped()) {
                possiblemoves.add(state[x + 1][y + 1]);
            }
        }

        return possiblemoves;
    }
    public Piece promote() {
        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        String choice = (String) JOptionPane.showInputDialog(null, 
                "Promote Pawn to:", 
                "Pawn Promotion", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);

        Piece promotedPiece = null;
        if (choice != null) {
            switch (choice) {
                case "Queen":
                	if(this.getcolor() == 0){
                		promotedPiece = new Queen(getId(), "White_Queen.png", getcolor(), getx(), gety());
                	}
                	else {
                		promotedPiece = new Queen(getId(), "Black_Queen.png", getcolor(),  getx(), gety());
                	}
                    break;
                case "Rook":
                	if(this.getcolor() == 0){
                		promotedPiece = new Rook(getId(), "White_Rook.png", getcolor(),  getx(), gety());
                	}
                	else {
                		promotedPiece = new Rook(getId(), "Black_Rook.png", getcolor(),  getx(), gety());
                	}
                    break;
                case "Bishop":
                	if(this.getcolor() == 0){
                		promotedPiece = new Bishop(getId(), "White_Bishop.png", getcolor(), getx(), gety());
                	}
                	else {
                		promotedPiece = new Bishop(getId(), "Black_Bishop.png", getcolor(),  getx(), gety());
                	}
                    break;
                case "Knight":
                	if(this.getcolor() == 0){
                		promotedPiece = new Knight (getId(), "White_Knight.png", getcolor(),  getx(), gety());
                	}
                	else {
                		promotedPiece = new Knight(getId(), "Black_Knight.png", getcolor(),  getx(), gety());
                	}
                    break;
            }
        }
        return promotedPiece;
    }
    public boolean getJustSkipped() {
        return this.justSkipped;
    }
    
    public void setJustSkipped(boolean justSkipped) {
        this.justSkipped = justSkipped;
    }
}
