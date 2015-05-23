package db_parsers;

import db_entities.Entity;

public class LabelsParser extends FileParser{
	
	//skos:prefLabel
	

	public int prefLabelLen;

	public void init() {
		
		prefLabelLen = "skos:prefLabel".length();
		
	}

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
