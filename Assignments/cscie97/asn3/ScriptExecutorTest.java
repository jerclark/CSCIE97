package cscie97.asn3;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ScriptExecutorTest {

    private class TestObj {
        public String a;
        public String b;
    }

    public static void main(String[] args) throws ScriptException {



        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
//        Map<String,Map<String, String>> subjectMap = new HashMap<String, Map<String, String>>();
//        Map<String, String> predicateMap = new HashMap<String, String>();
//        predicateMap.put("is_set_to", "65");
//        predicateMap.put("has_value", "terd");
//        subjectMap.put("a:b", predicateMap);
//        engine.put("var1", subjectMap);
        TestObj result = (TestObj)engine.eval("{a: '1', b: '2'}");
        System.out.println(result.a);

    }

}
