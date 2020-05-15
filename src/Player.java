import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Player {
	private String username;
	private double accuracy;
	private int totalGuesses;
	private int correctGuesses;
	private int cryptogramsPlayed;
	private int cryptogramsCompleted;
	private Game savedGame;
	
	public Player(String name) {
		username = name;
		accuracy = 100;
		totalGuesses = 0;
		cryptogramsPlayed = 0;
		cryptogramsCompleted = 0;
		correctGuesses = 0;
	}
	
	public String getName() {
		return username;
	}

	public void setStats(double acc, int tg, int cp, int cc, int cg) {
		accuracy = acc;
		totalGuesses = tg;
		correctGuesses = cg;
		cryptogramsPlayed = cp;
		cryptogramsCompleted = cc;	
	}
	
	public void incrementTotalGuesses() {
		totalGuesses++;
	}
	
	public void incrementCorrectGuesses() {
		correctGuesses++;
	}
	
	public void incrementCryptogramsPlayed() {
		cryptogramsPlayed++;
	}
	
	public void incrementCryptogramsCompleted() {
		cryptogramsCompleted++;
	}
	
	public void updateAccuracy() {
		if(totalGuesses != 0) {
			double cg = correctGuesses;
			double tg = totalGuesses;
			accuracy = (cg/tg)*100;
		}
	}

	public double getAccuracy() {
		return accuracy;
	}

	public int getNumCryptogramsPlayed() {
		return cryptogramsPlayed;
	}

	public int getNumCryptogramsCompleted() {
		return cryptogramsCompleted;
	}

	public int getTotalGuesses() {
		return totalGuesses;
	}

	public boolean hasSavedGame() {
		boolean status = false;
		if(savedGame != null) {
			status = true;
		}
		return status;
	}

	public void setSavedGame(Game g) {
		savedGame = g;
	}

	public Game getSavedGame() {
		return savedGame;
	}
	
	public void setSavedGameStatsL(HashMap<String, String> gu, ArrayList<String> lu, Stack<String> ud, String cp, HashMap<Character, Character> hm) {
		savedGame.setGuesses(gu);
		savedGame.setLettersUnmapped(lu);
		savedGame.setUndo(ud);
		savedGame.setCurrentPhrase(cp);
		savedGame.setLPuzzle(cp, hm);
	}

	public void setSavedGameStatsN(HashMap<String, String> map, ArrayList<String> lu, Stack<String> ud, String cp, HashMap<Integer, Character> hm1) {
		savedGame.setGuesses(map);
		savedGame.setLettersUnmapped(lu);
		savedGame.setUndo(ud);
		savedGame.setCurrentPhrase(cp);
		savedGame.setNPuzzle(cp, hm1);
	}

	public int getCorrectGuesses() {
		return correctGuesses;
	}
	
}
