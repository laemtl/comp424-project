package student_player;

import java.util.Arrays;

import boardgame.Board;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

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

	static int[][] scores = {
		{	
			5000000,
			20,
			10,
			200,
			2000,
			20000,
			200000
		},
		{
			-10000000,
			-20,
			-10,
			-200,
			-2000,
			-20000,
			-200000
		}
	};
	
    static int value;
	private static int[][] rows_sequences;
	private static int[][] cols_sequences;
	private static int[][] diags_sequences;
	   
    static public int compute(int player_id, PentagoBoardState state) {	
    	
    	if(state.gameOver()) {
    		if(state.getWinner() == player_id) return scores[0][0];
        	if(state.getWinner() == (1-player_id)) return scores[1][0];
        	
        	// Draw
        	return 0;    
    	} 
        			    			
    	rows_sequences = new int[6][4];
    	cols_sequences = new int[6][4];
    	diags_sequences = new int[6][4];
    	
    	value = 0;
    	analizeBoard(state);
    	    	
    	// For each index of rows, cols, diags
		for (int i = 0; i < 6; i++) {
			float[] rowScore = {0, 0}, colScore = {0, 0}, diagScore = {0, 0};
			
			// score_sequences for each rows_sequences[i] = {seqlength_w, seqlenth_b, #center_w, #center_b}
	
			
			// White score
			// Positive if player_id = 0 = white
			// Negative otherwise
			
			// Center Scores
			rowScore[0] += rows_sequences[i][2] * scores[player_id][1];
			colScore[0] += cols_sequences[i][2] * scores[player_id][1];
			diagScore[0] += diags_sequences[i][2] * scores[player_id][1];
			
			// Sequence Scores
			int w_seq_length;
			w_seq_length = Math.min(5, rows_sequences[i][0]);
			rowScore[0] += scores[player_id][w_seq_length+1];
			
			w_seq_length = Math.min(5, cols_sequences[i][0]);
			colScore[0] += scores[player_id][w_seq_length+1];
			
			w_seq_length = Math.min(5, diags_sequences[i][0]);
			diagScore[0] += scores[player_id][w_seq_length+1];
			
			
			// Black score
			// Positive if player_id = 1 = black
			// Negative otherwise
			
			// Center Scores
			rowScore[1] += rows_sequences[i][3] * scores[1-player_id][1];
			colScore[1] += cols_sequences[i][3] * scores[1-player_id][1];
			diagScore[1] += diags_sequences[i][3] * scores[1-player_id][1];
			
			// Sequence Scores
			int b_seq_length;
			b_seq_length = Math.min(5, rows_sequences[i][1]);
			rowScore[1] += scores[1-player_id][b_seq_length+1];
			
			b_seq_length = Math.min(5, cols_sequences[i][1]);
			colScore[1] += scores[1-player_id][b_seq_length+1];
			
			b_seq_length = Math.min(5, diags_sequences[i][1]);
			diagScore[1] += scores[1-player_id][b_seq_length+1];
			
			// score if we win
			
			value += (rowScore[0] + colScore[0] + diagScore[0]);
			value += (rowScore[1] + colScore[1] + diagScore[1]);


		}
		
		return value;
	}	
	    
    private static void analizeBoard(PentagoBoardState state) {
    	checkRows(state);
        checkCols(state);
    	checkDiags(state);
    	checkTrickyDiags(state);
	}
    
    private static void checkDiags(PentagoBoardState state) {
    	// Diags 0, 1, 2
    	// Diagonals from up to bottom
    	// Diags 3, 4, 5
    	// AntiDiagonals from up to bottom
    	
		Piece[] p_diag = new Piece[6];
		Piece[] p_adiag = new Piece[6];
		
		for (int i = 0; i < 6; i++) {
			p_diag[i] = state.getPieceAt(i, i);
			p_adiag[i] = state.getPieceAt(5-i, i);  
		}
		
		Sixplet s_diag = new Sixplet(p_diag);
		s_diag.extractSequences(diags_sequences[1]);
		
		Sixplet s_adiag = new Sixplet(p_adiag);
		s_adiag.extractSequences(diags_sequences[4]);
	}

    private static void checkTrickyDiags(PentagoBoardState state) {
    	Piece[] p_diag0 = new Piece[5];
    	Piece[] p_diag1 = new Piece[5];
		
    	Piece[] p_adiag0 = new Piece[5];
    	Piece[] p_adiag1 = new Piece[5];

		for (int i = 0; i < 5; i++) {
			p_diag0[i] = state.getPieceAt(i, i+1);
			p_diag1[i] = state.getPieceAt(i+1, i);
			
			p_adiag0[i] = state.getPieceAt(4-i, i);
			p_adiag1[i] = state.getPieceAt(5-i, i+1); 
		}
		
		Quintuplet s_diag0 = new Quintuplet(p_diag0);
		s_diag0.extractSequences(diags_sequences[0]);

		Quintuplet s_diag1 = new Quintuplet(p_diag1);
		s_diag1.extractSequences(diags_sequences[2]);

		Quintuplet s_adiag0 = new Quintuplet(p_adiag0);
		s_adiag0.extractSequences(diags_sequences[3]);  
		
		Quintuplet s_adiag1 = new Quintuplet(p_adiag1);
		s_adiag1.extractSequences(diags_sequences[5]); 
	}

	private static void checkCols(PentagoBoardState state) {
		Piece[] pieces = new Piece[6];
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				pieces[j] = state.getPieceAt(j, i);
			}
			
			Sixplet s = new Sixplet(pieces);
			s.extractSequences(cols_sequences[i]);  
		}
	}

	private static void checkRows(PentagoBoardState state) {
		Piece[] pieces = new Piece[6];
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				pieces[j] = state.getPieceAt(i, j);
			}
			
			Sixplet s = new Sixplet(pieces);
			s.extractSequences(rows_sequences[i]);  
		}
	}
}

