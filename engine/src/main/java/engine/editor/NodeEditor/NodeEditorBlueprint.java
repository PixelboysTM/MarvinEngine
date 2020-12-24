package engine.editor.NodeEditor;

import org.joml.Vector2f;

import java.util.List;

public class NodeEditorBlueprint {
    public int MAX_ID;
    public Vector2f move;
    public String name;
    public int objID;

    public List<Node> nodes;
    public List<Connection> connections;

    public NodeEditorBlueprint(int MAX_ID, Vector2f move, String name, List<Node> nodes, List<Connection> connections, int objID) {
        this.MAX_ID = MAX_ID;
        this.move = move;
        this.name = name;
        this.nodes = nodes;
        this.connections = connections;
        this.objID = objID;
    }
}
