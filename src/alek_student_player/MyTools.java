package alek_student_player;

import java.util.ArrayList;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoMove;

public class MyTools {	
	
	/*
	 * Strategy that will be used to pick the best move:
	 * 1. Can I win ? 
	 * 2. If I can't win : ( Make sure opponent cannot win ) Choose best move with the following heuristic:
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static Piece myPiece;
	public static Piece oppPiece;
	
	public static int oppPoints = 0;
	
	public static int myPoints = 0;	
	
    
	
	
    public static PentagoMove getHighestScoringMove(PentagoBoardState state, int turnNum) {
    	
    	int turn = -1;
    	if (myPiece == Piece.BLACK) turn = 1;
    	else turn = 0;
    	
    	PentagoBoardState clone = (PentagoBoardState) state.clone();
    	
    	
    	ArrayList<PentagoMove> moves = clone.getAllLegalMoves();
    	
    	PentagoMove theMove = null;
    	
    
    	int differential = Integer.MIN_VALUE; //initialize scoring to the lowest possible integer
    	
    	for (int i = 0; i < moves.size(); i++) { //loop through all moves
    		
    		PentagoBoardState tryClone = (PentagoBoardState) clone.clone();
    		
    		tryClone.processMove(moves.get(i));
    		
    		if (tryClone.gameOver() && tryClone.getWinner() == turn) { //if wins
    			
    			return moves.get(i); //STOP NOW AND RETURN
    			
    		}
    		//else means we cannot win with this move, still gotta evaluate if it is a feasible move.
    		int differentialCompare = generateOpponentChildren(tryClone, turnNum);
    		//minimax here
    		
    		if (differentialCompare > differential) //means move is promising , want to maximize this value.
    		{
    		
        			
    			differential = differentialCompare; //update differential
    			theMove = moves.get(i);
    			
    	    	System.out.println("\nCheck Move " + i + ": " + differential + "\n");
    	    	
    		
    		
    			
    		}
    			
    	}
    		
    	
    	//System.out.println("Move Chosen: " + differential + "\n");
    	//System.out.println("\nTHE MOVE IS: ");
    	if (theMove == null) {
    		return (PentagoMove) state.getRandomMove(); // means we have lost for sure :'(
    		
    	}
    	return theMove;
    	
    }
    
    
    
    private static int generateOpponentChildren(PentagoBoardState tryClone, int turnNum) {
		
    	PentagoBoardState state = (PentagoBoardState) tryClone.clone();
    	
    	ArrayList<PentagoMove> moves = state.getAllLegalMoves();
    	int toReturn = Integer.MAX_VALUE; //initialize scoring of opponent to max value, his goal is to reduce this
    	
    	for (int i = 0; i < moves.size(); i++) {
    		PentagoBoardState tryOppMove = (PentagoBoardState) state.clone();
    		
    		tryOppMove.processMove(moves.get(i));
    		if (tryOppMove.gameOver() && tryOppMove.getWinner()==tryOppMove.getOpponent()) {
    			//means they can win, we do not want them to win.
    			
    			return Integer.MIN_VALUE; 
    			
    			
    		}
    		int tempToReturn;
    		if (turnNum  > 9) {
    			tempToReturn = generateSecondDegreePoints(tryOppMove, turnNum);
        		
    		}
    		else {
    			initializeScoring(tryOppMove);
    			tempToReturn = oppPoints + myPoints;
    		}
    		if (tempToReturn < toReturn) { //opp player is going to want to play his most promising node.
    			
    			toReturn = tempToReturn;
    			
    		}
    		
    		
    	}
    	
		return toReturn;
	}


    private static int generateSecondDegreePoints(PentagoBoardState state, int turnNum) {
    	int myTurn = -1;
    	if (myPiece == Piece.WHITE) myTurn = 0;
    	else myTurn = 1;
    	PentagoBoardState cloneState = (PentagoBoardState) state.clone();
    	ArrayList<PentagoMove> moves = cloneState.getAllLegalMoves();
    	
    	
    	int differential = Integer.MIN_VALUE;
    	
    	for (int i =0 ; i < moves.size(); i++) {
    		
    		PentagoBoardState tryMoves = (PentagoBoardState) cloneState.clone();
        	
    		tryMoves.processMove(moves.get(i));
    		
    		if (tryMoves.gameOver() && tryMoves.getWinner() == myTurn) {
    			
    			return Integer.MAX_VALUE;
    			
    		}
    		int differentialCompare; 
    		if (turnNum == 14 || turnNum == 15) { //can go four deep
    			differentialCompare = generateOpponentChildren(tryMoves, 0);
    		}
    		
    		else if (turnNum > 15) {//can go 5 deep
    			
    			differentialCompare = generateOpponentChildren(tryMoves, turnNum);
    		}
    		else { //the board will be too big to go another level deeper
    			initializeScoring(tryMoves);
    		
    			differentialCompare = oppPoints + myPoints;
    		}
    		if (differentialCompare > differential) { //wanna maximize the points
    			
    			differential = differentialCompare;
    			
    			
    		}
    		
    	}
    	return differential;
    }

	public static void initializeScoring(PentagoBoardState state){
    	myPoints = 0;
    	oppPoints = 0;
    	
    	//first element of this array List is our player's score and second element is opponent's points.
    	
    	myPoints += alongMid(state, myPiece);
    	
    	oppPoints -= alongMid(state, oppPiece);
    	
    	
    	
    	myPoints += getPointsFromQuadrants(myPiece, state);
    	
    	
    	
    	oppPoints -= (int) Math.pow(getPointsFromQuadrants(oppPiece, state), 1.5);

    	
    	
    	
    	
    }
    
    

    
    
    

    
    private static int alongMid(PentagoBoardState state, Piece p) {
    	int number = 0;
    	
    	if (state.getPieceAt(1, 1) == p) number += 40;
    	if (state.getPieceAt(1,4) == p) number += 40;
    	if (state.getPieceAt(4,1) == p) number += 40;
    	if (state.getPieceAt(4,4) == p) number += 40;
    	
    	//System.out.println("\nThe move will give 20 points: \n" + number);
    	return number;
    	
    	
    	
    }
    


    
    
    
    
    private static int getPointsFromQuadrants(Piece p, PentagoBoardState state) {
    	//ANOTHER IDEA:
    	//Want to favor doing sequences that align with each other
    	
    	int quad1 = 0;
    	int quad2 = 0;
    	int quad3 = 0;
    	int quad4 = 0;
    	
    	
    	int score = 0;
    	/*Quadrant 1:
    	 * (0,0),(0,1),(0,2),
    	 * (1,0),(1,1),(1,2),
    	 * (2,0),(2,1),(2,2)
    	 * 
    	 * 
    	 * Quadrant 2:
    	 * 
    	 * (0,3),(0,4),(0,5)
    	 * (1,3),(1,4),(1,4)
    	 * (2,3),(2,4),(2,5)
    	 * 
    	 * 
    	 * 
    	 * Quadrant 3:
    	 * 
    	 * (3,0),(3,1),(3,2)
    	 * (4,0),(4,1),(4,2)
    	 * (5,0),(5,1),(5,2)
    	 * 
    	 * 
    	 * Quadrant 4:
    	 *
    	 * (3,3),(3,4),(3,5)
    	 * (4,3),(4,4),(4,5)
    	 * (5,3),(5,4),(5,5) 
    	 * 
    	 */
    	int sequenceHorizontal = 0;
    	int sequenceVertical = 0;
    	for (int i = 0; i < 3; i++) { //case for Quad 1
    		
    		
    		sequenceHorizontal = 0;// reinitialize the sequence to 0
    		sequenceVertical = 0;
    		
    		
    		for (int j = 0; j<3; j++) {
    			
    			if (p == state.getPieceAt(i, j)) sequenceHorizontal++;
    			
    			if (p == state.getPieceAt(j,i)) sequenceVertical++;
    			
    			
    		}
    		// means we have completed a line
    		if (sequenceHorizontal > 1) {
    			
    			score += (int) Math.pow(sequenceHorizontal, 3);
    			
    		}
    		if (sequenceVertical > 1) {
    			
    			score += (int) Math.pow(sequenceVertical, 3);
    		}
    		if (sequenceHorizontal == 3 || sequenceVertical == 3) {
    			
    			quad1 ++;
    		}
    		
    	}
    	if (quad1 > 0) quad1 = 1;
    	
