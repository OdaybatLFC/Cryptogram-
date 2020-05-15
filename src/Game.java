import java.util.*;
import java.text.*;
public class Game {
	
	private Player currentPlayer;
	private HashMap<String,String> guesses;
	private String type;
	private Cryptogram puzzle;
	private ArrayList<String> lettersUnmapped;
	private Stack<String> undo;
	private String currentPhrase;
	private Random randomGenerator;
	
	public Game(Player a, String cryptType) {
		currentPlayer = a;
		guesses = new HashMap<String, String>();
		undo = new Stack<String>();
		type = cryptType;
	}
	
	public void playGame(Game g, Players p) {
		generateCryptogram();
		System.out.println(lettersUnmapped);
		String choice, target;
		Scanner input = new Scanner(System.in);
		int errors = countErrors();
		while (lettersUnmapped.size() > 0 || errors > 0) {
			System.out.println("Phrase:");
			for (int i=0; i<currentPhrase.length();i++) {
				if (currentPhrase.charAt(i) == ' ') {
					System.out.print(" ");
				}
				else {
					String cypher = puzzle.getCodeLetter(currentPhrase.charAt(i));
					if (guesses.containsKey(cypher)){
						System.out.print(guesses.get(cypher));
					}
					else {
						System.out.print("("+cypher+")");
					}
				}
			}
			choice = "";
			target = "";
			System.out.println("\nEnter a cypher character followed by a letter to make a guess.");
			System.out.println("Enter 'undo' to undo your last guess.");
			System.out.println("Enter 'hint' to have a letter added for you.");
			System.out.println("Enter 'view' to view letter frequencies.");
			System.out.println("Enter 'show' if you wish to see the solution and forfeit");
			System.out.println("Enter 'top10' to check the leaderboard.");
			System.out.println("Enter 'save' to exit and save your game. Any previous save will be lost.");
			System.out.println("Enter 'load' to load your details.");
			System.out.println("Enter 'exit' to exit the game. Any progress will be lost!");
			if (input.hasNext()){
				target = input.next().toUpperCase();
			}
			if (target.equals("UNDO")) {
				undoLetter();
			}
			else if (target.equals("HINT")) {
				autoPlay();
			}
			else if(target.equals("VIEW")) {
				viewFrequencies();
			}
			else if(target.equals("SHOW")) 
			{
				System.out.println("The phrase was: " + g.getCurrentPhrase());
				System.out.println("better luck next time, goodbye...\n");
				if(currentPlayer.hasSavedGame()) {
					if(currentPlayer.getSavedGame().getPuzzle().getPhrase().equals(currentPhrase)) {
						currentPlayer.setSavedGame(null);
						p.getSavedGamePlayers().remove(currentPlayer);
						p.saveSavedGame();
					}
				}
				p.savePlayers();
				System.exit(0);
			}
			else if(target.equals("TOP10")) {
                System.out.println("Leaderboard:");
                ArrayList<Player> leaders = leaderboard(p.getAllPlayers());
                Player scorer;
                for (int i=0;i<10;i++) {
                	System.out.print((i+1)+": ");
                	if (i < leaders.size()) {
                		scorer = leaders.get(i);
                		System.out.print(scorer.getName()+" "+scorer.getNumCryptogramsCompleted());
                		if (scorer.equals(currentPlayer)){
                			System.out.print(" (YOU)");
                		}
                		System.out.println("");
                	}
                	else {
                		System.out.println("<NO PLAYER>");
                	}
                }
            }
			else if(target.equals("SAVE")) {
				if(guesses.size() == 0) {
					System.out.println("You can't save a game without any letter mapped!");
				}
				else {
					if(currentPlayer.hasSavedGame()) {
						System.out.println("Are you sure you want to overwrite your previous saved game? Y/N");
						choice = input.next().toUpperCase();
						if(choice.contentEquals("Y") || choice.contentEquals("YES")) {
							p.getSavedGamePlayers().remove(currentPlayer);
							saveGame(g,p);
							System.out.println("Game saved! Goodbye...");
							p.savePlayers();
							p.saveSavedGame();
							System.exit(0);
						}
						else if(choice.equals("NO") || choice.equals("N")) {
							System.out.println("Game not saved! Goodbye...");
							p.savePlayers();
							System.exit(0);
						}
						else {
							System.out.println("Invalid character! Please enter Y, YES, N or NO...");
						}
					}
					else {
						p.getSavedGamePlayers().remove(currentPlayer);
						saveGame(g,p);
						System.out.println("Game saved! Goodbye...");
						p.savePlayers();
						p.saveSavedGame();
						System.exit(0);
					}
				}
			}
			else if(target.equals("EXIT")) {
				System.out.println("Goodbye...\n");
				p.savePlayers();
				System.exit(0);
			}
			else if(target.equals("LOAD")) {
				Players check = new Players();
				if(check.findPlayer(currentPlayer)) {
					System.out.println("\nNickname: " + currentPlayer.getName());
					System.out.println("Games played: " + currentPlayer.getNumCryptogramsPlayed());
					System.out.println("Games successfully completed: " + currentPlayer.getNumCryptogramsCompleted());
					System.out.println("Total guesses made: " + currentPlayer.getTotalGuesses());
					System.out.println("Correct guesses made: " + currentPlayer.getCorrectGuesses());
					System.out.println("Accuracy: " + currentPlayer.getAccuracy() + "\n");
				}
				else {
					System.out.println("Error: Your details haven't been stored yet! Player created.\n");	
					p.savePlayers();
				}
			}
			else if (input.hasNext()) {
				choice = input.next().toUpperCase();
				enterLetter(target,choice);	
			}
			else {
				System.out.println("Invalid command!\n");
			}
			errors = countErrors();
			if(lettersUnmapped.size() == 0 && errors > 0) {
				System.out.println("Sorry, but you have a mistake! Try to undo any incorrect guess you have made.\n");
			}
		}
		input.close();
		if(errors == 0) 
			currentPlayer.incrementCryptogramsCompleted();
		if(currentPlayer.hasSavedGame()) {
			if(currentPlayer.getSavedGame().getPuzzle().getPhrase().equals(currentPhrase)) {
				currentPlayer.setSavedGame(null);
				p.getSavedGamePlayers().remove(currentPlayer);
				p.saveSavedGame();
			}
		}
		p.savePlayers();
		System.out.println("Congratulations, you solved the cryptogram!\nThe phrase was: '"+currentPhrase+"'!");
	}

