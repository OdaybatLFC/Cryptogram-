import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NumberCryptogram extends Cryptogram {
	
	private Random randomGenerator;
	HashMap<Integer,Character> cryptogramAlphabet;
	ArrayList<Integer> codedPhrase;
	public NumberCryptogram(String file) {
		super(file);
		randomGenerator = new Random();
		cryptogramAlphabet = new HashMap<Integer,Character>(26);
		ArrayList<Character> alphabet = getAlphabet();
		int mapTo;
		for (int i=1;i<=26;i++) {
			do{
				mapTo = randomGenerator.nextInt(alphabet.size());
			}while(alphabet.get(mapTo) == i+65);
			cryptogramAlphabet.put(i, alphabet.get(mapTo));
			alphabet.remove(mapTo);
		}
	}
	
	public Character getPlainLetter(Integer key) {
		return cryptogramAlphabet.get(key);
	}
	
	public String getCodeLetter(Character letter) {
		for (int i=1; i<=cryptogramAlphabet.size();i++) {
			if (cryptogramAlphabet.get(i) == letter) {
				return Integer.toString(i);
			}
		}
		return null;
	}
	
	public ArrayList<String> getUsedCyphers(){
		ArrayList<String> usedCyphers = new ArrayList<String>();
		for (int i=1; i<=cryptogramAlphabet.size();i++) {
			//System.out.print(cryptogramAlphabet.get(i)+",");
			if (phrase.contains(cryptogramAlphabet.get(i)+"")) {
				usedCyphers.add(Integer.toString(i));
			}
		}
		return usedCyphers;
	}
	
	public void setNCryptogramAlphabet(HashMap<Integer, Character> map) {
		cryptogramAlphabet = map;
	}
	
	public HashMap<Integer, Character> getNCryptogramAlphabet() {
		return cryptogramAlphabet;
	}
}