package engine.util.gson.nodeStuff;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import engine.editor.NodeEditor.Programm.Variable;

import java.lang.reflect.Type;

public class VariableDeserializer implements JsonDeserializer<Variable> {
    @Override
    public Variable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
return null;
    }
}