	public HashMap<String, String> getGuesses() {
		return guesses;
	}

	public String getCurrentPhrase() {
		return currentPhrase;
	}

	public Stack<String> getUndo() {
		return undo;
	}
	
	public String getType() {
		return type;
	}

	public ArrayList<String> getLettersUnmapped() {
		return lettersUnmapped;
	}

	private Integer countErrors() {
		int errors = 0;
		String compare;
			for(int i = 0; i < undo.size(); i++) {
				compare = undo.get(i);
				if (type.equals("L")) {
					if (guesses.get(compare).charAt(0) != puzzle.getPlainLetter(compare.charAt(0))){
						errors += 1;
					}
				}
				else {
					if (guesses.get(compare).charAt(0) != puzzle.getPlainLetter(Integer.valueOf(compare))){
						errors += 1;
					}
				}
			}
		return errors;
	}
	
	public void generateCryptogram() {
		if (type.equals("L")) {
			puzzle = new LetterCryptogram("src/phrases.txt");
			currentPhrase = puzzle.getPhrase();
			lettersUnmapped = puzzle.getUsedCyphers();
			currentPlayer.incrementCryptogramsPlayed();
		}
		else if (type.equals("C")) {
			Game g = currentPlayer.getSavedGame();
			loadGame(g);
		}
		else if (type.equals("T")) {
			//test mode (not accessible to user)
			puzzle = new LetterCryptogram("src/testphrase.txt");
			type = "L";
			currentPhrase = puzzle.getPhrase();
			lettersUnmapped = puzzle.getUsedCyphers();
			currentPlayer.incrementCryptogramsPlayed();
		}
		else {
			puzzle = new NumberCryptogram("src/phrases.txt");
			currentPhrase = puzzle.getPhrase();
			lettersUnmapped = puzzle.getUsedCyphers();
			currentPlayer.incrementCryptogramsPlayed();
		}
	}
	
