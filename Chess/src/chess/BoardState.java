package chess;

import java.util.Arrays;

public class BoardState {

	private Cell[][] state;
	
	public BoardState(Cell[][] state) {
		this.state = state;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardState)) return false;
        BoardState that = (BoardState) o;
        return Arrays.deepEquals(this.state, that.state);
    }
	
    public void printState() {
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			System.out.print(state[i][j].getpiece() != null ? state[i][j].getpiece().getPieceType() : "XX" );
    		}
    		System.out.print("\n");
    	}
    }
    
    public String buildString() {
        StringBuilder stateString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                stateString.append(state[i][j].getpiece() != null ? state[i][j].getpiece().getPieceType() : "XX");
            }
        }
        return stateString.toString();
    }
}
