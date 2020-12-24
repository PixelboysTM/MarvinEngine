package engine.editor.NodeEditor;

import engine.util.MyMath;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OutputHandle {
    private HandleType handleType = HandleType.OUTPUT;

    private transient List<Connection> connections;
    private transient float posX, posY, radius;
    private String name;
    private HandleDataType handleDataType;
    private transient int innerColor = ImGui.getColorU32(0.6f,0.6f,0.6f,1);
    private transient Node node;

    public OutputHandle(HandleDataType handleDataType, String name) {
        this.handleDataType = handleDataType;
        this.name = name;
        this.radius = 6;
        this.connections = new ArrayList<>();
    }

    public void draw(float x, float y, ImDrawList list, ImVec2 cursorPos){
        boolean hover = MyMath.PointCircle(cursorPos.x, cursorPos.y, x,y, 7);
        this.posX = x;
        this.posY = y;
        Vector3f col = NodeEnumMethods.getColor(handleDataType);
        int c = ImGui.getColorU32(col.x, col.y,col.z,1);
        if (handleDataType == HandleDataType.FLOW){
            x -= 1;
            ImVec2[] points = {
                    new ImVec2(x - 6, y - 6),
                    new ImVec2(x + 6, y - 6),
                    new ImVec2(x + 10, y),
                    new ImVec2(x + 6, y + 6),
                    new ImVec2(x - 6, y + 6),
            };
            list.addConvexPolyFilled(points, 5, connections.size() == 0 ? innerColor : c);
            list.addPolyline(points, 5, c,
                    true, hover ? 3 : 1.5f);
        }else {
            list.addCircleFilled(x,y, 7, connections.size() == 0 ?  innerColor : c, 16);
            list.addCircle(x,y, 7, c, 16, hover ? 4 : 2f);
        }
        ImVec2 size = new ImVec2();
        ImGui.calcTextSize(size, name);
        list.addText(x - size.x - 12 , y - ImGui.getTextLineHeight()/2, ImGui.getColorU32(1,1,1,1), name);
    }

    public boolean doHover(ImVec2 cursorPos) {
        return MyMath.PointCircle(cursorPos.x, cursorPos.y, posX, posY, 7);
    }

    public HandleType getHandleType() {
        return handleType;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public String getName() {
        return name;
    }

    public HandleDataType getHandleDataType() {
        return handleDataType;
    }

    public void addConnection(Connection con){
        if(!connections.contains(con))
            connections.add(con);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setParent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public boolean isConnected() {
        return connections.size() > 0;
    }
}