	public void enterLetter(String cypher, String guess) {
		if (type.equals("L")) {
			if (isValid(cypher)) {
				cypher = cypher.charAt(0)+"";
			}
			else {
				System.out.println("Invalid character!\n");
				return;
			}
		}
		else{
			try {
				Integer.parseInt(cypher);
			}
			catch (NumberFormatException e) {
				System.out.println("First parameter must be a number!\n");
				return;
			}
		}
		if (isValid(guess)) {
			guess = guess.charAt(0)+"";
		}
		else {
			System.out.println("Invalid character!\n");
			return;
		}
		if (guesses.containsKey(cypher)){
			System.out.println("Cypher character already mapped!\n");
			return;
		}
		else if (lettersUnmapped.contains(cypher)){
			lettersUnmapped.remove(cypher);
			guesses.put(cypher, guess);
			undo.push(cypher);
			if(type.equals("L")) {
				char target = cypher.charAt(0);
				if (guess.equals(puzzle.getLCryptogramAlphabet().get(target) + "")) {
					currentPlayer.incrementCorrectGuesses();
				}
			}
			if(type.equals("N")) {
				int target = Integer.parseInt(cypher);
				if(guess.equals(puzzle.getNCryptogramAlphabet().get(target) + "")) {
					currentPlayer.incrementCorrectGuesses();
				}
			}
			currentPlayer.incrementTotalGuesses();
			currentPlayer.updateAccuracy();
		}
		else {
			System.out.println("Cypher character not present in cryptogram!\n");
		}
	}
	
	public void undoLetter(){
		if (undo.isEmpty()) {
			System.out.println("No moves to undo!\n");
		}
		else {
			String target = undo.pop();
			lettersUnmapped.add(target);
			guesses.remove(target);
		}
	}
	
	public void autoPlay() {
		if (lettersUnmapped.size() > 0) {
			randomGenerator = new Random();
			int index = randomGenerator.nextInt(lettersUnmapped.size());
			String cypher = lettersUnmapped.get(index);
			String map = "";
			if (type.equals("L")) {
				map = puzzle.getLCryptogramAlphabet().get(cypher.charAt(0)) + "";
			}
			else {
				map = puzzle.getNCryptogramAlphabet().get(Integer.parseInt(cypher)) + "";
			}
			if (guesses.containsValue(map)) {
				for (String checkKey : guesses.keySet()) {
				    if (guesses.get(checkKey).equals(map)) {
				    	System.out.println(checkKey+" was incorrectly mapped to "+map+", removing!");
				    	guesses.remove(checkKey);
				    	lettersUnmapped.add(checkKey);
				    	Stack<String> hold = new Stack<String>();
				    	while (!undo.isEmpty()) {
				    		String item = undo.pop();
				    		if (item.equals(checkKey)) {
				    			break;
				    		}
				    		hold.push(item);
				    	}
				    	while (!hold.isEmpty()) {
				    		undo.push(hold.pop());
				    	}
				    	break;
				    }
				}
			}
			System.out.println(cypher+" mapped to "+map+"!");
			guesses.put(cypher, map);
			lettersUnmapped.remove(cypher);
		}
		else {
			System.out.println("All letters are already mapped, undo some if you think they're wrong!");
		}
	}
	
	private Boolean isValid(String letter) {
		if (letter.length() > 1) {
			return false;
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			if (c == letter.charAt(0)) {
				return true;
			}
		}
		return false;
	}
	
	public void saveGame(Game g, Players p) {
		currentPlayer.setSavedGame(g);
		p.addSavedGamePlayer(currentPlayer);
	}

