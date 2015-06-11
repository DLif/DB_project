package hangman.core;

public class ClueAddRemove_Language extends ClueAddRemove {

	public ClueAddRemove_Language(int id, int locationID,String language) {
		super(id, locationID, "Language", "The people in this place speak "+language);
		sqlRemoveOption="SELECT Language.Name AS op "
				+"FROM Language , LanguagesInCountries ,Country "
				+"WHERE Country.idCountry="+locationId
				+" AND LanguagesInCountries.idCountry=Country.idCountry "
				+"AND LanguagesInCountries.idLanguage=Language.idLanguage";
		
		sqlAddOption = "SELECT Language.Name AS op "
				+ "FROM Language , LanguagesInCountries ,Country "
				+ "WHERE Language.Name LIKE \"%s\" "
				+ "AND Language.Name NOT IN ( "+sqlRemoveOption+")";

		sqlAddUpdate="INSERT LanguagesInCountries "
				+"SELECT idLanguage , "+locationId
				+" FROM Language "
				+"WHERE Language.Name=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM LanguagesInCountries "
						+"WHERE idCountry="+locationId
						+" AND idLanguage =(SELECT idLanguage FROM Language WHERE Name =\"%s\")";
	}

}
