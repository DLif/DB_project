package db_parsers;

import db_entities.Entity;
import db_entities.Maps_entitys;

public class Labels_parser implements File_parser{
	
	//skos:prefLabel
	
	static int noPrefNameNum =
			Maps_entitys.conflictMap.size()+Maps_entitys.locationsMap.size()+Maps_entitys.langugagesMap.size()+Maps_entitys.continentsMap.size()+Maps_entitys.currenciesMap.size()+Maps_entitys.leadersMap.size()+Maps_entitys.constructionsMap.size();
	
	static int prefLabelLen;

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
			String name = get_Entity_and_name(line,left_container);
			String entity = left_container.toString();
			
			if (name == null){
				//this is a problematic line like: Problematic line: <id_1koas7s_1sz_e1lhb7>	owl:Thing	skos:prefLabel	"entity"@eng
				return;
			}
			//entity finding
			Entity toBeNamed = null;
			
			toBeNamed = Maps_entitys.conflictMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			
			toBeNamed = Maps_entitys.locationsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			
			toBeNamed = Maps_entitys.langugagesMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			
			toBeNamed = Maps_entitys.leadersMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			
			toBeNamed = Maps_entitys.continentsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			
			toBeNamed = Maps_entitys.currenciesMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
			

			toBeNamed = Maps_entitys.constructionsMap.get(entity);
			if (toBeNamed != null){
				toBeNamed.setName(name);
				noPrefNameNum--;
				return;
			}
		}
		
	}
	
	private String get_Entity_and_name(String line,StringBuilder entity){
		
		int indexEntityStart = Maps_entitys.nth_occurence(2,line,"<");
		if (indexEntityStart < 0 || line.indexOf(">", indexEntityStart) < 0){
			//this is a problematic line like: Problematic line: <id_1koas7s_1sz_e1lhb7>	owl:Thing	skos:prefLabel	"entity"@eng
			return null;
		}
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, Maps_entitys.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}


}
