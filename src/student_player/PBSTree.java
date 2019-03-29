package student_player;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class PBSTree implements Cloneable {

	private PentagoBoardState state;
    private List<PBSTree> children;
	private PentagoMove move;
	
	public PBSTree(PentagoBoardState state) {
		this(state, null);
    }
	
	public PBSTree(PBSTree tree) {
		if(tree == null) throw new InvalidParameterException("Tree cannot be null");
		
        this.state = tree.state;
        this.children = tree.children;
        this.move = tree.move;
    } 
	
	public PBSTree(PentagoBoardState state, PentagoMove move) {
        this.state = state;
        this.children = null;
        this.move = move;
    } 

    @Override
	protected PBSTree clone() throws CloneNotSupportedException {
		return (PBSTree)super.clone();
	}

	public List<PBSTree> getChildren() {
    	if(children == null) generateChildren();
    	return this.children;
    }
    
	private void generateChildren() {
		children = new ArrayList<PBSTree>();
		
		if(state.gameOver()) return;
			
		for (PentagoMove move : MyTools.getPossibleMoves(state)) {
			PentagoBoardState newState = (PentagoBoardState) state.clone();
			newState.processMove(move);	
			children.add(new PBSTree(newState, move));
		}
	}
	
	/*public PBSTree getMaxChildren() {
		return Collections.max(getChildren(), new Comparator<PBSTree>() {
			@Override
			public int compare(PBSTree a, PBSTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}
	
	public PBSTree getMinChildren() {
		return Collections.min(getChildren(), new Comparator<PBSTree>() {
			@Override
			public int compare(PBSTree a, PBSTree b) {
				return Integer.compare(a.getScore(), b.getScore());
			}
		});
	}*/
	
	public PentagoBoardState getState() {
		return state;
	}
	
	public PentagoMove getMove() {
		return move;
	}
	
	public void setMove(PentagoMove move) {
		this.move = move;
	}
}