class Quintuplet {
	Piece[] quintuplet;
	int[] count;
	
	public Quintuplet(Piece[] pieces) {
		if(pieces.length != 5) throw new IllegalArgumentException("You must pass 5 pieces to a quintuplet.");
		quintuplet = pieces;
		
		count = new int[2];
		for (int i = 0; i < quintuplet.length; i++) {
			if(quintuplet[i].toString().equals("w")) count[0] += 1;
			if(quintuplet[i].toString().equals("b")) count[1] += 1;
		}
	}
	
	public void extractSequences(int[] sequences) {		
		// Cannot make 5 in a row, ignore
		if(getwCount() > 0 && getbCount() > 0) return;
		
		sequences[0] = getwCount();
		sequences[1] = getbCount();	
	}

	int getwCount() {
		return count[0];  
	}
	
	int getbCount() {
		return count[1]; 
	}
}

class Sixplet {
	Triplet t1, t2;
	
	public Sixplet(Piece[] pieces) {
		if(pieces.length != 6) throw new IllegalArgumentException("You must pass 6 pieces to a sixplet.");
		t1 = new Triplet(pieces[0], pieces[1], pieces[2]);
		t2 = new Triplet(pieces[3], pieces[4], pieces[5]);
	}
	
	public void extractSequences(int[] sequences) {
		// Empty sixplet
		if(getwCount() == 0 && getbCount() == 0) return;
			
		int wCentersCount = getwCentersCount();
		int bCentersCount = getbCentersCount();
		sequences[2] = wCentersCount;
		sequences[3] = bCentersCount;
				
		// Cannot make 5 in a row, ignore
		if(getwCount() > 1 && getbCount() > 1) return;
		
		// If centers of different colors
		// Blocking stone cannot be in the center, otherwise ignore
		if(bCentersCount == 1 && wCentersCount == 1) return; 
		
		if(getwCount() == 1 && getbCount() == 1) {
			// If one stone in a center, it discards the other
			if(wCentersCount == 1) {
				sequences[0] += 1;
			} else if(bCentersCount == 1) {
				sequences[1] += 1;
			} else {
				// if they are both on the right/left position, they both count
				sequences[0] += 1;
				sequences[1] += 1;
			}
			
			return;
		}
		
		if(getwCount() > getbCount()) {
			// color = white
			// We have a white sequence
			// Count consecutive color : getwCount(true)

			if(t1.getwCount() == 3 || t2.getwCount() == 3) {
				sequences[0] = t1.getwCount(true) + t2.getwCount(true);
				
				return;
			} else {
				sequences[0] = Math.max(t1.getwCount(true), t2.getwCount(true));
			}
		} else {
			// color = black
			// We have a black sequence
			// Count consecutive color : getbCount(true)

			if(t1.getbCount() == 3 || t2.getbCount() == 3) {
				sequences[1] = t1.getbCount(true) + t2.getbCount(true);
				
				return;
			} else {
				sequences[1] = Math.max(t1.getbCount(true), t2.getbCount(true));
			}
		}	
	}
	
	private int getwCentersCount() {
		int centersCount = 0;
		
		if(t1.getCenter().equals("w")) {
			centersCount += 1;
		}
		
		if(t2.getCenter().equals("w")) {
			centersCount += 1;
		}
		
		return centersCount;
	}
	
	private int getbCentersCount() {
		int centersCount = 0;
		
		if(t1.getCenter().equals("b")) {
			centersCount += 1;
		}
		
		if(t2.getCenter().equals("b")) {
			centersCount += 1;
		}
		
		return centersCount;
	}

	int getwCount() {
		return t1.getwCount() + t2.getwCount();  
	}
	
	int getbCount() {
		return t1.getbCount() + t2.getbCount();
	}
}

enum TripletValue {
	___, 
	b__, _b_, __b, bb_, b_b, _bb, bbb, 
	bw_, b_w, bww, wb_, _bw, wbw, w_b,
	_wb, wwb, bbw, bwb, wbb, 
	w__, _w_, __w, ww_, w_w, _ww, www;
}

class Triplet {
	Piece[] triplet;	
	TripletValue tv;
	int[] count;
	
	public Triplet(Piece p1, Piece p2, Piece p3) {
		triplet = new Piece[3];
		
		triplet[0] = p1;
		triplet[1] = p2;
		triplet[2] = p3;
		
		tv = TripletValue.valueOf("" 
		+ p1.toString().replace(" ", "_") 
		+ p2.toString().replace(" ", "_") 
		+ p3.toString().replace(" ", "_"));
		
		count = new int[2];
		for (int i = 0; i < triplet.length; i++) {
			if(triplet[i].toString().equals("w")) {
				count[0] += 1;
			}
			if(triplet[i].toString().equals("b")) {
				count[1] += 1;
			}
		}
	}
	
	TripletValue getTripletValue() {
		return tv;
	}
	
	int getwCount() {
		return getwCount(false);
	}
	
	int getwCount(boolean seq) {
		if(seq && tv == TripletValue.valueOf("w_w")) return 1;
		
		return count[0];
	}
	
	int getbCount() {
		return getbCount(false);
	}
	
	int getbCount(boolean seq) {
		if(seq && tv == TripletValue.valueOf("b_b")) return 1;
		
		return count[1];
	}
	
	String getCenter() {
		return triplet[1].toString();
	}
		
	@Override
	public String toString() {
		return triplet[0].toString() + triplet[1].toString() + triplet[2].toString();
	}
}
