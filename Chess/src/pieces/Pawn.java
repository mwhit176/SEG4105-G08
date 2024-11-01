package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 *
 */
public class Pawn extends Piece {
    
    private boolean justSkipped = false;

    // COnstructors
    public Pawn(String i, String p, int c) {
        setId(i);
        setPath(p);
        setColor(c);
    }

    // Move Function Overridden
    public ArrayList<Cell> move(Cell state[][], int x, int y) {
        // Pawn can move only one step except the first chance when it may move 2 steps
        // It can move in a diagonal fashion only for attacking a piece of opposite
        // color
        // It cannot move backward or move forward to attact a piece

        possiblemoves.clear();
        if (getcolor() == 0) {
            if (x == 0)
                return possiblemoves;
            if (state[x - 1][y].getpiece() == null) {
                possiblemoves.add(state[x - 1][y]);
                if (x == 6) {
                    if (state[4][y].getpiece() == null)
                        possiblemoves.add(state[4][y]);
                }
            }
            if ((y > 0) && (state[x - 1][y - 1].getpiece() != null)
                    && (state[x - 1][y - 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x - 1][y - 1]);
            if ((y < 7) && (state[x - 1][y + 1].getpiece() != null)
                    && (state[x - 1][y + 1].getpiece().getcolor() != this.getcolor()))
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
                if (x == 1) {
                    if (state[3][y].getpiece() == null)
                        possiblemoves.add(state[3][y]);
                }
            }
            if ((y > 0) && (state[x + 1][y - 1].getpiece() != null)
                    && (state[x + 1][y - 1].getpiece().getcolor() != this.getcolor()))
                possiblemoves.add(state[x + 1][y - 1]);
            if ((y < 7) && (state[x + 1][y + 1].getpiece() != null)
                    && (state[x + 1][y + 1].getpiece().getcolor() != this.getcolor()))
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
    
    public boolean getJustSkipped() {
        return this.justSkipped;
    }
    
    public void setJustSkipped(boolean justSkipped) {
        this.justSkipped = justSkipped;
    }
}
