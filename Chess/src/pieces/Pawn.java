package pieces;

import java.util.ArrayList;
import javax.swing.JOptionPane;

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
            if (x == 0)
                return possiblemoves;
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
        } else {
            if (x == 8)
                return possiblemoves;
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

        // Check for promotion if the pawn reaches the last rank
        if ((getcolor() == 0 && x == 0) || (getcolor() == 1 && x == 7)) {
            Piece promotedPiece = promote();
            if (promotedPiece != null) {
                possiblemoves.clear(); // Clear possible moves to avoid confusion
                possiblemoves.add(new Cell(x, y, promotedPiece));
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
                		promotedPiece = new Queen(getId(), "White_Queen.png", getcolor());
                	}
                	else {
                		promotedPiece = new Queen(getId(), "Black_Queen.png", getcolor());
                	}
                    break;
                case "Rook":
                	if(this.getcolor() == 0){
                		promotedPiece = new Rook(getId(), "White_Rook.png", getcolor());
                	}
                	else {
                		promotedPiece = new Rook(getId(), "Black_Rook.png", getcolor());
                	}
                    break;
                case "Bishop":
                	if(this.getcolor() == 0){
                		promotedPiece = new Bishop(getId(), "White_Bishop.png", getcolor());
                	}
                	else {
                		promotedPiece = new Bishop(getId(), "Black_Bishop.png", getcolor());
                	}
                    break;
                case "Knight":
                	if(this.getcolor() == 0){
                		promotedPiece = new Knight (getId(), "White_Knight.png", getcolor());
                	}
                	else {
                		promotedPiece = new Knight(getId(), "Black_Knight.png", getcolor());
                	}
                    break;
            }
        }
        return promotedPiece;
    public boolean getJustSkipped() {
        return this.justSkipped;
    }
    
    public void setJustSkipped(boolean justSkipped) {
        this.justSkipped = justSkipped;
    }
}
