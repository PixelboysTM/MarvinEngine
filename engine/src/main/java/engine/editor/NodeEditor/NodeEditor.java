package engine.editor.NodeEditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import engine.Window;
import engine.ecs.GameObject;
import engine.editor.NodeEditor.Programm.VirtualMachine;
import engine.input.KeyListener;
import engine.input.MouseListener;
import engine.util.Settings;
import engine.util.Tuple;
import engine.util.gson.nodeStuff.*;
import engine.util.gson.nodeStuff.runtime.NodeEditorRuntimeSerializer;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class NodeEditor {
    private int MAX_ID;
    private String name;
    private ArrayList<Node> nodes;
    private List<Connection> connections;
    private Vector2f move;
    private int objID = -1;
    private transient ImVec2 lastDragPos = new ImVec2();
    private transient OutputHandle draggingFrom = null;
    private transient boolean inserterOpen = false;
    private transient ImVec2 inserterPos = new ImVec2();
    private transient List<Node> selectedNodes;
    private transient boolean isFocused;
    private transient Node hoveredNode = null;
    private transient Connection hoveredConnection = null;
    private transient GameObject self;

    public NodeEditor(GameObject obj) {
        this.self = obj;
        this.name = obj.getName();
        this.objID = obj.getUid();
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
        selectedNodes = new ArrayList<>();
//        nodes.add(new Node(110, 110, 200, 250, new Vector3f(1, 0.5f, 0.25f), "Test Node",
//                new InputHandle[]{
//                        new InputHandle(HandleDataType.FLOW, "In"),
//                        new InputHandle(HandleDataType.INT, "Number"),
//                },
//                new OutputHandle[]{
//                        new OutputHandle(HandleDataType.FLOW, "Out"),
//                        new OutputHandle(HandleDataType.INT, "Number"),
//                }
//        ));
//        nodes.add(new Node(110, 110, 200, 250, new Vector3f(1, 0.5f, 0.25f), "Test Node 2",
//                new InputHandle[]{
//                        new InputHandle(HandleDataType.FLOW, "In"),
//                        new InputHandle(HandleDataType.INT, "Number"),
//                },
//                new OutputHandle[]{
//                        new OutputHandle(HandleDataType.FLOW, "Out"),
//                        new OutputHandle(HandleDataType.INT, "Number"),
//                }
//        ));
        move = new Vector2f(0);

    }

    public void draw() {
    }

    public void imgui() {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 1, 1);

        ImGui.begin("Script: " + name + "###NODE_EDITOR_WITH_ID_" + objID , ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

        isFocused = ImGui.isWindowFocused();

        if (ImGui.beginMenuBar()){
            ImGui.separator();
            if(ImGui.menuItem("Compile")){
                compileToDiskAndProvideProgramm();
            }
            ImGui.separator();
            ImGui.endMenuBar();

        }


        ImVec2 offset = new ImVec2();
        ImGui.getWindowPos(offset);

        ImDrawList list = ImGui.getWindowDrawList();
        ImVec2 min = new ImVec2();
        ImGui.getWindowContentRegionMin(min);
        ImVec2 max = new ImVec2();
        ImGui.getWindowContentRegionMax(max);

        float spacing = 50;
        //Background
        ImGui.invisibleButton("nodeedit" + name, max.x, max.y);
        boolean focus = ImGui.isItemFocused();
        boolean hover = ImGui.isItemHovered();

        list.addRectFilled(min.x + offset.x, min.y + offset.y, offset.x + max.x, offset.y + max.y, ImGui.getColorU32(0.2f, 0.2f, 0.2f, 1));
        //Grid
        for (int i = 0; i < max.x / spacing; i++) {
            list.addLine(
                    offset.x + min.x + i * spacing + getRawOffset(move.x, spacing),
                    offset.y + min.y,
                    offset.x + min.x + i * spacing + getRawOffset(move.x, spacing),
                    offset.y + max.y,
                    ImGui.getColorU32(0, 0, 0, 1),
                    1.5f);
        }
        for (int i = 0; i < max.y / spacing; i++) {
            list.addLine(
                    offset.x + min.x,
                    offset.y + min.y + i * spacing + +getRawOffset(move.y, spacing),
                    offset.x + max.x,
                    offset.y + min.y + i * spacing + +getRawOffset(move.y, spacing),
                    ImGui.getColorU32(0, 0, 0, 1),
                    1.5f);
        }
        spacing /= 2;
        for (int i = 0; i < max.x / spacing; i++) {
            list.addLine(
                    offset.x + min.x + i * spacing + getRawOffset(move.x, spacing),
                    offset.y + min.y,
                    offset.x + min.x + i * spacing + getRawOffset(move.x, spacing),
                    offset.y + max.y,
                    ImGui.getColorU32(0, 0, 0, 1),
                    1f);
        }
        for (int i = 0; i < max.y / spacing; i++) {
            list.addLine(
                    offset.x + min.x,
                    offset.y + min.y + i * spacing + +getRawOffset(move.y, spacing),
                    offset.x + max.x,
                    offset.y + min.y + i * spacing + +getRawOffset(move.y, spacing),
                    ImGui.getColorU32(0, 0, 0, 1),
                    1f);
        }
        ImVec2 mPos = new ImVec2();
        ImGui.getMousePos(mPos);
        //Draw nodes
        hoveredNode = null;
        hoveredConnection = null;
        for (Connection c : connections) {
            if(c.daw(list, mPos))
                hoveredConnection = c;
        }
        for (Node n :
                nodes) {
            if (n.draw(list, offset, move, selectedNodes.contains(n))) {
                hoveredNode = n;
            }
        }



        //Debug
        list.addText(offset.x + min.x + 5, offset.y + min.y + 10, ImGui.getColorU32(1, 1, 1, 1), ImGui.getMousePosX() + " | " + ImGui.getCursorPosY());

        //Connection making
        if (draggingFrom != null) {
            Vector3f s = NodeEnumMethods.getColor(draggingFrom.getHandleDataType());
            list.addLine(draggingFrom.getPosX(), draggingFrom.getPosY(), mPos.x, mPos.y,
                    ImGui.getColorU32(s.x, s.y, s.z, 1), 5);

            if (!ImGui.isMouseDown(0)) {
                if (hoveredNode != null) {
                    InputHandle hh = hoveredNode.getHoveredInput(mPos);
                    if (hh != null && hh.getNode() != draggingFrom.getNode() && hh.getHandleDataType() == draggingFrom.getHandleDataType() && !hh.isConnected() ) {
                        connections.add(new Connection(hh, draggingFrom));
                    }
                }
                draggingFrom = null;
            }

        } else {
            if (inserterOpen) {
                drawInserter();
            }


            //Input handling
            else if (ImGui.isWindowFocused() && hover) {

                if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                    inserterOpen = true;
                    ImGui.getMousePos(inserterPos);

                }

                else if (ImGui.isMouseClicked(0)) {
                    if (hoveredNode != null){
                    OutputHandle handle = hoveredNode.getHoveredOutput(mPos);
                    if (handle != null) {
                        draggingFrom = handle;
                    }else{
                        selectedNodes.clear();
                        selectedNodes.add(hoveredNode);
                    }
                    }else {
                        selectedNodes.clear();
                    }
                } else if (ImGui.isMouseDown(0)) {
                    ImVec2 mousePos = new ImVec2();
                    ImGui.getMousePos(mousePos);
                    float deltaX = mousePos.x - lastDragPos.x;
                    float deltaY = mousePos.y - lastDragPos.y;
                    if (selectedNodes.size() == 0) {
                        move.add(deltaX, deltaY);
                    } else {

                        selectedNodes.forEach(node -> node.move(deltaX, deltaY));
                    }
                }
                ImGui.getMousePos(lastDragPos);
            }

        }
        if (isFocused){
            if(KeyListener.isKeyPressed(GLFW_KEY_DELETE) && hoveredConnection != null){
                hoveredConnection.unbind();
                connections.remove(hoveredConnection);
                hoveredConnection = null;
            }else if (KeyListener.isKeyPressed(GLFW_KEY_DELETE) && selectedNodes.size() > 0) {
                for (Node n :
                        selectedNodes) {
                    for (int ic = 0; ic < connections.size(); ic++) {
                        Connection c = connections.get(ic);
                        if (c.getInputHandle().getNode().getID() == n.getID() || c.getOutputHandle().getNode().getID() == n.getID()){
                            c.unbind();
                            connections.remove(c);
                            ic--;
                        }
                    }
                    nodes.remove(n);
                }
                selectedNodes.clear();
            }
        }
        ImGui.end();
        ImGui.popStyleVar();

    }

    private void compileToDiskAndProvideProgramm() {
        System.out.println(NodeEditorRuntimeSerializer.getRuntimeDataForObjectNodes(this));
    }



    private void loadEditorFromData(String data){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Node.class, new NodeDeserializer())
                .registerTypeAdapter(Connection.class, new ConnectionSerializer())
                .registerTypeAdapter(ConnectionBlueprint.class, new ConnectionDeserializer())
                .registerTypeAdapter(NodeEditorBlueprint.class, new NodeEditorDeserializer())
                .registerTypeAdapter(InputHandle.class, new InputHandleDeserializer())
                .registerTypeAdapter(OutputHandle.class, new OutputHandleDeserializer())
                .registerTypeAdapter(HandleDataType.class, new HandleDataTypeSerializer())
                .create();



        NodeEditorBlueprint blueprint = gson.fromJson(data, NodeEditorBlueprint.class);
        MAX_ID = blueprint.MAX_ID;
        move.set(blueprint.move);
        name = blueprint.name;
        objID = blueprint.objID;
        nodes = new ArrayList<>(blueprint.nodes);
        connections = new ArrayList<>(blueprint.connections);
    }

    public String getSaveData() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Node.class, new NodeDeserializer())
                .registerTypeAdapter(Connection.class, new ConnectionSerializer())
                .registerTypeAdapter(ConnectionBlueprint.class, new ConnectionDeserializer())
                .registerTypeAdapter(HandleDataType.class, new HandleDataTypeSerializer())
                .create();

        return gson.toJson(this);

    }


    transient String[] inserterItems = new String[]{
            "Var/Int/Int Variable get",
            "Var/Int/Int Variable set",
            "Var/Int/Int constant",
            "event/Start Node",
            "event/Tick Node",
            "event/Key Pressed",
            "move/Set x Position",
            "move/Set y Position",
            "move/Set Position",
            "move/Move x Position",
            "move/Move y Position",
            "move/Move Position",
            "math/Int/Int Add",
            "math/Int/Int Subtract",
            "math/Int/Int Multiply",
            "math/Int/Int Divide",
            "Utils/Print",
            "Utils/Convert/Int to String",
    };
    transient String folders = "";
    transient ImString searchString = new ImString();

    private void drawInserter() {
        selectedNodes.clear();

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 5);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 2);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 5, 5);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowTitleAlign, 0.5f, 0.5f);

        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.1f,0.1f,0.1f,1);
        ImGui.pushStyleColor(ImGuiCol.Border, 1, 1, 1, 1);

        ImGui.setNextWindowPos(inserterPos.x - 100, inserterPos.y);
        ImGui.setNextWindowSize(200, 500);
        ImGui.begin("Inserter", ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse);
    if (!ImGui.isWindowFocused())
        inserterOpen = false;

        ImGui.text("Search");
        ImGui.sameLine();
        ImGui.inputText("", searchString);
        ImGui.separator();
        if (!folders.equals("")) {
            if (ImGui.menuItem(folders)) {
                folders = folders.substring(folders.length() - 1);
                int i = folders.lastIndexOf("/");
                folders = folders.substring(i + 1);
            }
            ImGui.separator();
        }

        List<Tuple<String, Boolean>> items = new ArrayList<>();
        for (String s :
                inserterItems) {
            if (!s.startsWith(folders))
                continue;

            String comp = s.substring(folders.length());

            String[] ss = comp.split("/");
            Tuple t = new Tuple<>(ss[0], ss.length > 1);
            boolean contains = false;
            for (Tuple<String, Boolean> cs :
                    items) {
                if (cs.equals(t))
                    contains = true;
            }
            if (!contains)
                items.add(t);
        }


        ImGui.setNextItemWidth(150);
        for (int i = 0; i < items.size(); i++) { //TODO: Fix Folder signals
            if (ImGui.menuItem(items.get(i).x)) {
                if (items.get(i).y) {
                    folders += items.get(i).x + "/";
                    continue;
                }

                ImVec2 pos = new ImVec2();
                ImGui.getWindowPos(pos);
                Node n = NodeHolder.getNode(items.get(i).x.trim());
                n.setPos(pos.x,pos.y);
                n.setID(MAX_ID);
                MAX_ID++;
                nodes.add(n);
                inserterOpen = false;
            }

            if (items.get(i).y) {
                ImGui.sameLine();
                ImDrawList list = ImGui.getWindowDrawList();
                float x = ImGui.getWindowPosX();
                float y = ImGui.getCursorScreenPosY();
                list.addText(x + 180, y, ImGui.getColorU32(1,1,1,1), ">");
                ImGui.newLine();
//                ImGui.text(">");
            }
            ImGui.separator();
        }

        ImGui.end();

        ImGui.popStyleVar(4);
        ImGui.popStyleColor(2);
    }

    public float getRawOffset(float val, float clamp) {
        if (val > 0) {
            while (val >= clamp) {
                val -= clamp;
            }
            return val;
        }
        while (val <= clamp) {
            val += clamp;
        }
        return val;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public String getName() {
        return name;
    }

    public void setData(String toString) {
        loadEditorFromData(toString);
    }

    public void inspector(){

        if (selectedNodes.size() == 1){
            Node n = selectedNodes.get(0);
            n.drawInspector();
        }

        ImGui.separator();
        if (hoveredNode != null){
            ImGui.labelText("Name", hoveredNode.getTitle());
            ImGui.labelText("ID", String.valueOf(hoveredNode.getID()));
        }
    }

    public boolean isFocused() {
        return isFocused;
    }

    public int getObjID() {
        return objID;
    }

    public void setName(GameObject objwithID) {
        name = objwithID.getName();
    }
}
