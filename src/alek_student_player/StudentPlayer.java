package alek_student_player;

import java.util.*;
import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("26074280");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
    	//Determine my Ai's color ...
    	int turnNumber = boardState.getTurnNumber();
    	System.out.println("Turn number is: " + turnNumber + "\n");
    	int myColor = getColor();
    	if (myColor == 0) {
    		
    		MyTools.myPiece = Piece.WHITE;
    		MyTools.oppPiece = Piece.BLACK;
    	}
    	
    	else {
    		
    		MyTools.myPiece = Piece.BLACK;
    		MyTools.oppPiece = Piece.WHITE;
    	}
    	
    	Move myMove = MyTools.getHighestScoringMove(boardState, turnNumber);
	
        // Return your move to be processed by the server.
        return myMove;
    }
    
    
    
    
}