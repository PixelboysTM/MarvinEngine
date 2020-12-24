package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.HandleDataType;
import engine.editor.NodeEditor.HandleType;
import engine.editor.NodeEditor.InputHandle;
import engine.editor.NodeEditor.OutputHandle;

import java.lang.reflect.Type;

public class OutputHandleDeserializer implements JsonDeserializer<OutputHandle> {
    @Override
    public OutputHandle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        HandleDataType hType = context.deserialize(object.get("handleDataType"), HandleDataType.class);
        String name = object.get("name").getAsString();

        return new OutputHandle(hType, name);
    }
}
