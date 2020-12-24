package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.InputHandle;
import engine.editor.NodeEditor.Node;
import engine.editor.NodeEditor.OutputHandle;
import engine.editor.NodeEditor.Programm.ActionCommand;
import org.joml.Vector3f;

import java.lang.reflect.Type;

public class NodeDeserializer implements JsonDeserializer<Node>, JsonSerializer<Node> {
    @Override
    public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject element = json.getAsJsonObject();
        float x = element.get("posX").getAsFloat();
        float y = element.get("posY").getAsFloat();
        float w = element.get("sizeX").getAsFloat();
        float h = element.get("sizeY").getAsFloat();
        JsonArray volor = element.getAsJsonArray("color");
        Vector3f color = new Vector3f(volor.get(0).getAsFloat(), volor.get(1).getAsFloat(), volor.get(2).getAsFloat());
        String title = element.get("title").getAsString();
        InputHandle[] inputHandles = context.deserialize(element.get("iHandles"), InputHandle[].class);
        OutputHandle[] outputHandles = context.deserialize(element.get("oHandles"), OutputHandle[].class);
        ActionCommand cmd = context.deserialize(element.get("cmd"), ActionCommand.class);
        int id = element.get("id").getAsInt();
        Node n = new Node(x,y,w,h,color, title, inputHandles, outputHandles, cmd);
        n.setID(id);
        return n;
    }

    @Override
    public JsonElement serialize(Node src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject element = new JsonObject();
        element.add("posX", new JsonPrimitive(src.getPosX()));
        element.add("posY", new JsonPrimitive(src.getPosY()));
        element.add("sizeX", new JsonPrimitive(src.getSizeX()));
        element.add("sizeY", new JsonPrimitive(src.getSizeY()));
        JsonArray volor = new JsonArray();
        volor.add(src.getColor().x);
        volor.add(src.getColor().y);
        volor.add(src.getColor().z);
        element.add("color", volor);
        element.add("title", new JsonPrimitive(src.getTitle()));
        element.add("iHandles", context.serialize(src.getInputHandles()));
        element.add("oHandles", context.serialize(src.getOutputHandles()));
        element.add("cmd", context.serialize(src.getCommand()));
        element.add("id", new JsonPrimitive(src.getID()));
        return element;
    }

}
