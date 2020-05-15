import java.io.*;
import java.util.*;

public class Players {
	
	private ArrayList<Player> allPlayers;
	private FileReader playersFile;
	private ArrayList<Player> savedGamePlayers;
	
	public Players() {
	
		allPlayers = new ArrayList<>();
		savedGamePlayers = new ArrayList<>();
		try {
			playersFile = new FileReader("players.txt");
			BufferedReader read = new BufferedReader(playersFile);
			String line = read.readLine();
			while(line != null) {
				Player p;
				String[]info = line.split(" ");
				p = new Player(info[0]);
				double acc = Double.parseDouble(info[1]);
				int tg = Integer.parseInt(info[2]); 
				int cp = Integer.parseInt(info[3]);
				int cc = Integer.parseInt(info[4]);
				int cg = Integer.parseInt(info[5]);
				p.setStats(acc, tg, cp, cc, cg);	
				allPlayers.add(p);
				line = read.readLine();
			}
			read.close();
		} catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
		catch(Exception e){
			System.out.println(e);
		}
		try {
			FileReader savedGameFile = new FileReader("savedgame.txt");
			BufferedReader read = new BufferedReader(savedGameFile);
			String line = read.readLine();
			while(line != null) {
				for(Player p: allPlayers) {
					String[]info = line.split("%");
					if(p.getName().equals(info[0])) {
						Game g = new Game(p, info[1]);
						p.setSavedGame(g);
						savedGamePlayers.add(p);
						String[]info2 = info[2].split(", ");
						HashMap<String, String> map = new HashMap<String, String>();
						if(info2.length == 1 ) {
							String[]str = info2[0].split("[{]");
							String[]str2 = str[1].split("[}]");
							String[]str3 = str2[0].split("=");
							map.put(str3[0], str3[1]);
						}
						else {
							for(int i = 0; i < info2.length; i++) {
								if(i == 0) {
									String[]str = info2[i].split("[{]");
									String[]str2 = str[1].split("=");
									map.put(str2[0], str2[1]);
								}
								else if(i == info2.length-1) {
									String[]str = info2[i].split("[}]");
									String[]str2 = str[0].split("=");
									map.put(str2[0], str2[1]);
								}
								else {
									String[]str = info2[i].split("=");
									map.put(str[0], str[1]);
								}
							}
						}
						ArrayList<String> lu = new ArrayList<String>();
						String[]info3 = info[3].split(", ");
						if(info3.length == 1) {
							String[]str = info3[0].split("\\[");
							String[]str2 = str[1].split("\\]");
							lu.add(str2[0]);
						}
						else {
							for(int i = 0; i < info3.length; i++) {
								if(i == 0) {
									String[]str = info3[i].split("\\[");
									lu.add(str[1]);
								}
								else if(i == info3.length-1) {
									String[]str = info3[i].split("\\]");
									lu.add(str[0]);
								}
								else {
									lu.add(info3[i]);
								}
							}
						}
						Stack<String> ud = new Stack<String>();
						String[]info4 = info[4].split(", ");
						if(info4.length == 1) {
							String[]str = info4[0].split("\\[");
							String[]str2 = str[1].split("\\]");
							ud.add(str2[0]);
						}
						else {
							for(int i = 0; i < info4.length; i++) {
								if(i == 0) {
									String[]str = info4[i].split("\\[");
									ud.add(str[1]);
								}
								else if(i == info4.length-1) {
									String[]str = info4[i].split("\\]");
									ud.add(str[0]);
								}
								else {
									ud.add(info4[i]);
								}
							}
						}
						String cp = info[5];
						HashMap<Character, Character> hm = new HashMap<Character, Character>();
						HashMap<Integer, Character> hm1 = new HashMap<Integer, Character>();
						if(info[1].equals("L")) {
							String[]info6 = info[6].split(", ");
							for(int i = 0; i < info6.length; i++) {
								if(i == 0) {
									String[]str = info6[i].split("[{]");
									String[]str2 = str[1].split("=");
									hm.put(str2[0].toCharArray()[0], str2[1].toCharArray()[0]);
								}
								else if(i == info6.length-1) {
									String[]str = info6[i].split("[}]");
									String[]str2 = str[0].split("=");
									hm.put(str2[0].toCharArray()[0], str2[1].toCharArray()[0]);
								}
								else {
									String[]str = info6[i].split("=");
									hm.put(str[0].toCharArray()[0], str[1].toCharArray()[0]);
								}
							}
							p.setSavedGameStatsL(map, lu, ud, cp, hm);
						}
						
						if(info[1].equals("N")) {
							String[]info6 = info[6].split(", ");
							for(int i = 0; i < info6.length; i++) {
								if(i == 0) {
									String[]str = info6[i].split("[{]");
									String[]str2 = str[1].split("=");
									int a = Integer.parseInt(str2[0]);
									hm1.put(a, str2[1].toCharArray()[0]);
								}
								else if(i == info6.length-1) {
									String[]str = info6[i].split("[}]");
									String[]str2 = str[0].split("=");
									int a = Integer.parseInt(str2[0]);
									hm1.put(a, str2[1].toCharArray()[0]);
								}
								else {
									String[]str = info6[i].split("=");
									int a = Integer.parseInt(str[0]);
									hm1.put(a, str[1].toCharArray()[0]);
								}
							}
							p.setSavedGameStatsN(map, lu, ud, cp, hm1);
						}
					}
				}
				line = read.readLine();
			}
			read.close();
		  } catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void addPlayer(Player p) {
		if(!findPlayer(p)) {
			allPlayers.add(p);
		}
	}
	
	public void savePlayers() {
		try {
            File file = new File("players.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            for(Player player: allPlayers) {
    			String username = player.getName();
    			double acc = player.getAccuracy();
    			int tg = player.getTotalGuesses();
    			int cp = player.getNumCryptogramsPlayed();
    			int cc = player.getNumCryptogramsCompleted();
    			int cg = player.getCorrectGuesses();
    			output.write(username + " " + acc + " " + tg + " " + cp + " " + cc + " " + cg);
    			output.newLine();
    		}
            output.close();
        } catch ( IOException e ) {
            System.out.print(e);
        }
	}
	
	public void saveSavedGame() {
		try {
		    File file = new File("savedgame.txt");
		    BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for(Player player: savedGamePlayers) {
				Game game = player.getSavedGame();
    			String username = player.getName();
    			String t = game.getType();
    			HashMap<String,String> gu = game.getGuesses();
    			ArrayList<String> lu = game.getLettersUnmapped();
    			Stack<String> ud = game.getUndo();
    			String cp = game.getCurrentPhrase();
    			HashMap<Character, Character> map1;
    			HashMap<Integer, Character> map2;
    			if(t.equals("L")) {
    				map1 = game.getPuzzle().getLCryptogramAlphabet();
    				output.write(username + "%" + t + "%" + gu + "%" + lu + "%" + ud + "%" + cp + "%" + map1);
    			}
    			if(t.equals("N")) {
    				map2 = game.getPuzzle().getNCryptogramAlphabet();
    				output.write(username + "%" + t + "%" + gu + "%" + lu + "%" + ud + "%" + cp + "%" + map2);
    			}
    			output.newLine();
			}
			output.close();
        } catch ( IOException e ) {
            System.out.print(e);
        }
	}
	
	public boolean findPlayer(Player p) {
		String name = p.getName();
		boolean result = false;
		for(int i = 0; i < allPlayers.size(); i++) {
			if(allPlayers.get(i).getName().equals(name)) {
				result = true;
			}
		}
		return result;
	}

	public Player loadPlayer(Player p) {
		String name = p.getName();
		for(int i = 0; i < allPlayers.size(); i++) {
			if(allPlayers.get(i).getName().equals(name)) {
				p = allPlayers.get(i);
				allPlayers.set(i, p);
			}
		}
		return p;
	}

	public ArrayList<Player> getSavedGamePlayers() {
		return savedGamePlayers;
	}

	public void addSavedGamePlayer(Player p) {
		boolean result = false;
		int target = 0;
		for(int i = 0; i < savedGamePlayers.size(); i++) {
			if(p.getName().equals(savedGamePlayers.get(i).getName())) {
				result = true;
				target = i;
			}
		}
		if(result) {
			savedGamePlayers.set(target, p);
		}
		else {
			savedGamePlayers.add(p);
		}
	}
	
	public ArrayList<Double> getAllPlayersAccuracies() {
		ArrayList<Double> allPlayersAccuracies = new ArrayList<>();
		for(Player p: allPlayers) {
			allPlayersAccuracies.add(p.getAccuracy());
		}
		return allPlayersAccuracies;
	}
	
	public ArrayList<Integer> getAllPlayersCryptogramsPlayed() {
		ArrayList<Integer> allPlayersCryptogramsPlayed = new ArrayList<>();
		for(Player p: allPlayers) {
			allPlayersCryptogramsPlayed.add(p.getNumCryptogramsPlayed());
		}
		return allPlayersCryptogramsPlayed;
	}
	
	public ArrayList<Integer> getAllPlayersCompletedCryptos() {
		ArrayList<Integer> allPlayersCompletedCryptos = new ArrayList<>();
		for(Player p: allPlayers) {
			allPlayersCompletedCryptos.add(p.getNumCryptogramsCompleted());
		}
		return allPlayersCompletedCryptos;
	}

	public ArrayList<Player> getAllPlayers() {
		return allPlayers;
	}
	
}
