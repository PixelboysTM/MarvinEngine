package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.HandleDataType;

import java.lang.reflect.Type;

public class HandleDataTypeSerializer implements JsonSerializer<HandleDataType>, JsonDeserializer<HandleDataType> {
    @Override
    public HandleDataType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        System.out.println(json.getAsString());
        return HandleDataType.valueOf(json.getAsString());
    }

    @Override
    public JsonElement serialize(HandleDataType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }
}
