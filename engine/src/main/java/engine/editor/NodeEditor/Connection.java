package engine.editor.NodeEditor;

import engine.util.MyMath;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector3f;

public class Connection {
    private InputHandle inputHandle;
    private OutputHandle outputHandle;

    public Connection(InputHandle inputHandle, OutputHandle outputHandle) {
        this.inputHandle = inputHandle;
        this.outputHandle = outputHandle;
        inputHandle.setConnection(this);
        outputHandle.addConnection(this);
    }

   public boolean daw(ImDrawList list, ImVec2 mPos){
        boolean h = MyMath.linePoint(inputHandle.getPosX(), inputHandle.getPosY(), outputHandle.getPosX(), outputHandle.getPosY(), mPos.x, mPos.y);

       Vector3f c = NodeEnumMethods.getColor(inputHandle.getHandleDataType());
        int color = ImGui.getColorU32(c.x,c.y,c.z,1);
        //list.addLine(inputHandle.getPosX(), inputHandle.getPosY(), outputHandle.getPosX(), outputHandle.getPosY(), color, 8);
       float half = (inputHandle.getPosX() - outputHandle.getPosX())/2;
       list.addBezierCurve(outputHandle.getPosX(), outputHandle.getPosY(), outputHandle.getPosX() + half, outputHandle.getPosY(),
               inputHandle.getPosX() - half, inputHandle.getPosY(), inputHandle.getPosX(), inputHandle.getPosY(), color, h ? 10 : 6);
       return h;
   }

    public InputHandle getInputHandle() {
        return inputHandle;
    }

    public OutputHandle getOutputHandle() {
        return outputHandle;
    }

    public void unbind() {
        inputHandle.setConnection(null);
        outputHandle.getConnections().remove(this);
    }
}
