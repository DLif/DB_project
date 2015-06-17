package hangman.core.clues;

/**
 * enum that holds all clue types and their
 * SQL query format string to construct a SQL query
 * to retrieve the corresponding clue from the DB. 
 */
public enum ClueType {

	BATTLE("SELECT MilitaryAction.Name ,MilitaryAction.Year ,MilitaryAction.Month,MilitaryAction.Day ,MilitaryAction.PopularityRating ,MilitaryAction.idMilitaryAction "
					+ "FROM MilitaryAction ,Battle ,MilitaryActionLocations "
					+ "WHERE MilitaryActionLocations.idAdministrativeDivision=%s "
					+ "AND MilitaryAction.idMilitaryAction=MilitaryActionLocations.idMilitaryAction "
					+ "AND Battle.idBattle=MilitaryAction.idMilitaryAction "
					+ "ORDER BY MilitaryAction.PopularityRating DESC"),

	WAR("SELECT MilitaryAction.Name ,MilitaryAction.Year ,MilitaryAction.Month,MilitaryAction.Day ,MilitaryAction.PopularityRating ,MilitaryAction.idMilitaryAction  "
					+ "FROM MilitaryAction ,War ,MilitaryActionLocations "
					+ "WHERE MilitaryActionLocations.idAdministrativeDivision=%s "
					+ "AND MilitaryAction.idMilitaryAction=MilitaryActionLocations.idMilitaryAction "
					+ "AND War.idWar=MilitaryAction.idMilitaryAction "
					+ "ORDER BY MilitaryAction.PopularityRating DESC"),

	MOTTO( "SELECT Motto ,AdministrativeDivision.idAdministrativeDivision " 
			+ "FROM AdministrativeDivision "
			+ "WHERE idAdministrativeDivision=%s"),

	BUILDING( "SELECT Construction.Name ,Construction.idConstruction "
			+ "FROM Construction "
			+ "WHERE Construction.idAdministrativeDivision=%s "),

	CURRENCY( "SELECT Currency.Name ,Currency.idCurrency "
			+ "FROM Currency, Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND Country.idCurrency=Currency.idCurrency "),

	CONTINENT( "SELECT Continent.Name ,Continent.idContinent " 
			+ "FROM Continent , Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND Continent.idContinent=Country.idContinent "),

	LANGUAGE( "SELECT Language.Name ,Language.idLanguage "
			+ "FROM Language , LanguagesInCountries , Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND LanguagesInCountries.idCountry=Country.idCountry "
			+ "AND LanguagesInCountries.idLanguage=Language.idLanguage "),

	FOUNDATION( "SELECT FoundingYear ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s "),

	DESTRUCTION( "SELECT DestructionYear ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s "),

	POPULATION( "SELECT Population ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s "),

	CAPITAL("SELECT capital.Name ,capital.idAdministrativeDivision "
					+ "FROM AdministrativeDivision AS location , AdministrativeDivision AS capital , Country ,City "
					+ "WHERE location.idAdministrativeDivision=%s "
					+ "AND Country.idCountry=location.idAdministrativeDivision "
					+ "AND Country.idCapitalCity=City.idCity "
					+ "AND City.idCity=capital.idAdministrativeDivision"),

				
	COUNTRY("SELECT AdministrativeDivision.Name ,AdministrativeDivision.idAdministrativeDivision "
					+ "FROM  Country ,City ,AdministrativeDivision "
					+ "WHERE City.idCity=%s "
					+ "AND Country.idCountry=City.idCountry "
					+ "AND AdministrativeDivision.idAdministrativeDivision=Country.idCountry"),
					

	LEADER("SELECT Leader.FullName ,Leader.BirthYear,Leader.DeathYear,Leader.PopularityRating ,Leader.idLeader "
					+ "FROM  AdministrativeDivisionLeader ,Leader "
					+ "WHERE AdministrativeDivisionLeader.idAdministrativeDivision=%s "
					+ "AND AdministrativeDivisionLeader.idLeader=Leader.idLeader "
					+ "ORDER BY Leader.PopularityRating DESC"),
					
	PRATICIPANTS("SELECT MilitaryAction.Name ,MilitaryAction.PopularityRating ,MilitaryAction.idMilitaryAction "
					+ "FROM MilitaryAction ,MilitaryActionParticipants "
					+ "WHERE MilitaryActionParticipants.idAdministrativeDivision=%s "
					+ "AND MilitaryAction.idMilitaryAction=MilitaryActionParticipants.idMilitaryAction "
					+ "ORDER BY MilitaryAction.PopularityRating DESC"),
					
	ISCOUNTRY("SELECT * "
					+ "FROM Country "
					+ "WHERE Country.idCountry=%s "),
	
	ISCITY("SELECT * "
					+ "FROM City "
					+ "WHERE City.idCity=%s "),
	
	NECURRENCY("SELECT Currency.Name ,Currency.idCurrency "
			+ "FROM Currency, Country,City "
			+ "WHERE Country.idCountry=City.idCountry "
			+ "AND City.idCity=%s "
			+ "AND Country.idCurrency=Currency.idCurrency "),
			
	NECONTINENT( "SELECT Continent.Name ,Continent.idContinent " 
					+ "FROM Continent , Country ,City "
					+ "WHERE Country.idCountry=City.idCountry "
					+ "AND City.idCity=%s "
					+ "AND Continent.idContinent=Country.idContinent "),

	NELANGUAGE( "SELECT Language.Name ,Language.idLanguage "
					+ "FROM Language , LanguagesInCountries , Country ,City "
					+ "WHERE Country.idCountry=City.idCountry "
					+ "AND City.idCity=%s "
					+ "AND LanguagesInCountries.idCountry=Country.idCountry "
					+ "AND LanguagesInCountries.idLanguage=Language.idLanguage "),
					
	BORNIN("SELECT Leader.FullName ,Leader.idLeader "
			+ "FROM Leader "
			+ "WHERE Leader.BornIn=%s "),
			
	DIEDIN("SELECT Leader.FullName ,Leader.idLeader "
			+ "FROM Leader "
			+ "WHERE Leader.DiedIn=%s ");

	/**
	 * Stores the SQL query to construct the clue
	 */
	public String constructionQuery;

	private ClueType(String sqlQ) {

		this.constructionQuery = sqlQ;
	}

}