    	/* QUADRANT 2 in the HORIZONTAL 
    	 * AND
    	 * QUADRANT 3 in the VERTICAL
    	 * */
    	for (int i = 0; i < 3; i++) { //case for Quad 2
    		
    		
    		sequenceHorizontal = 0;// reinitialize the sequence to 0
    		sequenceVertical = 0;
    		
    		
    		for (int j = 3; j<6; j++) {
    			
    			if (p == state.getPieceAt(i, j)) sequenceHorizontal++;
    			
    			if (p == state.getPieceAt(j,i)) sequenceVertical++;
    			
    			
    		}
    		// means we have completed a line
    		if (sequenceHorizontal > 1) {
    			
    			score += (int) Math.pow(sequenceHorizontal, 3);
    			
    			if (sequenceHorizontal == 3) quad2++;
    			
    		}
    		if (sequenceVertical > 1) {
    			
    			score += (int) Math.pow(sequenceVertical, 3);
    			
    			if (sequenceVertical == 3) quad3++;
    		
    		}
    		

    	}
    	
    	if (quad2 > 0) quad2 = 1;
    	if (quad3 > 0) quad3 = 1;
    	
    	/*
    	 * QUADRANT 3 in the HORIZONTAL
    	 * AND
    	 * QUADRANT 2 in the VERTICAL
    	 * 
    	 */
    	
