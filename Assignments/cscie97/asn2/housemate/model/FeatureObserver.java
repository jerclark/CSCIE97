package cscie97.asn2.housemate.model;

import java.util.*;

import cscie97.asn3.housemate.controller.Rule;

public class FeatureObserver implements Observer {

    private String _featureFqn;
    private Map<String, Rule> _rules;

    public FeatureObserver(String featureFqn){
        _featureFqn = featureFqn;
        _rules = new HashMap<String, Rule>();
    }

    @Override
    public void update(Observable o, Object arg) {
        _rules.values().forEach( (rule) -> rule.match(o) );
    }

    public void addRule(Rule r){
        _rules.put(r.getName(), r);
    }

    public void removeRule(Rule r){
        _rules.remove(r.getName());
    }

    public Map<String, Rule> getRules(){
        return _rules;
    }

    public String getFeatureFqn(){
        return _featureFqn;
    }


}
