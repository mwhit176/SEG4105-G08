package pieces;

import java.util.ArrayList;

import chess.Cell;

public class King extends Piece {
    boolean castle;
    // King Constructor
    public King(String i, String p, int c, int x, int y) {
        setx(x);
        sety(y);
        setId(i);
        setPath(p);
        setColor(c);
    }
    
    public boolean getCastle() {
    	return castle;
    }
    
    public void setCastle(boolean castle) {
    	this.castle = castle;
    }

    // Move Function for King Overridden from Pieces
    public ArrayList<Cell> move(Cell state[][], int x, int y) {
        // King can move only one step. So all the adjacent 8 cells have been
        // considered.
        possiblemoves.clear();
        possiblemoves = getCandidateMoves(state);
        
        if (this.getMoveCount() == 0 && !isindanger(state)) {
        	Piece rightPiece = state[x][y+3].getpiece();
        	Piece leftPiece = state[x][y-4].getpiece();
        	
        	// Does checks for queen side castle
        	if (rightPiece instanceof Rook && rightPiece.getcolor() == this.getcolor()) {
        		castle = true;
        		if (rightPiece.getMoveCount() == 0) {
        			for (int i = y+1; i < y+3; i++) {
        				if (castle && state[x][i].getpiece() != null) {
        					castle = false;
        				}
        				if (i <= y+2 && isindanger(state, x, i)) {
        					castle = false;
        				}
        			}
        			if (castle) {
        				possiblemoves.add(state[x][y+2]);
        			}
        			
        		}
        	}
        	
        	// Does checks for king side castle 
        	if (leftPiece instanceof Rook && leftPiece.getcolor() == this.getcolor()) {
        		castle = true;
        		if (leftPiece.getMoveCount() == 0) {
        			for (int i = y-1; i > y-4; i--) {
        				if (castle && state[x][i].getpiece() != null) {
        					castle = false;
        				}
        				if (i >= y-2 && isindanger(state, x, i)) {
        					castle = false;
        				}
        			}
        			if (castle) {
        				possiblemoves.add(state[x][y-2]);
        			}
        		}
        	}
        }
        return possiblemoves;
    }

    // Function to check if king is under threat
    // It checks whether there is any piece of opposite color that can attack king
    // for a given board state
    public boolean isindanger(Cell state[][]) {

       return isindanger(state, this.getx(), this.gety());
    }
    
