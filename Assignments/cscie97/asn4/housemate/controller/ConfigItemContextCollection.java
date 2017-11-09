package cscie97.asn4.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

import java.util.*;


/**
 * Basically a collection of ConfigItemContex objects. But it implements the Context interface so that the “Serialize” method will return serialized data for every associated ConfigItemContext obejct. Creating a composite class like this may be overkill for the basic implementation because we might as well always use the entire HMMS context for each rule. However, if the HMMS context grew exceedingly larger, or if another subclass fetched its data from a remote service, we may not want to use the entire state as context since most of it wouldn’t be useful to a Rules’ Predicates or Commands.
 */
public class ConfigItemContextCollection implements Context, ConfigItemContextComponent {

	/**
	 * TA list of ConfigItemContextComponent objects. This is a composite, that allows for collections to be added to other collections since a ConfigItemContextCollection is a ConfigItemContextComponent
	 */
	private List<ConfigItemContextComponent> contexts;

	/**
	 * The name of the collection. Used to return the "ContextId()"
	 */
	private String _name;


	public ConfigItemContextCollection(String name){
		contexts = new ArrayList<ConfigItemContextComponent>();
		_name = name;
	}

	public String getContextId(){
		return _name;
	}


	/**
	 * Will call serialize() on all of items in ‘contexts’ and merge them together into a <Map<String,Map<String,String>>>
	 * @return a context map of type Map<String,Map<String,String>
	 */
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
				System.err.println("A ContextFetchException was encountered: " + e);
				throw new RuntimeException(e);
			} catch (QueryEngineException e) {
				System.err.println("A QueryEngineException was encountered: " + e);
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	public void addContext(ConfigItemContextComponent context){
		contexts.add(context);
	}




}
