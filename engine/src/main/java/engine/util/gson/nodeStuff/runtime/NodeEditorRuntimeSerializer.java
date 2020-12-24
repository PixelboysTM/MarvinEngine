package engine.util.gson.nodeStuff.runtime;

import com.google.gson.*;
import engine.editor.NodeEditor.*;
import engine.editor.NodeEditor.Programm.Action;
import engine.editor.NodeEditor.Programm.Variable;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NodeEditorRuntimeSerializer implements JsonSerializer<NodeEditor> {

    public static JsonArray getRuntimeDataForObjectNodes(NodeEditor editor){
        JsonArray nodes = new JsonArray();
        List<Node> rawNodesList = editor.getNodes();
        rawNodesList.sort(new NodeIDComparator());

        for (int i = 0; i < rawNodesList.size(); i++) {
            Node n = rawNodesList.get(i);
            JsonObject ns = new JsonObject();

            //General data
            ns.add("debug_key_id", new JsonPrimitive(n.getID()));
            ns.add("cmd", new JsonPrimitive(n.getCommand().getAction().name()));
            //## Vars
            JsonArray vars = new JsonArray();
            List<Variable> vs = n.getCommand().getVariables();
            for (int j = 0; j < vs.size(); j++) {
                JsonObject var = new JsonObject();
                Variable v = vs.get(j);

                var.add("name", new JsonPrimitive(v.getName()));
                var.add("default", new JsonPrimitive(v.getDefaultValueAsString()));
                var.add("type", new JsonPrimitive(v.getVarType().name()));
                vars.add(var);
            }

            ns.add("vars", vars);

            //Flow
            OutputHandle[] flowHandles = n.getOutputHandlesOfType(HandleDataType.FLOW);
            if (flowHandles.length > 0){
                JsonArray flows = new JsonArray();
                for (int j = 0; j < flowHandles.length; j++) {
                    if (!flowHandles[j].isConnected())
                        continue;
                    JsonObject f = new JsonObject();
                    f.add("key", new JsonPrimitive(flowHandles[j].getName()));
                    f.add("value", getFlowIndexes(flowHandles[j]));
                    flows.add(f);
                }
                ns.add("flows", flows);
            }else{
                ns.add("flows", new JsonArray());
            }


            //Data pins
            InputHandle[] dataHandles = n.getDataHandles();
            if (dataHandles.length > 0){
                JsonArray data = new JsonArray();
                for (int j = 0; j < dataHandles.length; j++) {
                    if (!dataHandles[j].isConnected())
                        continue;
                    JsonObject d = new JsonObject();
                    d.add("key", new JsonPrimitive(dataHandles[j].getName()));
                    d.add("value", getDataIndex(dataHandles[j]));
                    d.add("identifier", new JsonPrimitive(dataHandles[j].getConnection().getOutputHandle().getName()));
                    data.add(d);
                }
                ns.add("data", data);
            }else{
                ns.add("data", new JsonArray());
            }
            nodes.add(ns);
        }
        return nodes;
    }

    private static JsonElement getDataIndex(InputHandle dataHandle) {
        return new JsonPrimitive( dataHandle.getConnection().getOutputHandle().getNode().getID());
    }

    @Override
    public JsonElement serialize(NodeEditor src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        JsonArray nodes = new JsonArray();
        List<Node> rawNodesList = src.getNodes();
        Collections.sort(rawNodesList, new NodeIDComparator());
        List<Node> rawNodes =  rawNodesList;

        for (int i = 0; i < rawNodes.size(); i++) {
            Node n = rawNodes.get(i);
            JsonObject ns = new JsonObject();
            ns.add("debug_key_id", new JsonPrimitive(n.getID()));
            ns.add("cmd", new JsonPrimitive(n.getCommand().getAction().name()));
            OutputHandle[] flowHandles = n.getOutputHandlesOfType(HandleDataType.FLOW);
            if (flowHandles.length > 0){
                JsonArray flows = new JsonArray();
                for (int j = 0; j < flowHandles.length; j++) {
                    if (!flowHandles[j].isConnected())
                        continue;
                   JsonObject f = new JsonObject();
                   f.add("key", new JsonPrimitive(flowHandles[j].getName()));
                   f.add("value", getFlowIndexes(flowHandles[j]));
                   flows.add(f);
                }
                ns.add("flows", flows);
            }
            nodes.add(ns);
        }
        obj.add("data", nodes);

        return obj;
    }

    private static JsonElement getFlowIndexes(OutputHandle flowHandle) {
        JsonArray array = new JsonArray();
        for (Connection c : flowHandle.getConnections()){
            array.add(c.getInputHandle().getNode().getID());
        }
        return array;
    }

    private Node findStart(List<Node> nodes) {
        for (Node n :
                nodes) {
            if (n.getCommand().getAction().equals(Action.START)){
                return n;
            }
        }
        return null;
    }

    public static class NodeIDComparator implements Comparator<Node>{

        @Override
        public int compare(Node o1, Node o2) {
            return Integer.compare(o1.getID(), o2.getID());
        }
    }
}
