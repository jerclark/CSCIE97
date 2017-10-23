package cscie97.asn3.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.lang.reflect.Array;
import java.util.*;

public class ConfigItemContextCollection implements Context, ConfigItemContextComponent {

	private List<ConfigItemContextComponent> contexts;

	private String _name;

	public ConfigItemContextCollection(String name){
		contexts = new ArrayList<ConfigItemContextComponent>();
		name = name;
	}

	public String getContextId(){
		return _name;
	}

	public Map<String,Map<String,String>> serialize() {
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		Iterator i = contexts.iterator();
		while (i.hasNext()){
			ConfigItemContextComponent nextContext = (ConfigItemContextComponent)i.next();
			try {
				Map<String,Map<String,String>> context = (HashMap<String, Map<String, String>>)nextContext.serialize();
				Set<String> contextSubjects = context.keySet();
				Iterator subjects = contextSubjects.iterator();
				while (subjects.hasNext()){
					String subject = (String)subjects.next();
					result.putIfAbsent(subject, new HashMap<String, String>());
					result.get(subject).putAll(context.get(subject));
				}

			} catch (ContextFetchException e) {
				e.printStackTrace();
			} catch (QueryEngineException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void addContext(ConfigItemContextComponent context){
		contexts.add(context);
	}




}