    	for (int i = 3; i < 6; i++) {
    		
    		
    		sequenceHorizontal = 0;// reinitialize the sequence to 0
    		sequenceVertical = 0;
    		
    		
    		for (int j = 0; j<3; j++) {
    			
    			if (p == state.getPieceAt(i, j)) sequenceHorizontal++;
    			
    			if (p == state.getPieceAt(j,i)) sequenceVertical++;
    			
    			
    		}
    		// means we have completed a line
    		if (sequenceHorizontal > 1) {
    			
    			score += (int) Math.pow(sequenceHorizontal, 3);
    			if (sequenceHorizontal == 3 && quad3 == 0) quad3++;
    			
    		}
    		if (sequenceVertical > 1) {
    			
    			score += (int) Math.pow(sequenceVertical, 3);
    			if (sequenceVertical == 3 && quad2 == 0) quad2++;
    		}
    		
    	}    	
    	if (quad3 > 0) quad3 = 0;
    	if (quad2 > 0) quad2 = 0;
    	
    	/*FINAL CASE:
    	 * QUADRANT 4
    	 */
    	
    	for (int i = 3; i < 6; i++) { //case for Quad 1
    		
    		
    		sequenceHorizontal = 0;// reinitialize the sequence to 0
    		sequenceVertical = 0;
    		
    		
    		for (int j = 3; j<6; j++) {
    			
    			if (p == state.getPieceAt(i, j)) sequenceHorizontal++;
    			
    			if (p == state.getPieceAt(j,i)) sequenceVertical++;
    			
    			
    		}
    		// means we have completed a line
    		if (sequenceHorizontal > 1) {
    			
    			score += (int) Math.pow(sequenceHorizontal, 3);
    			
    		}
    		if (sequenceVertical > 1) {
    			
    			score += (int) Math.pow(sequenceVertical, 3);
    		}
    		
    		if (sequenceHorizontal == 3 || sequenceVertical == 3) quad4++;
    		
    		
    		
    	}
    	
    	if (quad4 > 0) quad4 = 1;
    	
    	int numSequencesInQuads = quad1+quad2+quad3+quad4;
    	if (numSequencesInQuads > 1) score += Math.pow(numSequencesInQuads, 3);
    	
    	score += scoreMatchingPieces(state, p);
    	
    	return score;
    	
    }
    
    
    private static int scoreMatchingPieces(PentagoBoardState state, Piece p) {
    	
    	int horizontalTop = 0;
    	int horizontalMid = 0;
    	int horizontalLower = 0;
    	int verticalLeft = 0;
    	int verticalMid = 0;
    	int verticalRight = 0;
    	int diagonalRight = 0;
    	int diagonalLeft = 0;
    	
    	
    	for (int i = 0; i < 6; i++) {
    		for (int j = 0; j < 6; j++) {
    			
    			if (state.getPieceAt(i, j) == p) {
    				
    				if (j == 0 || j == 3) verticalLeft ++;
    				if (j == 1 || j == 4) verticalMid ++;
    				if (j == 2 || j == 5) verticalRight ++;
    				
    				if (i == 0 || i == 3) horizontalTop ++;
    				if (i == 1 || i == 4) horizontalMid ++;
    				if (i == 2 || i == 5) horizontalLower ++;
    				
    				
    				if (i == j || j == (i-3) || i == (j-3)) diagonalRight ++;
    				if ( (i == 0 && j == 2) || (i == 1 && j==1) || (i == 2 && j == 0)
    						|| (i == 0 && j == 5) || (i == 1 && j == 4) || (i == 2 && j == 3)
    						|| (i == 3 && j == 2) || (i == 4 && j == 1) || (i == 5 && j == 0) 
    						|| (i == 3 && j == 5) || (i == 4 && j == 4) || (i == 5 && j == 3)) {
    					
    					diagonalLeft ++;
    				}
    					
    				
    					
    				/*back diagonal is comprised of:
    				 * 
    				 * (0,2),(1,1),(2,0)
    				 * (0,5),(1,4),(2,3)
    				 * (3,2),(4,1),(5,0)
    				 * (3,5),(4,4),(5,3)
    				 * 
    				 * 
    				 */
    			}
    			
    			
    			
    			
    			
    		}
    		
    		
    	}
    	
    	horizontalTop = (int) Math.pow(horizontalTop, 1.5);
    	horizontalMid = (int) Math.pow(horizontalMid, 1.5);
    	horizontalLower = (int) Math.pow(horizontalLower, 1.5);
    	verticalLeft = (int) Math.pow(verticalLeft, 1.5);
    	verticalMid = (int) Math.pow(verticalMid, 1.5);
    	verticalRight = (int) Math.pow(verticalRight, 1.5);
    	diagonalRight = (int) Math.pow(diagonalRight, 1.5);
    	diagonalLeft = (int) Math.pow(diagonalLeft, 1.5);
    	
    	return horizontalTop + horizontalMid + horizontalLower + verticalLeft + verticalMid + verticalRight + diagonalRight + diagonalLeft;
    }
    
   
}