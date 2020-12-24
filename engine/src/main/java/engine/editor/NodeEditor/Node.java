package engine.editor.NodeEditor;
import engine.editor.NodeEditor.Programm.Action;
import engine.editor.NodeEditor.Programm.ActionCommand;
import engine.util.MyMath;
import imgui.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private float posX, posY, sizeX, sizeY;
    private Vector3f color;
    private String title;
    private InputHandle[] inputHandles;
    private OutputHandle[] outputHandles;
    private ActionCommand command;
    private int ID = -1;

    private ImFont titleFont;

    public Node(float posX, float posY, float sizeX, float sizeY, Vector3f color, String title, InputHandle[] inputHandles, OutputHandle[] outputHandles, ActionCommand command ) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color = color;
        this.title = title;
        this.inputHandles = inputHandles;
        this.outputHandles = outputHandles;
        this.command = command;
        init();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private void init() {
        //titleFont = ImGui.getIO().getFonts().addFontFromFileTTF("assets/fonts/ComicNeue-Bold.ttf", 20);
        for (InputHandle h :
                inputHandles) {
            h.setParent(this);
        }
        for (OutputHandle o :
                outputHandles) {
            o.setParent(this);
        }
    }

    public boolean draw(ImDrawList list, ImVec2 offset, Vector2f move, boolean selected) {
        float pMinX = offset.x + posX + move.x;
        float pMinY = offset.y + posY + move.y;
        float pMaxX = offset.x + posX + sizeX + move.x;
        float pMaxY = offset.y + posY + sizeY + move.y;

        boolean hoveringNode = MyMath.PointRectCol(
                new Vector2f(ImGui.getMousePosX(), ImGui.getMousePosY()),
                new Vector2f(pMinX, pMinY), new Vector2f(sizeX, sizeY));
        boolean hoverHandle = hoverHandle();
        boolean hovering = hoveringNode && !hoverHandle;



        list.addRectFilled(
                pMinX,
                pMinY,
                pMaxX,
                pMaxY,
                ImGui.getColorU32(0.2f, 0.2f, 0.2f, 0.8f),
                10);
        list.addRect(
                pMinX,
                pMinY,
                pMaxX,
                pMaxY,
                ImGui.getColorU32(color.x, color.y, color.z, 1),
                10, 0xF, hovering || selected ? 3 : 1.5f);
        list.addLine(
                pMinX,
                pMinY + 30,
                pMaxX,
                pMinY + 30,
                ImGui.getColorU32(color.x, color.y, color.z, 1),
                1.5f);
        //ImFont font = ImGui.getIO().getFonts().addFontFromFileTTF("assets/fonts/ComicNeue-Regular.ttf", 12);

        list.addText(pMinX + 5, pMinY + 5, ImGui.getColorU32(1, 1, 1, 1), title);


        ImVec2 cPos = new ImVec2();
        ImGui.getMousePos(cPos);

        float startY = pMinY + 50;
        for (int i = 0; i < inputHandles.length; i++) {
            inputHandles[i].draw(pMinX, startY, list, cPos);
            startY += 25;

        }
        startY = pMinY + 50;
        for (int i = 0; i < outputHandles.length; i++) {
            outputHandles[i].draw(pMaxX, startY, list, cPos);
            startY += 25;
        }

        return hovering || hoverHandle;
    }

    private boolean hoverHandle() {
        ImVec2 mPos = new ImVec2();
        ImGui.getMousePos(mPos);

        boolean hover = false;
        for (InputHandle ih :
                inputHandles) {
            if(ih.doHover(mPos))
                hover = true;
        }
        for (OutputHandle oh :
                outputHandles) {
            if(oh.doHover(mPos))
                hover = true;
        }
        return hover;
    }

    public void move(float deltaX, float deltaY) {
        posX += deltaX;
        posY += deltaY;
    }

    public OutputHandle getHoveredOutput(ImVec2 mPos) {
        for (OutputHandle oh :
                outputHandles) {
            if(oh.doHover(mPos))
               return oh;
        }
        return null;
    }

    public InputHandle getHoveredInput(ImVec2 mPos) {
        for (InputHandle ih :
                inputHandles) {
            if(ih.doHover(mPos))
                return ih;
        }
        return null;
    }

    public ActionCommand getCommand() {
        return command;
    }

    public void setPos(float x, float y) {
        posX = x;
        posY = y;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public Vector3f getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public InputHandle[] getInputHandles() {
        return inputHandles;
    }

    public OutputHandle[] getOutputHandles() {
        return outputHandles;
    }

    public OutputHandle[] getOutputHandlesOfType(HandleDataType type) {
        List<OutputHandle> hs = new ArrayList<>();
        for (OutputHandle h :
                outputHandles) {
            if (h.getHandleDataType().equals(type))
                hs.add(h);
        }
        OutputHandle[] out = new OutputHandle[hs.size()];
        out = hs.toArray(out);
        return out;
    }

    public InputHandle[] getDataHandles() {
        List<InputHandle> hs = new ArrayList<>();
        for (InputHandle h :
                inputHandles) {
            if (!h.getHandleDataType().equals(HandleDataType.FLOW))
                hs.add(h);
        }
        InputHandle[] out = new InputHandle[hs.size()];
        out = hs.toArray(out);
        return out;
    }

    public void drawInspector() {
        ImGui.labelText("Selected Node", title + getID());
        command.imgui(command.getAction().equals(Action.GET_CONSTANT));
    }
}
