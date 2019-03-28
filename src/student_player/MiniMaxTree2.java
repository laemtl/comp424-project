package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MiniMaxTree {

	private PentagoBoardState state;
    private List<MiniMaxTree> children;
	private PentagoMove move;
	private int score;
	private PentagoMove bestNextMove;
	int depth;
	boolean maximizer;
	
	public MiniMaxTree(PentagoBoardState state, int depth) {
		this(state, null, true, depth);
    }
	
    public MiniMaxTree(PentagoBoardState state, PentagoMove move, boolean isMax, int depth) {
        this.state = state;
        this.children = null;
        this.move = move;
        this.score = Integer.MIN_VALUE;
        this.depth = depth;
    }

    public List<MiniMaxTree> getChildren() {
    	if(children == null) generateChildren();
    	return this.children;
    }
    
	private void generateChildren() {
		children = new ArrayList<MiniMaxTree>();
		
		if(state.gameOver()) return;
			
		for (PentagoMove move : state.getAllLegalMoves()) {
			PentagoBoardState newState = (PentagoBoardState) state.clone();
			newState.processMove(move);
			children.add(new MiniMaxTree(newState, move, !maximizer, depth-1));
		}
	}
	
	public MiniMaxTree getMaxChildren() {
		return Collections.max(getChildren(), new Comparator<MiniMaxTree>() {
			@Override
			public int compare(MiniMaxTree a, MiniMaxTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}
	
	public MiniMaxTree getMinChildren() {
		return Collections.min(getChildren(), new Comparator<MiniMaxTree>() {
			@Override
			public int compare(MiniMaxTree a, MiniMaxTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}
	
	public PentagoBoardState getState() {
		return state;
	}
	
	public int getScore() {
		return score;
	}
	
	private void setScore(int score) {
		this.score = score;
	}
	
	public PentagoMove getBestNextMove() {
		if (depth == 0 || getState().gameOver()) {
			if(score == Integer.MIN_VALUE) {
				setScore(HeuristicFunction.compute(getState()));
			}
			return null;
	    }
	    
	    // If current move is maximizer, find the maximum value 
	    if (maximizer) {
	    	for (MiniMaxTree child : getChildren()) {
	    		child.getBestNextMove(depth-1, !maximizer, child);
			}
	    	
	    	MiniMaxTree max = bsTree.getMaxChildren();
	    	setScore(max.getScore());
	    	
	    	return getOptimalNextState().getMove();
	    	
	    // If current move is minimizer, find the minimum value 	
	    } else {
	    	PBSTree min = bsTree.getMinChildren();
	    	bsTree.setScore(min.getScore());
	    }
		
		return bestNextMove;
	}
}
