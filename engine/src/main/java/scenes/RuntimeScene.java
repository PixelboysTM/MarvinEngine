package scenes;

import com.google.gson.JsonArray;
import engine.Window;
import engine.ecs.GameObject;
import engine.editor.NodeEditor.Node;
import engine.editor.NodeEditor.NodeEditor;
import engine.editor.NodeEditor.Programm.VirtualMachine;
import engine.input.KeyListener;
import engine.renderer.Camera;
import engine.util.Settings;
import engine.util.Tuple;
import engine.util.gson.nodeStuff.runtime.NodeEditorRuntimeSerializer;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class RuntimeScene extends Scene{

    VirtualMachine vm;
    public RuntimeScene(List<GameObject> objs, Collection<NodeEditor> ps){

        gameObjetcs.clear();
        for (int i = 0; i < objs.size(); i++) {
            gameObjetcs.add(null);
        }
        Collections.copy(gameObjetcs, objs);

        List<Tuple<JsonArray, GameObject>> psForVm = new ArrayList<>();
        for (int i = 0; i < ps.size(); i++) {
            NodeEditor ned = (NodeEditor) ps.toArray()[i];
            JsonArray compiled = NodeEditorRuntimeSerializer.getRuntimeDataForObjectNodes(ned);
            GameObject g = findObjwithID(ned.getObjID());
            psForVm.add(new Tuple<>(compiled, g));
        }
        vm = new VirtualMachine(psForVm);
    }

    @Override
    public void init() {
        super.init();
        camera = new Camera(new Vector2f(-Settings.ViewportWidth / 2.0f, -Settings.ViewportHeight / 2.0f));
    }

    float elapsed = 0.0f;
    @Override
    public void Update(float dt) {
        for (GameObject g : gameObjetcs) {
            g.update(dt);
        }
        renderer.render();

        elapsed += dt;
        if (elapsed > 0.03f){
            vm.setCurrentDt(elapsed);
            if  (vm.hasStepsLeft())
                vm.step();

            elapsed = 0.0f;

            //EVENTS
            boolean[] k = KeyListener.getKeyPressed();
            for (int i = 0; i < k.length; i++) {
                if (k[i]){
                    vm.addKeyEvent(i);
                }
            }
        }

    }

    @Override
    public void sceneImgui() {
        imgui();

        if (ImGui.beginMainMenuBar()){
            if (ImGui.menuItem("Exit Run Mode")){
                Window.get().popScene();
            }
            ImGui.endMainMenuBar();
        }
    }
}
