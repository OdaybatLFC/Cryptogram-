import java.util.*;


public class LetterCryptogram extends Cryptogram {
	
	private Random randomGenerator;
	HashMap<Character,Character> cryptogramAlphabet;
	HashMap<Character,Character> reverseAlphabet;
	
	public LetterCryptogram(String file) {
		super(file);
		randomGenerator = new Random();
		cryptogramAlphabet = new HashMap<Character,Character>(26);
		ArrayList<Character> alphabet = getAlphabet();
		int mapTo;
		for (char i='A';i<='Z';i++) {
			do{
				mapTo = randomGenerator.nextInt(alphabet.size());
			}while(alphabet.get(mapTo) == i);
			cryptogramAlphabet.put(i, alphabet.get(mapTo));
			alphabet.remove(mapTo);
		}
	}
	
	public String getCodeLetter(Character letter) {
		for (char i='A'; i<='Z';i++) {
			if (cryptogramAlphabet.get(i) == letter) {
				return i+"";
			}
		}
		return null;
	}
	
	public Character getPlainLetter(Character key) {
		return cryptogramAlphabet.get(key);
	}
	
	public ArrayList<String> getUsedCyphers(){
		ArrayList<String> usedCyphers = new ArrayList<String>();
		for (char i='A'; i<='Z';i++) {
			if (phrase.contains(cryptogramAlphabet.get(i)+"")) {
				usedCyphers.add(i+"");
			}
		}
		return usedCyphers;
	}
	
	public void setLCryptogramAlphabet(HashMap<Character, Character> map) {
		cryptogramAlphabet = map;
	}
	
	public HashMap<Character, Character> getLCryptogramAlphabet() {
		return cryptogramAlphabet;
	}
}
