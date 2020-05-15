import java.io.*;
import java.util.*;
public class Cryptogram {
	private Random randomGenerator;
	String phrase;
	public Cryptogram(String file) {
		phrase = getRandomPhrase(file);
	}

	private String getRandomPhrase(String path){
		try{
			FileReader file = new FileReader(path);
			BufferedReader read = new BufferedReader(file);
			ArrayList<String> phrases = new ArrayList<String>();
			String line = read.readLine();
			while (line != null){
				phrases.add(line);
				line = read.readLine();
			}
			read.close();
			randomGenerator = new Random();
			int index = randomGenerator.nextInt(phrases.size());
			String phrase = phrases.get(index);
			return phrase.toUpperCase();
		}
		catch(FileNotFoundException e){
			System.out.println("File not found!");
			return null;
		}
		catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	
	public Character getPlainLetter(Character key) {
		return null;
	}
	
	public Character getPlainLetter(Integer key) {
		return null;
	}
	
	public ArrayList<String> getUsedCyphers(){
		return null;
	}
	
	public String getCodeLetter(Character letter){
		return null;
	}
	
	public String getPhrase() {
		return phrase;
	}
	
	public void setPhrase(String ph) {
		phrase = ph;
	}
	
	public ArrayList<Character> getAlphabet(){
		ArrayList<Character> alphabet = new ArrayList<Character>();
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet.add(c);
		}
		return alphabet;
	}

	public void setLCryptogramAlphabet(HashMap<Character, Character> map) {
	}
	
	public void setNCryptogramAlphabet(HashMap<Integer, Character> map) {
	}

	public HashMap<Character, Character> getLCryptogramAlphabet() {
		return null;
	}
	
	public HashMap<Integer, Character> getNCryptogramAlphabet() {
		return null;
	}
}