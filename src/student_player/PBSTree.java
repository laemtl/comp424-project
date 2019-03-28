package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class PBSTree implements Cloneable {

	private PentagoBoardState state;
    private List<PBSTree> children;
	private PentagoMove move;
	private int score;
	
	public PBSTree(PentagoBoardState state, int depth) {
		this(state, null);
    }
	
    public PBSTree(PentagoBoardState state, PentagoMove move) {
        this.state = state;
        this.children = null;
        this.move = move;
        this.score = Integer.MIN_VALUE;
    } 

    @Override
	protected PBSTree clone() throws CloneNotSupportedException {
		return (PBSTree)super.clone();
	}

	public List<PBSTree> getChildren() {
    	if(children == null) generateChildren();
    	return this.children;
    }
    
	private void generateChildren() {
		children = new ArrayList<PBSTree>();
		
		if(state.gameOver()) return;
			
		for (PentagoMove move : state.getAllLegalMoves()) {
			PentagoBoardState newState = (PentagoBoardState) state.clone();
			newState.processMove(move);	
			children.add(new PBSTree(newState, move));
		}
	}
	
	public PBSTree getMaxChildren() {
		return Collections.max(getChildren(), new Comparator<PBSTree>() {
			@Override
			public int compare(PBSTree a, PBSTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}
	
	public PBSTree getMinChildren() {
		return Collections.min(getChildren(), new Comparator<PBSTree>() {
			@Override
			public int compare(PBSTree a, PBSTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}
	
	public PentagoBoardState getState() {
		return state;
	}
	
	public PentagoMove getMove() {
		return move;
	}
	
	public void setMove(PentagoMove move) {
		this.move = move;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}