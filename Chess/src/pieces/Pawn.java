package pieces;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 */
public class Pawn extends Piece {

    // Constructors
    public Pawn(String i, String p, int c) {
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
        } else { // Black pawn
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
        // Open a dialog to choose the piece for promotion
        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        String choice = (String) JOptionPane.showInputDialog(null, "Promote Pawn to:", "Pawn Promotion", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Create the new piece based on the user's choice
        Piece promotedPiece = null;
        if (choice != null) {
            switch (choice) {
                case "Queen":
                    promotedPiece = new Queen(getId(), getPath(), getcolor());
                    break;
                case "Rook":
                    promotedPiece = new Rook(getId(), getPath(), getcolor());
                    break;
                case "Bishop":
                    promotedPiece = new Bishop(getId(), getPath(), getcolor());
                    break;
                case "Knight":
                    promotedPiece = new Knight(getId(), getPath(), getcolor());
                    break;
            }
        }
        return promotedPiece;
    }
}
