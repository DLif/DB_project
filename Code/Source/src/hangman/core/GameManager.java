package hangman.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hangman.core.clues.AbstractClue;
/**
 * 
 * @author gilifaro
 *  Runs and manage the Game
 */


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
		currScore=0;
		//System.out.println("started startGame()");
		try{
			prepareNextLocation();
		}
		catch(Exception e)
		{
			// try again, if failed on second time give up
			prepareNextLocation();
		}
		NextLocation();
		//System.out.println("end startGame()");
	}
	/**
	 * looking for new location from the DB
	 * and add to him clues
	 * @throws Exception
	 */
	public void prepareNextLocation() throws Exception{
		//System.out.println("started next location");
		calledNext = false;
		do{
			nextLocation= Location.getLocation(level);
		} while(nextLocation==null);
		//System.out.println("got location and clues");

		calledNext = true;
		//System.out.println("end next location");
	}
		
		
	/**
	 * <pre> must call prepareNextLocation before
	 * switch to the next location
	 */
	public void NextLocation(){

		currLocation=nextLocation;
		nextLocation=null;
		letters=new char[26];
		fillLetters(currLocation.getName());
		numOfMistakes=0;
		gameEnd=false;
	}
	
	/**
	 * 
	 * @return the name of the current location with only the guessed letters shown
	 */
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

	/**
	 * 
	 * @param letter - the letter we guess is in the current location name
	 * @return True iff the letter is in the current location name
	 */
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

	/**
	 * 
	 * @return True iff  we guessed all the letters in the current location name
	 */
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
	
	public AbstractClue updateCurrClue(){
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
	
	public boolean haveNextClue(){
		return currLocation.haveNextCLue();
	}
	
	public boolean havePrevClue(){
		return currLocation.havePrevCLue();
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
}
