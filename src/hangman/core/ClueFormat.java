package hangman.core;

public enum ClueFormat {

	BATTLE("SELECT MilitaryAction.Name ,MilitaryAction.Year ,MilitaryAction.Month,MilitaryAction.Day ,MilitaryAction.PopularityRating ,MilitaryAction.idMilitaryAction "
					+ "FROM MilitaryAction ,Battle ,MilitaryActionLocations "
					+ "WHERE MilitaryActionLocations.idAdministrativeDivision=%s "
					+ "AND MilitaryAction.idMilitaryAction=MilitaryActionLocations.idMilitaryAction "
					+ "AND Battle.idBattle=MilitaryAction.idMilitaryAction "
					+ "ORDER BY MilitaryAction.PopularityRating DESC", 1),

	WAR("SELECT MilitaryAction.Name ,MilitaryAction.Year ,MilitaryAction.Month,MilitaryAction.Day ,MilitaryAction.PopularityRating ,MilitaryAction.idMilitaryAction  "
					+ "FROM MilitaryAction ,War ,MilitaryActionLocations "
					+ "WHERE MilitaryActionLocations.idAdministrativeDivision=%s "
					+ "AND MilitaryAction.idMilitaryAction=MilitaryActionLocations.idMilitaryAction "
					+ "AND War.idWar=MilitaryAction.idMilitaryAction "
					+ "ORDER BY MilitaryAction.PopularityRating DESC", 1),

	MOTTO( "SELECT Motto ,AdministrativeDivision.idAdministrativeDivision " 
			+ "FROM AdministrativeDivision "
			+ "WHERE idAdministrativeDivision=%s", 1),

	BUILDING( "SELECT Construction.Name ,Construction.idConstruction "
			+ "FROM Construction "
			+ "WHERE Construction.idAdministrativeDivision=%s ", 1),

	CURRENCY( "SELECT Currency.Name ,Currency.idCurrency "
			+ "FROM Currency, Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND Country.idCurrency=Currency.idCurrency ", 1),

	CONTINENT( "SELECT Continent.Name ,Continent.idContinent " 
			+ "FROM Continent , Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND Continent.idContinent=Country.idContinent ", 1),

	LANGUAGE( "SELECT Language.Name ,Language.idLanguage "
			+ "FROM Language , LanguagesInCountries , Country "
			+ "WHERE Country.idCountry=%s "
			+ "AND LanguagesInCountries.idCountry=Country.idCountry "
			+ "AND LanguagesInCountries.idLanguage=Language.idLanguage ", 1),

	FOUNDATION( "SELECT FoundingYear ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s ", 1),

	DESTRUCTION( "SELECT DestructionYear ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s ", 1),

	POPULATION( "SELECT Population ,AdministrativeDivision.idAdministrativeDivision "
			+ "FROM AdministrativeDivision "
			+ "WHERE AdministrativeDivision.idAdministrativeDivision=%s ", 1),

	CAPITAL("SELECT capital.Name ,capital.idAdministrativeDivision "
					+ "FROM AdministrativeDivision AS location , AdministrativeDivision AS capital , Country ,City "
					+ "WHERE location.idAdministrativeDivision=%s "
					+ "AND Country.idCountry=location.idAdministrativeDivision "
					+ "AND Country.idCapitalCity=City.idCity "
					+ "AND City.idCity=capital.idAdministrativeDivision", 1),

				
	COUNTRY("SELECT AdministrativeDivision.Name ,AdministrativeDivision.idAdministrativeDivision "
					+ "FROM  Country ,City ,AdministrativeDivision "
					+ "WHERE City.idCity=%s "
					+ "AND Country.idCountry=City.idCountry "
					+ "AND AdministrativeDivision.idAdministrativeDivision=Country.idCountry", 1),
					

	LEADER("SELECT Leader.FullName ,Leader.BirthYear,Leader.DeathYear,Leader.PopularityRating ,Leader.idLeader "
					+ "FROM  AdministrativeDivisionLeader ,Leader "
					+ "WHERE AdministrativeDivisionLeader.idAdministrativeDivision=%s "
					+ "AND AdministrativeDivisionLeader.idLeader=Leader.idLeader "
					+ "ORDER BY Leader.PopularityRating DESC", 1);

	public String sqlQ;
	public int numArgs;

	private ClueFormat(String sqlQ, int numArgs) {

		this.sqlQ = sqlQ;
		this.numArgs = numArgs;
	}

}