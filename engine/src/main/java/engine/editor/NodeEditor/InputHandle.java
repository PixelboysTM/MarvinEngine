package engine.editor.NodeEditor;

import engine.renderer.DebugDraw;
import engine.util.MyMath;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputHandle {
    private HandleType handleType = HandleType.INPUT;
    private transient Node node;


    private transient Connection connection;

    private float posX, posY, radius;
    private String name;
    private HandleDataType handleDataType;
    private int innerColor = ImGui.getColorU32(0.6f,0.6f,0.6f,1);

    public InputHandle(HandleDataType handleDataType, String name) {
        this.handleDataType = handleDataType;
        this.radius = 6;
        this.name = name;
    }
    public void setParent(Node n){
        this.node = n;
    }

    public void draw(float x, float y,ImDrawList list, ImVec2 cursorPos){
        boolean hover = MyMath.PointCircle(cursorPos.x, cursorPos.y, x,y, 7);
        this.posX = x;
        this.posY = y;
        Vector3f col = NodeEnumMethods.getColor(handleDataType);
        int c = ImGui.getColorU32(col.x, col.y,col.z,1);
        if (handleDataType == HandleDataType.FLOW){
            x += 1;
            ImVec2[] points = {
              new ImVec2(x - 6, y - 6),
              new ImVec2(x + 6, y - 6),
              new ImVec2(x + 10, y),
              new ImVec2(x + 6, y + 6),
              new ImVec2(x - 6, y + 6),
            };
            list.addConvexPolyFilled(points, 5,connection == null  ? innerColor : c);
            list.addPolyline(points, 5,c , true, hover ? 3 : 1.5f);
        }else {
            list.addCircleFilled(x,y, 7, connection == null  ? innerColor : c, 16);
            list.addCircle(x,y, 7, c, 16, hover ? 4 : 2 );
        }
        list.addText(x + 15, y - 12, ImGui.getColorU32(1,1,1,1), name);
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

    public void setConnection(Connection con){
        //assert connection != null : "Cant connect connected!";
        connection = con;
    }
    public boolean isConnected() {
        return connection != null;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputHandle that = (InputHandle) o;
        return getHandleType() == that.getHandleType() && getNode().getID() == that.getNode().getID() && Objects.equals(getName(), that.getName()) && getHandleDataType() == that.getHandleDataType();
    }

    public Connection getConnection() {
        return connection;
    }
}
