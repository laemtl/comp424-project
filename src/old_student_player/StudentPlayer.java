package old_student_player;

import boardgame.Move;
import pentago_swap.PentagoPlayer;
import student_player.MyTools2;
import pentago_swap.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260791354");
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
        // MyTools.getSomething();
    	
    	//Move myMove = MyTools.getMove(getColor(), boardState);
    	Move myMove = MyTools2.getMove(getColor(), boardState);
    	        
        // Return your move to be processed by the server.
        return myMove;
    }
}