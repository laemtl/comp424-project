package student_player;

import java.util.Comparator;
import pentago_swap.PentagoMove;

public class MiniMaxTree extends PBSTree  implements Comparable<MiniMaxTree>, Comparator<PentagoMove> {
	private int score;
	private int player_id;
	
	public MiniMaxTree(int player_id, PBSTree tree, int score) {
		super(tree);
		this.score = score;
		this.player_id = player_id;
	}
	
	public MiniMaxTree(int player_id, PBSTree tree) {
		this(player_id, tree, Integer.MIN_VALUE);
	}
	
	public MiniMaxTree(int player_id, MiniMaxTree tree) {
		this(player_id, tree, tree.getScore());
	}
	
	public int getScore() {
		if(score == Integer.MIN_VALUE){
			score = HeuristicFunction.compute(player_id, getState());
		}
		
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(MiniMaxTree item) {
		if (getScore() > item.getScore()) {
    		return 1;
    	} else if(getScore() < item.getScore()) {
		    return -1;
    	} else {
    		return compare(getMove(), item.getMove());
    	}
	}
	
	@Override
	public int compare(PentagoMove m1, PentagoMove m2) {
		if(IsBlockcenter(m1) && IsBlockcenter(m2)) return 0;
		if(IsBlockcenter(m1)) return 1;
		if(IsBlockcenter(m2)) return -1;
		
		if(IsTripletcenter(m1) && IsTripletcenter(m2)) return 0;
		if(IsTripletcenter(m1)) return 1;
		if(IsTripletcenter(m2)) return -1;
		
		return 0;
	}
	
	private boolean IsTripletcenter(PentagoMove m) {
		if(m.getMoveCoord().getX() == 1
		|| m.getMoveCoord().getX() == 4
		|| m.getMoveCoord().getY() == 1
		|| m.getMoveCoord().getY() == 4
		) return true;
		
		return false;
	}
	
	private boolean IsBlockcenter(PentagoMove m) {
		if((m.getMoveCoord().getX() == 1 
		|| m.getMoveCoord().getX() == 4)
		&& (m.getMoveCoord().getY() == 1 
		|| m.getMoveCoord().getY() == 4)
		) return true;
		
		return false;
	}	
}