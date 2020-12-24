package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.HandleDataType;
import engine.editor.NodeEditor.InputHandle;

import java.lang.reflect.Type;

public class InputHandleDeserializer implements JsonDeserializer<InputHandle> {
    @Override
    public InputHandle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        HandleDataType hType = context.deserialize(object.get("handleDataType"), HandleDataType.class); //TODO: Serialize enum as int
        String name = object.get("name").getAsString();

        return new InputHandle(hType, name);
    }
}
