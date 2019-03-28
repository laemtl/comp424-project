package student_player;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MyTools2 {
	// Returns the optimal value a maximizer can obtain. 
	// depth is the limit depth in game tree. 
	// isMax is true if current move is of maximizer
    /*public static PentagoMove minimax(int depth, boolean isMax, PBSTree bsTree){
    	
    	// Terminating condition. i.e leaf node is reached or max depth reached
	    if (depth == 0 || bsTree.getState().gameOver()) {
	    	bsTree.setScore(HeuristicFunction.compute(bsTree.getState()));
	    	return null;
	    }
	  
	    
	    
	    // If current move is maximizer, find the maximum value 
	    if (isMax) {
	    	for (PBSTree child : bsTree.getChildren()) {
		    	minimax(depth-1, !isMax, child);
			}
	    	
	    	PBSTree max = bsTree.getMaxChildren();
	    	bsTree.setScore(max.getScore());
	    	
	    	return bsTree.getOptimalNextState().getMove();
	    	
	    // If current move is minimizer, find the minimum value 	
	    } else {
	    	PBSTree min = bsTree.getMinChildren();
	    	bsTree.setScore(min.getScore());
	    }
	  
	    
	}*/

	public static PentagoMove getMove(PentagoBoardState boardState) {
		int maxDepth = 4;
		
		MiniMaxTree bsTree = new MiniMaxTree(boardState, maxDepth);
		//return MyTools.minimax(depth, true, bsTree);
		return bsTree.getBestNextMove();
	}
}
