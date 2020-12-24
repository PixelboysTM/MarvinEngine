package engine.editor.NodeEditor.Programm;

import com.google.gson.JsonPrimitive;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RuntimeVarHandler {
    private Map<String, Integer> intVars;

    public RuntimeVarHandler(){
        intVars = new HashMap<>();
    }

    public <T> T getValueOrDefault(String key, T defaultValue, VarType type){
        switch (type){
            case INT:
                if (intVars.containsKey(key)){
                    return (T) intVars.get(key);
                }
                intVars.put(key, (Integer) defaultValue);
                return defaultValue;
        }
        return null;
    }

    public <T> void setValue(String varName, T value, VarType anInt) {
        switch (anInt){
            case INT:
                intVars.put(varName, (Integer) value);
                break;
            default:
                System.out.println("\\u001B[31mVM_ERROR: [VarHandler] Unknown type for variable " + varName + "\\u001B[0m");
        }
    }
}
