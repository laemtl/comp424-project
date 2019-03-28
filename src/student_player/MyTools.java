package student_player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MyTools {
	// Returns the optimal value a maximizer can obtain. 
	// depth is the limit depth in game tree. 
	// isMax is true if current move is of maximizer
    public static PBSTree minimax(int depth, boolean isMax, PBSTree bsTree, int player_id) {
    	
    	// Terminating condition. i.e leaf node is reached or max depth reached
	    if (depth == 0 || bsTree.getState().gameOver() || bsTree.getChildren().isEmpty()) {
	    	bsTree.setScore(HeuristicFunction.compute(player_id, bsTree.getState()));
	    	PBSTree treeCopy = null;
			try {
				treeCopy = bsTree.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
	    	treeCopy.setMove(null);
	    	return treeCopy;
	    } else {
	    	List<PBSTree> list = new ArrayList<>();
		    for (PBSTree child : bsTree.getChildren()) {
		    	PBSTree childMiniMax = null;
				try {
					childMiniMax = minimax(depth-1, !isMax, child, player_id).clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
		    	childMiniMax.setMove(child.getMove());
		    	list.add(childMiniMax);
		    }
		    
		    if (isMax) {
		    	PBSTree max = null;
			    for (PBSTree child : list) {
			    	if (max == null) {
			    		max = child;
			    	} else if (child.getScore() > max.getScore()) {
			    		max = child;
			    	}
			    }
			    return max;
		    } else {
		    	PBSTree min = null;
			    for (PBSTree child : list) {
			    	if (min == null) {
			    		min = child;
			    	} else if (child.getScore() < min.getScore()) {
			    		min = child;
			    	}
			    }
			    return min;
		    }
	    }
	}

	public static PentagoMove getMove(int player_id, PentagoBoardState boardState) {
		int depth = 2;
		PBSTree bsTree = new PBSTree(boardState, null);

		// Pick a random first move
		if(boardState.getTurnNumber() < 1) {
			ArrayList<PentagoMove> move = boardState.getAllLegalMoves();
			Random rand = new Random();
		    return move.get(rand.nextInt(move.size()));
		} else {
			// If there is a winning move do it!
			for (PBSTree child : bsTree.getChildren()) {
				if(child.getState().getWinner() == player_id) return child.getMove();
			}
			
			PBSTree t = minimax(depth, true, bsTree, player_id);
			
			return t.getMove();
		}
	}
}