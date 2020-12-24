package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.*;
import org.joml.Vector2f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeEditorDeserializer implements JsonDeserializer<NodeEditorBlueprint> {
    @Override
    public NodeEditorBlueprint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject element = json.getAsJsonObject();
        int max = element.get("MAX_ID").getAsInt();
        String name = element.get("name").getAsString();
        Node[] nodes = context.deserialize(element.get("nodes"), Node[].class);
        ConnectionBlueprint[] connectionBlueprints = context.deserialize(element.get("connections"), ConnectionBlueprint[].class);
        Connection[] cons = genConns(connectionBlueprints, nodes);
        JsonObject move = element.getAsJsonObject("move");
        Vector2f m = new Vector2f(move.get("x").getAsFloat(), move.get("y").getAsFloat());
        int objID = element.get("objID").getAsInt();

        return new NodeEditorBlueprint(max, m, name, Arrays.asList(nodes), Arrays.asList(cons), objID);
    }

    private Connection[] genConns(ConnectionBlueprint[] connectionBlueprints, Node[] nodes) {
        Connection[] cons = new Connection[connectionBlueprints.length];
        for (int i = 0; i < cons.length; i++) {
            ConnectionBlueprint bp = connectionBlueprints[i];
            Node in = nodes[bp.InodeID];
            InputHandle ih = in.getInputHandles()[bp.IhID];
            Node on = nodes[bp.OnodeId];
            OutputHandle oh = on.getOutputHandles()[bp.OhID];
            cons[i] = new Connection(ih,oh);
        }
        return cons;
    }
}