    public boolean isindanger(Cell state[][], int x, int y) {

        // Checking for attack from left,right,up and down
        for (int i = x + 1; i < 8; i++) {
            if (state[i][y].getpiece() == null)
                continue;
            else if (state[i][y].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if ((state[i][y].getpiece() instanceof Rook) || (state[i][y].getpiece() instanceof Queen))
                    return true;
                else
                    break;
            }
        }
        for (int i = x - 1; i >= 0; i--) {
            if (state[i][y].getpiece() == null)
                continue;
            else if (state[i][y].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if ((state[i][y].getpiece() instanceof Rook) || (state[i][y].getpiece() instanceof Queen))
                    return true;
                else
                    break;
            }
        }
        for (int i = y + 1; i < 8; i++) {
            if (state[x][i].getpiece() == null)
                continue;
            else if (state[x][i].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if ((state[x][i].getpiece() instanceof Rook) || (state[x][i].getpiece() instanceof Queen))
                    return true;
                else
                    break;
            }
        }
        for (int i = y - 1; i >= 0; i--) {
            if (state[x][i].getpiece() == null)
                continue;
            else if (state[x][i].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if ((state[x][i].getpiece() instanceof Rook) || (state[x][i].getpiece() instanceof Queen))
                    return true;
                else
                    break;
            }
        }

        // checking for attack from diagonal direction
        int tempx = x + 1, tempy = y - 1;
        while (tempx < 8 && tempy >= 0) {
            if (state[tempx][tempy].getpiece() == null) {
                tempx++;
                tempy--;
            } else if (state[tempx][tempy].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
                    return true;
                else
                    break;
            }
        }
        tempx = x - 1;
        tempy = y + 1;
        while (tempx >= 0 && tempy < 8) {
            if (state[tempx][tempy].getpiece() == null) {
                tempx--;
                tempy++;
            } else if (state[tempx][tempy].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
                    return true;
                else
                    break;
            }
        }
        tempx = x - 1;
        tempy = y - 1;
        while (tempx >= 0 && tempy >= 0) {
            if (state[tempx][tempy].getpiece() == null) {
                tempx--;
                tempy--;
            } else if (state[tempx][tempy].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
                    return true;
                else
                    break;
            }
        }
        tempx = x + 1;
        tempy = y + 1;
        while (tempx < 8 && tempy < 8) {
            if (state[tempx][tempy].getpiece() == null) {
                tempx++;
                tempy++;
            } else if (state[tempx][tempy].getpiece().getcolor() == this.getcolor())
                break;
            else {
                if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
                    return true;
                else
                    break;
            }
        }

        // Checking for attack from the Knight of opposite color
        int posx[] = { x + 1, x + 1, x + 2, x + 2, x - 1, x - 1, x - 2, x - 2 };
        int posy[] = { y - 2, y + 2, y - 1, y + 1, y - 2, y + 2, y - 1, y + 1 };
        for (int i = 0; i < 8; i++)
            if ((posx[i] >= 0 && posx[i] < 8 && posy[i] >= 0 && posy[i] < 8))
                if (state[posx[i]][posy[i]].getpiece() != null
                        && state[posx[i]][posy[i]].getpiece().getcolor() != this.getcolor()
                        && (state[posx[i]][posy[i]].getpiece() instanceof Knight)) {
                    return true;
                }

        // Checking for attack from the Pawn of opposite color
        int pox[] = { x + 1, x + 1, x + 1, x, x, x - 1, x - 1, x - 1 };
        int poy[] = { y - 1, y + 1, y, y + 1, y - 1, y + 1, y - 1, y };
        {
            for (int i = 0; i < 8; i++)
                if ((pox[i] >= 0 && pox[i] < 8 && poy[i] >= 0 && poy[i] < 8))
                    if (state[pox[i]][poy[i]].getpiece() != null
                            && state[pox[i]][poy[i]].getpiece().getcolor() != this.getcolor()
                            && (state[pox[i]][poy[i]].getpiece() instanceof King)) {
                        return true;
                    }
        }
        if (getcolor() == 0) {
            if (x > 0 && y > 0 && state[x - 1][y - 1].getpiece() != null
                    && state[x - 1][y - 1].getpiece().getcolor() == 1
                    && (state[x - 1][y - 1].getpiece() instanceof Pawn))
                return true;
            if (x > 0 && y < 7 && state[x - 1][y + 1].getpiece() != null
                    && state[x - 1][y + 1].getpiece().getcolor() == 1
                    && (state[x - 1][y + 1].getpiece() instanceof Pawn))
                return true;
        } else {
            if (x < 7 && y > 0 && state[x + 1][y - 1].getpiece() != null
                    && state[x + 1][y - 1].getpiece().getcolor() == 0
                    && (state[x + 1][y - 1].getpiece() instanceof Pawn))
                return true;
            if (x < 7 && y < 7 && state[x + 1][y + 1].getpiece() != null
                    && state[x + 1][y + 1].getpiece().getcolor() == 0
                    && (state[x + 1][y + 1].getpiece() instanceof Pawn))
                return true;
        }
        return false;
    }

    private ArrayList<Cell> getCandidateMoves(Cell state[][]){
        ArrayList<Cell> candidates = new ArrayList<Cell>();
        int x = this.getx(), y = this.gety();

        int posx[] = { x, x, x + 1, x + 1, x + 1, x - 1, x - 1, x - 1 };
        int posy[] = { y - 1, y + 1, y - 1, y, y + 1, y - 1, y, y + 1 };
        for (int i = 0; i < 8; i++){
            if (posx[i] >= 0 && posx[i] < 8 && posy[i] >= 0 && posy[i] < 8){
                boolean positionIsAvailable = state[posx[i]][posy[i]].getpiece() == null || (state[posx[i]][posy[i]].getpiece().getcolor() != this.getcolor());
                boolean positionIsNotCurrent = !(posx[i] == x && posy[i] == y);

                if (positionIsAvailable && positionIsNotCurrent){ candidates.add(state[posx[i]][posy[i]]); }
            }
        }

        return candidates;
    }
}