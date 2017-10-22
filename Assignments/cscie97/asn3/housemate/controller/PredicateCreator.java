package cscie97.asn3.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

public class PredicateCreator {

    public static Predicate<Context> createJSPredicate(String jsString){

        Predicate<Context> p = (context) -> {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            try {
                engine.put("context", context.serialize());
            } catch (QueryEngineException e) {
                e.printStackTrace();
            } catch (ContextFetchException e) {
                e.printStackTrace();
            }
            Object result = null;
            try {
                result = engine.eval(jsString);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            return (Boolean) result;
        };

        return p;
    }

    public static Predicate<Boolean> createBooleanPredicate(){

        Predicate<Boolean> p = (boolVal) -> { return boolVal; };
        return p;

    }


}
