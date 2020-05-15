import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class Sprint3AutoTest {

	@Test
	public void topTen() {
		//generates dummy list of players and checks that "scoreboard" returns them in the correct order
		Player good = new Player("good");
		good.setStats(85, 1000, 500, 450, 850);
		Player better = new Player("better");
		better.setStats(90, 1000, 500, 450, 900);
		Player ok = new Player("ok");
		ok.setStats(50, 1000, 500, 250, 500);
		Player bad = new Player("bad");
		bad.setStats(0, 1000, 500, 0, 0);
		ArrayList<Player> dummyPlayers = new ArrayList<Player>();
		dummyPlayers.add(ok);
		dummyPlayers.add(good);
		dummyPlayers.add(better);
		dummyPlayers.add(bad);
		Game dummyGame = new Game(ok,"L");
		ArrayList<Player> dummyScores = dummyGame.leaderboard(dummyPlayers);
		assertEquals(better,dummyScores.get(0));
		assertEquals(good,dummyScores.get(1));
		assertEquals(ok,dummyScores.get(2));
		assertEquals(bad,dummyScores.get(3));
	}
	
	@Test
	public void hint() {
		//generates dummy game with phrase "TEST", then deliberately causes a wrong guess to be overwritten, requests a hint
		//when all letters are already mapped, and finally undoes it's wrong guess and asks the game to give it a hint again
		Player dummy = new Player("Dummy");
		Game dummyGame = new Game(dummy,"T");
		dummyGame.generateCryptogram();
		dummyGame.enterLetter(dummyGame.getPuzzle().getCodeLetter('T')+"", "S");
		dummyGame.enterLetter(dummyGame.getPuzzle().getCodeLetter('E')+"", "B");
		dummyGame.autoPlay();
		dummyGame.autoPlay();
		dummyGame.autoPlay();
		assertTrue(dummyGame.getGuesses().get(dummyGame.getPuzzle().getCodeLetter('S')+"").equals("S"));
		assertTrue(dummyGame.getGuesses().get(dummyGame.getPuzzle().getCodeLetter('T')+"").equals("T"));
		assertTrue(dummyGame.getGuesses().get(dummyGame.getPuzzle().getCodeLetter('E')+"").equals("B"));
		dummyGame.undoLetter();
		dummyGame.autoPlay();
		assertTrue(dummyGame.getGuesses().get(dummyGame.getPuzzle().getCodeLetter('E')+"").equals("E"));
	}
	
	@Test
	public void frequency() {
		//generates dummy game with phrase "TEST", and checks the frequencies of the cyphers for T, E, and S are correct
		Player dummy = new Player("Dummy");
		Game dummyGame = new Game(dummy,"T");
		dummyGame.generateCryptogram();
		HashMap<String,Double> freq = dummyGame.generateFrequencies();
		assertTrue(freq.get(dummyGame.getPuzzle().getCodeLetter('T')+"")==50.0);
		assertTrue(freq.get(dummyGame.getPuzzle().getCodeLetter('S')+"")==25.0);
		assertTrue(freq.get(dummyGame.getPuzzle().getCodeLetter('E')+"")==25.0);
	}
}
