package engine.util.gson.nodeStuff;

import com.google.gson.*;
import engine.editor.NodeEditor.Connection;
import engine.editor.NodeEditor.ConnectionBlueprint;

import java.lang.reflect.Type;

public class ConnectionSerializer implements JsonSerializer<Connection> {


    @Override
    public JsonElement serialize(Connection src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        JsonObject oh = new JsonObject();
        oh.add("nodeId", new JsonPrimitive( src.getOutputHandle().getNode().getID()));
        oh.add("hIndex", new JsonPrimitive( find( src.getOutputHandle().getNode().getOutputHandles(), src.getOutputHandle())));
        object.add("oh", oh);

        JsonObject ih = new JsonObject();
        ih.add("nodeId", new JsonPrimitive( src.getInputHandle().getNode().getID()));
        ih.add("hIndex", new JsonPrimitive( find( src.getInputHandle().getNode().getInputHandles(), src.getInputHandle())));
        object.add("ih", ih);
        return object;
    }

    public static<T> int find(T[] a, T target)
    {
        for (int i = 0; i < a.length; i++)
            if (target.equals(a[i]))
                return i;

        return -1;
    }
}
