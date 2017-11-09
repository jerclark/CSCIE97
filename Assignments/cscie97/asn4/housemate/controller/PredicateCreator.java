package cscie97.asn4.housemate.controller;

import cscie97.asn1.knowledge.engine.QueryEngineException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

/**
 * Creates predicates for use with rules
 */
public class PredicateCreator {

    /**
     * Creates a predicate lambda function for reusable attachment to rules.
     *
     * @param jsString - The javascript string to evaluate as the predicate test. Must return a boolean.
     * @return
     */
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
                //I intentionally print the stack trace here because it's helpful to debug a JS
                e.printStackTrace();
            }
            System.out.println("CONTROLLER: Predicate '" + jsString + "' is " + result.toString());
            return (Boolean) result;
        };

        return p;
    }


}
