package student_player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
// Put outcomes file as the first argument (default file is "outcome.txt") 
// and Student ID/player name as second argument in the run configuration, 
// or just hard-code it down there. Whatever you feel like.
//
// Outcomes file is located in the logs folder, you should clean it up 
// every time you make modifications to your AI.
*/

public class WinRateCalc {
	 
	public static String log_dir = boardgame.Server.log_dir;
	
	public static void main(String[] args) {
		String file = args[0];
		String player = args[1];
		
		ArrayList<Integer> lost_games = new ArrayList<>();
		
		try {
			FileReader fileReader = new FileReader(log_dir + "/" + file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line = null;
            
            int num_wins = 0;
            int num_draws = 0;
            int total_games = 0;
            
            while((line = bufferedReader.readLine()) != null) {
            	total_games++;
            	
            	String[] tokens = line.split(",");
                if(tokens[4].equals(player)) {
                	num_wins++;
                } else if (tokens[4].equals("NOBODY")) {
                	num_draws++;
                } else {
                	lost_games.add(total_games);
                }    
            } 
            
            System.out.printf("Won %d out of %d (%.2f%%) games, plus %d draws (%.2f%%), total win+draw vs loss: %d out of %d (%.2f%%)\n", 
            				  num_wins, total_games, (float)(num_wins*100)/total_games, 
            				  num_draws, (float)(num_draws*100)/total_games, 
            				  num_wins+num_draws, total_games, (float)((num_wins+num_draws)*100)/total_games);
            
            System.out.printf("Lost games: %s\n", lost_games.toString());
            bufferedReader.close();   
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
