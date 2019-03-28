package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MyTools3 {
	// Returns the optimal value a maximizer can obtain. 
	// depth is the limit depth in game tree. 
	// isMax is true if current move is of maximizer
    public static PentagoMove minimax(int depth, boolean isMax, PBSTree bsTree){
    	
    	// Terminating condition. i.e leaf node is reached or max depth reached
	    if (depth == 0 || bsTree.getState().gameOver()) {
	    	//bsTree.setScore(HeuristicFunction.compute(bsTree.getState()));
	    	return null;
	    }
	  
	    for (PBSTree child : bsTree.getChildren()) {
	    	PentagoMove m = minimax(depth-1, !isMax, child);
	    	System.out.println("Après Min Max");
		}
	    
	    // If current move is maximizer, find the maximum value 
	    if (isMax) { 	    	
	    	PBSTree max = bsTree.getMaxChildren();
	    	bsTree.setScore(max.getScore());
	    	
	    // If current move is minimizer, find the minimum value 	
	    } else {
	    	PBSTree min = bsTree.getMinChildren();
	    	bsTree.setScore(min.getScore());
	    }
	  
	
	    return bsTree.getOptimalNextState().getMove();
	    
	    
	    

		  
	    /*
	    for (PBSTree child : bsTree.getChildren()) {
	    	PBSTree tree = minimax(depth-1, !isMax, child);
	    	
	    	// If current move is maximizer, find the maximum value 
		    if (isMax) { 	    	
		    	PBSTree max = bsTree.getMaxChildren();
		    	bsTree.setScore(max.getScore());
		    	bsTree.setMove(max.getScore());
		    	
		    	
		    // If current move is minimizer, find the minimum value 	
		    } else {
		    	PBSTree min = bsTree.getMinChildren();
		    	bsTree.setScore(min.getScore());
		    }
		}
		*/
	}

	/*public static PentagoMove getMove(PentagoBoardState boardState) {
		int depth = 4;
		PBSTree bsTree = new PBSTree(boardState, null);
		return MyTools.minimax(depth, true, bsTree);
	}*/
}
