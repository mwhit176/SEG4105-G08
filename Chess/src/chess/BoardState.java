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

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.state);
    }
	
}
