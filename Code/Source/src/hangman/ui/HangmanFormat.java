package hangman.ui;

public class HangmanFormat {
	
	
	public static String getFormat(int mistakes){
		if(mistakes==0){
			return  "_________" 		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"|      "		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"=========";
		} else if(mistakes==1){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"=========";
		} else if(mistakes==2){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|      0 "		+"\n"+
					"|      "		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"=========";
		} else if(mistakes==3){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|      0 "		+"\n"+
					"|      / "		+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"=========";
		} else if(mistakes==4){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|      0 "		+"\n"+
					"|      / \\"	+"\n"+
					"|       "		+"\n"+
					"|      "		+"\n"+
					"=========";
		} else if(mistakes==5){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|      0 "		+"\n"+
					"|      / \\"	+"\n"+
					"|       |"		+"\n"+
					"|      / "		+"\n"+
					"=========";
		} else if(mistakes==6){
			return  "_________" 		+"\n"+
					"|       |"		+"\n"+
					"|      0 "		+"\n"+
					"|      / \\"	+"\n"+
					"|       |"		+"\n"+
					"|      / \\"	+"\n"+
					"=========";
		}
		
		return "";
	}

}
