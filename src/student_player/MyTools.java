package student_player;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;

public class MyTools {
	
	// Override of the core function to eliminate quadrant swapping
	public static ArrayList<PentagoMove> getPossibleMoves(PentagoBoardState state) {
        ArrayList<PentagoMove> legalMoves = new ArrayList<>();
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
    public static MiniMaxTree minimax(int depth, boolean isMax, PBSTree bsTree, PBSTree parentState, int player_id) {
    	
    	
    	// Terminating condition. i.e leaf node is reached or max depth reached
	    if (depth == 0 || bsTree.getState().gameOver() || bsTree.getChildren().isEmpty()) {
	    	int score = HeuristicFunction.compute(player_id, bsTree.getState());
	    	MiniMaxTree treeCopy = new MiniMaxTree(player_id, bsTree, score);
	    	treeCopy.setMove(null);
	    	if(isMax && parentState != null) treeCopy.setPrevState(new MiniMaxTree(player_id, parentState));
	    	
	    	//System.out.println("leaf score : " + bsTree.getScore());
		    //System.out.println(bsTree.getState());
		    
	    	return treeCopy;
	    } else {
	    	List<MiniMaxTree> list = new ArrayList<>();
		    for (PBSTree child : bsTree.getChildren()) {
		    	MiniMaxTree childMiniMax = null;
		    	if(isMax) parentState = bsTree;
				childMiniMax = new MiniMaxTree(player_id, minimax(depth-1, !isMax, child, parentState, player_id));
				childMiniMax.setMove(child.getMove());
				if(isMax && parentState != null) childMiniMax.setPrevState(new MiniMaxTree(player_id, parentState));
		    	list.add(childMiniMax);
		    }
		    
		    if (isMax) {
		    	
		    	MiniMaxTree max = null;
			    for (MiniMaxTree child : list) {
			    	
			    	/*if(depth == 3) {
			    		System.out.println("child (max) score : " + child.getScore());
			    		System.out.println(child.getState());
			    	}*/
			    	
			    	if(max != null && child.getScore() == 9954) {
			    		System.out.println("child (egal) score : " + child.getScore());
			    	
			    		System.out.println(child.getState());
			    		
			    		System.out.println("Prev state");
			    		System.out.println(child.getPrevState().getState());
			    		
			    		System.out.println("prev score child " + child.getPrevState().getScore());
			    		System.out.println("prev score max " + max.getPrevState().getScore());
			    	}
			    	
					if ( max == null || child.getScore() > max.getScore()) {
			    		max = child;
			    	} else if(child.getScore() == max.getScore()) {
					    if(child.getPrevState() != null && max.getPrevState() != null) {
					    	
				    		
					    	if(child.getPrevState().getScore() > max.getPrevState().getScore()) {
					    		max = child;
					    	}
					    }
			    	}
			    }
			    
			    //System.out.println("max score : " + max.getScore());
			    //System.out.println(max.getState());
			    
			    return max;
		    } else {
		    	MiniMaxTree min = null;
			    for (MiniMaxTree child : list) {
			    	if (min == null || child.getScore() < min.getScore()) {
			    		min = child;
			    	}
			    }
			    
			    //System.out.println("min score : " + min.getScore());
			    //System.out.println(min.getState());
			    
			    return min;
		    }
	    }
	}

	public static PentagoMove getMove(int player_id, PentagoBoardState boardState) {
		
		// Save original out stream.
        PrintStream originalOut = System.out;
        
        // Create a new file output stream.
        PrintStream fileOut = null;
		try {
			fileOut = new PrintStream(boardgame.Server.log_dir + "/out.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Redirect standard out to file.
        System.setOut(fileOut);
        
        
        
        
		int depth = 3;
		PBSTree bsTree = new PBSTree(boardState);

		//System.out.println("original score : " + bsTree.getScore());
		//System.out.println(bsTree.getState());
		   
		// Pick a random first move
		/*if(boardState.getTurnNumber() < 1) {
			ArrayList<PentagoMove> move = boardState.getAllLegalMoves();
			Random rand = new Random();
		    return move.get(rand.nextInt(move.size()));
		} else {*/
			// If there is a winning move do it!
			for (PBSTree child : bsTree.getChildren()) {
				if(child.getState().getWinner() == player_id) return child.getMove();
			}
			
			MiniMaxTree tree = minimax(depth, true, bsTree, null, player_id);
			
			// Some logic here to define the best swap
			
			System.out.println("selected score : " + tree.getScore());
			System.out.println(tree.getState());
			System.out.println(tree.getPrevState().getState());
			System.out.println("prev score max " + tree.getPrevState().getScore());
			
			System.out.println("Move : " + tree.getMove().getMoveCoord().getX() + " " + tree.getMove().getMoveCoord().getY());
			
			
			return tree.getMove();
			
			
		//}
	}
}