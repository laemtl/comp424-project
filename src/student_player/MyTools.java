package student_player;

import java.util.ArrayList;
import java.util.List;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;

public class MyTools {
	// Override of the core function to eliminate quadrant swapping
	public static List<PentagoMove> getPossibleMoves(PentagoBoardState state) {
		List<PentagoMove> legalMoves = new ArrayList<>();
        for (int i = 0; i < PentagoBoardState.BOARD_SIZE; i++) { //Iterate through positions on board
            for (int j = 0; j < PentagoBoardState.BOARD_SIZE; j++) {
                if (state.getPieceAt(i, j) == Piece.EMPTY) {
                	
                	// Careful, default Quadrant values
                	legalMoves.add(new PentagoMove(i, j, Quadrant.TL, Quadrant.TR, state.getTurnPlayer()));
                }
            }
        }
        return legalMoves;
    }
	
	// Returns the optimal value a maximizer can obtain. 
	// depth is the limit depth in game tree. 
	// isMax is true if current move is of maximizer
    public static MiniMaxTree minimax(int depth, boolean isMax, PBSTree bsTree, int player_id) {
    	
    	// Terminating condition. i.e leaf node is reached or max depth reached
	    if (depth == 0 || bsTree.getChildren().isEmpty()) {
	    	int score = HeuristicFunction.compute(player_id, bsTree.getState());
	    	MiniMaxTree treeCopy = new MiniMaxTree(player_id, bsTree, score);
	    	treeCopy.setMove(null);
	    	
	    	return treeCopy;
	    } else {
	    	List<MiniMaxTree> list = new ArrayList<>();
		    for (PBSTree child : bsTree.getChildren()) {
		    	MiniMaxTree childMiniMax = null;
				childMiniMax = new MiniMaxTree(player_id, minimax(depth-1, !isMax, child, player_id));
				childMiniMax.setMove(child.getMove());
				list.add(childMiniMax);
		    }
		    
		    if (isMax) {
		    	MiniMaxTree max = null;
			    for (MiniMaxTree child : list) {
			    	if(depth == 2) {
			    		//System.out.println("child score : " + child.getScore());
			    		//System.out.println(child.getState());	
		    		}	
			    	if ( max == null || max.compareTo(child) < 0) {
			    		max = child;
			    	}
			    }
			    
			    return max;
			    
		    } else {
		    	MiniMaxTree min = null;
			    for (MiniMaxTree child : list) {
			    	//System.out.println("child score : " + child.getScore());
			    	//System.out.println(child.getState());			
		    		
			    	if ( min == null || min.compareTo(child) > 0) {
			    		min = child;
			    	}
			    }
			    
			    return min;
		    }
	    }
	}

	public static PentagoMove getMove(int player_id, PentagoBoardState boardState) {
				
		// Save original out stream.
		/*PrintStream originalOut = System.out;
        
        // Create a new file output stream.
        PrintStream fileOut = null;
		try {
			fileOut = new PrintStream(boardgame.Server.log_dir + "/out.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Redirect standard out to file.
        System.setOut(fileOut);*/
        
        
        
        
		int depth = 2;
		PBSTree bsTree = new PBSTree(boardState);

		// If there is a winning move do it!
		for (PBSTree child : bsTree.getChildren()) {
			if(child.getState().getWinner() == player_id) return child.getMove();
		}
		
		MiniMaxTree tree = minimax(depth, true, bsTree, player_id);
		
		// Some logic here to define the best swap
		
		System.out.println("selected score : " + tree.getScore());
		System.out.println(tree.getState());			
		System.out.println("Move : " + tree.getMove().getMoveCoord().getX() + " " + tree.getMove().getMoveCoord().getY());
		
		
		return tree.getMove();
		
		
	}
}