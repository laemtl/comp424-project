package student_player;

import java.util.function.UnaryOperator;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoCoord;

public class HeuristicFunction {	
    // 0 : 2-in-a-row
    // 1 : 3-in-a-row
    // 2 : 4-in-a-row 	
    // 3 : 5-in-a-row
    // 4 : marble in center
    // 5 : 3-in-a-row (same block)
	
	// detect hole
	// place hole
	// detect if placement is useless (3 in a row where we can't place more than 3)
    
    // never touch the 4 corners for opening
    // Must play when my opponent have 4 in a row
    // 4 in a row with both ends open will almost always win
    
    // align stones at last minute
    // pattern 
    // x0x0x0
    // 00x0x0
    // 0000x0
    
    // when opponent forms alignment, block where there is another stone 
    // x00x0x0
    // x00*0x0
    
    // heuristic should give more point for sparse alignment
    // -> trick the opponent
    
    // logic to construct alignment on 2 different quarter
    
    // don’t  let  the  opponent  get  three  in  a  row  on  one  game block, 
    // or you’ll be on the defensive

	
	
	
	
	
	
	
	
	
	
	// 9 patterns for rows/cols
	
	// 0x0 : 0
	// x0x : 0
	// xx0 : 0
	// 0xx : 0
	
	// 100 : 4
	// 010 : 2
	// 001 : 1
	// 110 : 6
	// 101 : 5
	// 011 : 3
	// 111 : 7
	
	// 3 levels
	
	int[] top_row = new int[8];
	int[] mid_row = new int[8];
	int[] bot_row = new int[8];
	
    static int[][] counter;
    static int value;
    
    static int[] myScore = {50, 500, 10000, 100000, 5, 1000};
    static int[] oppScore = {-100, -1000, -20000, -200000, -10, -2000};
	
    static public int compute(int player_id, PentagoBoardState state) {	
    	counter = new int[2][6];
    	value = 0;
    	
    	checkVertical(state);
		checkHorizontal(state);
		checkDiagRight(state);
		checkDiagLeft(state);
		checkCenters(state);
		
		for (int i = 0; i < 6; i++) {
			value += counter[player_id][i] * myScore[i]; 
			value += counter[1-player_id][i] * oppScore[i]; 			
		}
				
		return value;
	}	
	
	private static void checkCenters(PentagoBoardState state) {
		Piece c1 = state.getPieceAt(1, 1);
		if(c1 == Piece.BLACK) {
			counter[1][4] += 1;
		} else if(c1 == Piece.WHITE) {
			counter[0][4] += 1;
		}

		Piece c2 = state.getPieceAt(4, 1);
		if(c2 == Piece.BLACK) {
			counter[1][4] += 1;
		} else if(c2 == Piece.WHITE) {
			counter[0][4] += 1;
		}
		
		Piece c3 = state.getPieceAt(1, 4);
		if(c3 == Piece.BLACK) {
			counter[1][4] += 1;
		} else if(c3 == Piece.WHITE) {
			counter[0][4] += 1;
		}
		
		Piece c4 = state.getPieceAt(4, 4);
		if(c4 == Piece.BLACK) {
			counter[1][4] += 1;
		} else if(c4 == Piece.WHITE) {
			counter[0][4] += 1;
		}
	}

	private static void checkVertical(PentagoBoardState state) { 
		final UnaryOperator<PentagoCoord> getNextVertical = c -> new PentagoCoord(c.getX()+1, c.getY());
        int count = 0;
        int endX = PentagoBoardState.BOARD_SIZE;
        int endY = PentagoBoardState.BOARD_SIZE;
        
        PentagoCoord current;
		Piece currColour;
		Piece prevColour = null;
        
        for (int i = 0; i < endX; i++) {
        	current = new PentagoCoord(0, i);
        	currColour = state.getPieceAt(current.getX(), current.getY());
        	prevColour = null;
        	 
			for (int j = 0; j < endY; j++) {		
				if(currColour != prevColour) {
					registerSequence(prevColour, count);
	            	count = 0;
				}
	            
				count++;
	            prevColour = currColour;
	            
	            if(j != endY - 1) {
		            current = getNextVertical.apply(current);
		            currColour = state.getPieceAt(current.getX(), current.getY());
	            } else {
	            	registerSequence(prevColour, count);
	            }
	        }
        }
    }

    private static void checkHorizontal(PentagoBoardState state) {
    	final UnaryOperator<PentagoCoord> getNextHorizontal = c -> new PentagoCoord(c.getX(), c.getY()+1);
        int count = 0;
        int endX = PentagoBoardState.BOARD_SIZE;
        int endY = PentagoBoardState.BOARD_SIZE;
        
        PentagoCoord current;
		Piece currColour;
		Piece prevColour = null;
        
        for (int i = 0; i < endX; i++) {
        	current = new PentagoCoord(i, 0);
     		currColour = state.getPieceAt(current.getX(), current.getY());
     		prevColour = null;
     		
			for (int j = 0; j < endY; j++) {		
				if(currColour != prevColour) {
					registerSequence(prevColour, count);
	            	count = 0;
				}
	            
				count++;
	            prevColour = currColour;
	            
	            if(j != endY - 1) {
		            current = getNextHorizontal.apply(current);
		            currColour = state.getPieceAt(current.getX(), current.getY());
	            } else {
	            	registerSequence(prevColour, count);
	            }
	        }
        }
    }

    private static void checkDiagRight(PentagoBoardState state) {
    	//final UnaryOperator<PentagoCoord> getNextDiagRight = c -> new PentagoCoord(c.getX()+1, c.getY()+1);
        //checkAlign(state, PentagoBoardState.BOARD_SIZE, 1, getNextDiagRight);
    }

    private static void checkDiagLeft(PentagoBoardState state) {
    	//final UnaryOperator<PentagoCoord> getNextDiagLeft = c -> new PentagoCoord(c.getX()+1, c.getY()-1);
    	//checkAlign(state, 1, PentagoBoardState.BOARD_SIZE, getNextDiagLeft);
    }
    
    private static void registerSequence(Piece prevColour, int count) {
    	if (count > 1) {
    		if(prevColour == Piece.BLACK) {
				counter[1][Math.min(count-2, 5)] += 1;
			} else if(prevColour == Piece.WHITE) {
				counter[0][Math.min(count-2, 5)] += 1;
			}
    	}
    }
}
