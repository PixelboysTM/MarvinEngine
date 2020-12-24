package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.ConnectionBlueprint;

import java.lang.reflect.Type;

public class ConnectionDeserializer implements JsonDeserializer<ConnectionBlueprint> {
    @Override
    public ConnectionBlueprint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int OnodeID = json.getAsJsonObject().get("oh").getAsJsonObject().get("nodeId").getAsInt();
        int InodeID = json.getAsJsonObject().get("ih").getAsJsonObject().get("nodeId").getAsInt();

        int OhID = json.getAsJsonObject().get("oh").getAsJsonObject().get("hIndex").getAsInt();
        int IhID = json.getAsJsonObject().get("ih").getAsJsonObject().get("hIndex").getAsInt();
        ConnectionBlueprint b = new ConnectionBlueprint(OnodeID, OhID, InodeID,IhID);

        return b;
    }
}
