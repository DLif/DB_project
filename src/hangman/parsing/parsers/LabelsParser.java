package hangman.parsing.parsers;

import hangman.parsing.entities.Entity;

public class LabelsParser extends FileParser{
	
	/**
	 * This class does the parsing of the yagoLiteralFacts.tsv file.
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entity labelType label
	 * <id_3vgmh2_1sz_bfz811>	<Olmo,_Haute-Corse>	skos:prefLabel	"Olmo, Haute-Corse"@eng
	 */
	
	//this variable holds the length of the string "skos:prefLabel". it is used in the parsing
	public int prefLabelLen;

	
	public void init() {
		// fill the prefLabelLen variable
		prefLabelLen = "skos:prefLabel".length();
	}

	
	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	public void filter(String line) {
		int indexPrefLabel = line.indexOf("skos:prefLabel");
		
		if (indexPrefLabel < 0){
			return; // another label
		}
		else {
			
			StringBuilder left_container = new StringBuilder();
			String name = get_Entity_and_name(line, left_container);
			String entity = left_container.toString();
			
			if (name == null){
				//this is a problematic line like: Problematic line: <id_1koas7s_1sz_e1lhb7>	owl:Thing	skos:prefLabel	"entity"@eng
				return;
			}
			//entity finding
			Entity toBeNamed = null;
			
			toBeNamed = ParsedData.conflictMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			
			toBeNamed = ParsedData.locationsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			
			toBeNamed = ParsedData.langugagesMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			
			toBeNamed = ParsedData.leadersMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			
			toBeNamed = ParsedData.continentsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			
			toBeNamed = ParsedData.currenciesMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
			

			toBeNamed = ParsedData.constructionsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				
				return;
			}
		}
		
	}
	
	
	/* 
	 * A parsing helper function
	 * form <A> <labeType> "B" will return A in the return value and B in entity
	 * This function will return the name and return by reference the entity in the "entity" parameter
	 */
	private String get_Entity_and_name(String line,StringBuilder entity){
		
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		if (indexEntityStart < 0 || line.indexOf(">", indexEntityStart) < 0){
			//this is a problematic line like: Problematic line: <id_1koas7s_1sz_e1lhb7>	owl:Thing	skos:prefLabel	"entity"@eng
			return null;
		}
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}


}
