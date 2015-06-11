package hangman.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import hangman.db.QueryExecutor;


public class GameManager {
	
	private Location currLocation ,nextLocation=null;
	private char[] letters;
	private int numOfMistakes;
	private String userName;
	private int currScore;
	private boolean gameEnd;
	private int level=65000;
	private boolean calledNext = false;


	public GameManager(){
	}

	public void startGame() throws Exception{
		prepareNextLocation();
		NextLocation();
	}
	
	public void prepareNextLocation() throws Exception{
		calledNext = false;
		do{
		nextLocation= Location.getLocation(level);
		}while(nextLocation==null);
		List<Clue> clue;
		for(ClueFormat format : ClueFormat.values()){
			clue=QueryExecutor.getClueListByType(format, nextLocation.getLocationId());
			if(clue!=null)
				nextLocation.addClueFromList(clue);
		}
		calledNext = true;
	}
		
		
	// must call prepareNextLocation before
	public void NextLocation(){

		currLocation=nextLocation;
		nextLocation=null;
		letters=new char[26];
		fillLetters(currLocation.getName());
		numOfMistakes=0;
		gameEnd=false;
	}
	
	public String getCurrentGuess() {
		String currGuess=currLocation.getName().toLowerCase();
		for(int i=0;i<letters.length;i++){
			if(letters[i]!=3)
				currGuess=currGuess.replace((char)('a'+i), '_');
		}		
		return currGuess;
	}
	
	public String getNextClue(){
		return currLocation.getNextClue();
	}
	
	public String getPrevClue(){
		return currLocation.getPrevClue();
	}

	public boolean guess(char letter){;
		switch(letters[letter-'a']){
		case 0:
			numOfMistakes++;
			if(numOfMistakes==6)gameEnd=true;
			letters[letter-'a']=2;	
		case 2:
			return false;
		case 1:
			letters[letter-'a']=3;
		case 3:
			return true;
		default:
			break;
		}
		return false;
	}

	public boolean isWin(){

		for(int i=0;i<letters.length;i++)
			if(letters[i]==1)
				return false;
		gameEnd=true;
		return true;
	}

	public int getNumOfMistakes() {
		return numOfMistakes;
	}
	
	
	private void fillLetters(String name) {
		name=name.replace(" ", "").toLowerCase();
		for(int i=0;i<name.length();i++){
			letters[name.charAt(i)-'a']=1;
		}
		setStartGuess(name);
	}
	
	private void setStartGuess(String name) {
		if(name.length()<=5)
			return;
		else if(name.length()<=10){
			addLetter();
			addLetter();
		}
		else
			addLetter();
	}

	private void addLetter() {
		List<Integer> letterInWord=new ArrayList<Integer>();
		for(int i=0;i<letters.length;i++){
			if(letters[i]==1)
				letterInWord.add(i);
		}
		Random rnd=new Random();
		letters[letterInWord.get(rnd.nextInt(letterInWord.size()))]=3;
	}
	
	public char getLetterState(String s){
		return letters[s.charAt(1)-'a'];
	}

	
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void addScore() {
		currScore=currScore+100-numOfMistakes*10;
	}
	
	public int getScore() {
		return currScore;
	}
	
	public String getRealLocation() {
		return currLocation.getName();
	}
	
	public Clue updateCurrClue(){
		return currLocation.getCurrClue();	
	}
	
	public boolean isOver(){
		return gameEnd;
	}
	
	public boolean isCalledNext() {
		return calledNext;
	}
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
