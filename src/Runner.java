import java.util.*;

public class Runner {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your username:");
		String choice = input.nextLine();;
		boolean exit = true;
		while(exit) {
			if(choice.contains(" ") || choice.equals("") || choice.contains("%")) {
				System.out.println("Please type a valid name! You can't use space, % or empty string.");
				choice = input.nextLine();
			}
			else {
				exit = false;
			}
		}
		Player player = new Player(choice);
		Players players = new Players();
		players.addPlayer(player);
		player = players.loadPlayer(player);
		Game game;
		int check = 0;
		if(player.getNumCryptogramsPlayed() > 0) {
			System.out.println("Welcome back " + player.getName() + "! Your game details are:\n");
			System.out.println("Games played: " + player.getNumCryptogramsPlayed());
			System.out.println("Games successfully completed: " + player.getNumCryptogramsCompleted());
			System.out.println("Total guesses made: " + player.getTotalGuesses());
			System.out.println("Correct guesses made: " + player.getCorrectGuesses());
			System.out.println("Accuracy: " + player.getAccuracy());
			System.out.println("Has saved game: " + player.hasSavedGame() + "\n");
		}
		while (!exit) {
			if(check < 1) {
				System.out.println(player.getName() + " would you like to (C)ontinue your saved game, play (L)etters cryptogram, play (N)umbers cryptogram, or (E)xit?");
			}
			if (input.hasNextLine()) {
				choice = input.nextLine().toUpperCase();
			}
			if(choice.equals("C") && !player.hasSavedGame()) {
				System.out.println("Error: You don't have a saved game!");
				check++;
			}
			if (choice.equals("L") || choice.equals("N") || ((choice.equals("C") && player.hasSavedGame()))) {
				exit = true;
			}
			else if (choice.equals("E")){
				players.savePlayers();
				System.out.println("Goodbye...\n");
				players.savePlayers();
				System.exit(0);
			}
			else {
				if(!choice.equals("C")) {
					System.out.println("Invalid input! Valid choices are displayed in brackets.\n");
					check++;
				}
			}
		}
		game = new Game(player,choice);
		game.playGame(game, players);
		input.close();
	}
}