	private void loadGame(Game g) {
		lettersUnmapped = g.getLettersUnmapped();
		undo = g.getUndo();
		currentPhrase= g.getCurrentPhrase();
		guesses= g.getGuesses();
		type = g.getType();
		puzzle = g.getPuzzle();
	}

	public void setGuesses(HashMap<String, String> gu) {
		guesses = gu;
	}

	public void setLettersUnmapped(ArrayList<String> lu) {
		lettersUnmapped = lu;
	}

	public void setUndo(Stack<String> ud) {
		undo = ud;
	}

	public void setCurrentPhrase(String cp) {
		currentPhrase = cp;
	}
	
	public void setType(String t) {
		type = t;
	}

	public Cryptogram getPuzzle() {
		return puzzle;
	}

	public void setLPuzzle(String cp, HashMap<Character, Character> hm) {
		puzzle = new LetterCryptogram("phrases.txt");
		puzzle.setPhrase(cp);
		puzzle.setLCryptogramAlphabet(hm);
	}
	
	public void setNPuzzle(String cp, HashMap<Integer, Character> hm) {
		puzzle = new NumberCryptogram("phrases.txt");
		puzzle.setPhrase(cp);
		puzzle.setNCryptogramAlphabet(hm);
	}
	
	public HashMap<String,Double> generateFrequencies(){
		HashMap<String, Double> freqs = new HashMap<String, Double>();
		double count = 0;
		double total = currentPhrase.replace(" ", "").length();
		for(String cypher : puzzle.getUsedCyphers()) {
			for(char letter : currentPhrase.toCharArray()) {
				if(letter != ' ') {
					if(puzzle.getCodeLetter(letter).equals(cypher)) {
					count++;
					}
				}
			}
			freqs.put(cypher, count*100/total);
			count = 0;
		}
		return freqs;
	}
	
	public void viewFrequencies() {
		HashMap<String, Double> freqs = generateFrequencies();
		DecimalFormat df2 = new DecimalFormat(".##");
		System.out.print("\nCryptogram: ");
		for(String cypher : puzzle.getUsedCyphers()) {
			System.out.print("('"+cypher+ "' - "+df2.format(freqs.get(cypher))+"%)");
		}
		System.out.print("\nEnglish: "+"(E - 12.02%)(T - 9.10%)(A - 8.12%)(O - 7.68%)(I - 7.31%)(N - 6.95%)");
        System.out.print("(S - 6.28%)(R - 6.02%)(H - 5.92%)(D - 4.32%)(L - 3.98%)(U - 2.88%)(C - 2.71%)(M - 2.61)");		
		System.out.print("(F - 2.30%)(Y - 2.11%)(W - 2.09%)(G - 2.03%)(P - 1.82%)(B - 1.49%)(V - 1.11%)");
		System.out.print("(K - 0.69%)(X - 0.17%)(Q - 0.11%)(J - 0.10%)(Z - 0.07%)\n");
	}
	
	public ArrayList<Player> leaderboard(ArrayList<Player> allPlayerz) {
		ArrayList<Player> allPlayers = new ArrayList<Player>();
		for (int i=0;i<allPlayerz.size();i++){
			allPlayers.add(allPlayerz.get(i));
		}
		ArrayList<Player> ordered = new ArrayList<Player>();
		Player best;
		Player current;
		while (allPlayers.size() > 0) {
			best = allPlayers.get(0);
			for (int i=0;i<allPlayers.size();i++){
				current = allPlayers.get(i);
				if (current.getNumCryptogramsCompleted()>best.getNumCryptogramsCompleted()) {
					best = current;
				}
				else if (current.getNumCryptogramsCompleted()==best.getNumCryptogramsCompleted()) {
					if (current.getAccuracy() > best.getAccuracy()) {
						best = current;
					}
				}
			}
			ordered.add(best);
			allPlayers.remove(best);
		}
		return ordered;
    }
}
