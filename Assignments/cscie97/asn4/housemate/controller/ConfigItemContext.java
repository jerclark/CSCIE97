package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.model.HouseMateModelService;
import cscie97.asn4.housemate.model.HouseMateModelServiceImpl;

import java.util.*;


public class ConfigItemContext implements Context, ConfigItemContextComponent {

	private ConfigItemType configItemType;
	private String _fqn;

	public ConfigItemContext(ConfigItemType type, String fqn){
		configItemType = type;
		_fqn = fqn;
	}

	public String getContextId(){
		return _fqn;
	}

	public Map<String,Map<String,String>> serialize() throws ContextFetchException {
		HouseMateModelService hmms = HouseMateModelServiceImpl.getInstance();
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		List<String> context = new ArrayList<String>();
		try {
			switch (configItemType) {
				case CONFIG_ITEM_TYPE_HOUSE:
					context = hmms.getHouse("1", _fqn);
					break;
				case CONFIG_ITEM_TYPE_ROOM:
					context = hmms.getRoom("1", _fqn);
					break;
				case CONFIG_ITEM_TYPE_DEVICE:
					context = hmms.getDevice("1", _fqn);
					break;
				case CONFIG_ITEM_TYPE_FEATURE:
					context = hmms.getFeature("1", _fqn);
					break;
				case CONFIG_ITEM_TYPE_OCCUPANT:
					context = hmms.getOccupant("1", _fqn);
					break;
				case CONFIG_ITEM_ALL:
					context = hmms.getAll("1");
			}
			Iterator contextIterator = context.iterator();
			while(contextIterator.hasNext()){
				String nextTriple = (String)contextIterator.next();
				String[] tripleParts = nextTriple.split(" ");
				String subject = tripleParts[0];
				String predicate = tripleParts[1];
				String object = tripleParts[2];
				result.putIfAbsent(subject, new HashMap<String, String>());
				result.get(subject).put(predicate, object);
			}
			return result;
		}catch(Exception e){
			throw new ContextFetchException(e);
		}
	}



}
