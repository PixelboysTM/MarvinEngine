package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.Window;
import engine.ecs.GameObject;
import engine.ecs.Transform;
import engine.ecs.components.MouseControls;
import engine.ecs.components.SpriteRenderer;
import engine.ecs.components.Spritesheet;
import engine.editor.GameViewWindow;
import engine.editor.NodeEditor.*;
import engine.renderer.Camera;
import engine.util.*;
import engine.util.gson.nodeStuff.*;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.io.File;
import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestNodeScene extends Scene {

    enum InspectorFocus {
        GAME_OBJECT,
        NODE_EDITOR,
        NONE
    }
    private NodeEditor lastDrawnNodeEditor = null;

    private InspectorFocus lastFocus = InspectorFocus.NONE;

    Map<GameObject, NodeEditor> editorDictionary = new Hashtable<>();
    ImBoolean is = new ImBoolean();

    MouseControls mouseControls = new MouseControls();

    @Override
    public void Update(float dt) {
        mouseControls.update(dt);

        //System.out.println("FPS: " + 1.0f/dt);
        for (GameObject g : gameObjetcs) {
            g.update(dt);
        }


        renderer.render();
    }

    @Override
    public void lateUpdate(float dt) {
        if (dt < 0) return;

        for (Map.Entry<GameObject, NodeEditor> e : editorDictionary.entrySet()) {
            NodeEditor n = e.getValue();
            n.draw();
        }


    }

    private void loadResources() {
        float t = Time.getTime();
        System.out.println("Preloading Assets from internal Directory.");

        //Shaders
        File fs = new File("assets/shaders");
        for (File f : fs.listFiles()) {
            AssetPool.getShader(f.getName());
        }

        //Textures
        fs = new File("assets/images");
        for (File f : fs.listFiles()) {
            AssetPool.getTexture(f.getName());
        }

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("spritesheet.png"), 16, 16, 26, 0));
        AssetPool.addSpritesheet("assets/images/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("decorationsAndBlocks.png"), 16, 16, 81, 0));

        for (GameObject g : gameObjetcs) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTextureFull(spr.getTexture().getFilepath()));
                }
            }
        }

        System.out.println("Finished preloading internal Assets.");
        System.out.println("Loading took: " + (Time.getTime() - t) + "s");
    }


    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-Settings.ViewportWidth / 2.0f, -Settings.ViewportHeight / 2.0f));
         loadResources();
    }

    @Override
    public void sceneImgui() {
        //super.sceneImgui();
        imgui();


        //Scene
        boolean sceneFocus = false;
        ImGui.begin("Scene Outline");
        sceneFocus = ImGui.isWindowFocused();
        if (ImGui.collapsingHeader("Scene Hierarchy")) {
            for (int i = 0; i < gameObjetcs.size(); i++) {
                ImGui.pushID("sceneNodeHie" + i);
                 ImGui.selectable(gameObjetcs.get(i).getName(), gameObjetcs.get(i) == activeGameObject);
                if (ImGui.isItemClicked(0)) {
                    activeGameObject = gameObjetcs.get(i);
                }
                if (ImGui.isItemClicked(1)) {
                    ImGui.openPopup("SceneContext");
                }
                if (ImGui.beginPopup("SceneContext")) {
                    if (ImGui.menuItem("Open Script")) {
                        String name = gameObjetcs.get(i).getName();
                        if (!editorDictionary.containsKey(gameObjetcs.get(i))) {
                            editorDictionary.put(gameObjetcs.get(i), new NodeEditor(gameObjetcs.get(i)));
                        }
                    }
                    if (ImGui.menuItem("Delete")) {
                        gameObjetcs.remove(gameObjetcs.get(i));
                    }
                    ImGui.endPopup();
                }
                ImGui.popID();
            }
        }

        ImGui.separator();
        if (ImGui.button("Add new GameObject")) {
            gameObjetcs.add(new GameObject("unnamed", new Transform(), 0));
        }
        ImGui.end();

//Inspector
        ImGui.pushID(Settings.InspectorID);
        ImGui.begin("Inspector");
        if (GameViewWindow.isFocused() || sceneFocus)
            lastFocus = InspectorFocus.GAME_OBJECT;
        if ((GameViewWindow.isFocused() || sceneFocus) && activeGameObject != null) {
            activeGameObject.imgui();
        } else {
            boolean drawn = false;
            for (NodeEditor editor :
                    editorDictionary.values()) {
                if (editor.isFocused()) {
                    lastFocus = InspectorFocus.NODE_EDITOR;
                    editor.inspector();
                    lastDrawnNodeEditor = editor;
                    drawn = true;
                }
            }
            if (!drawn) {
                switch (lastFocus) {
                    case GAME_OBJECT:
                        if (activeGameObject != null)
                            activeGameObject.imgui();
                        break;
                    case NODE_EDITOR:
                        if (lastDrawnNodeEditor != null)
                            lastDrawnNodeEditor.inspector();
                        break;

                }
            }
        }

        ImGui.end();
        ImGui.popID();

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("New")) {
                    Window.get().setSingle(new TestNodeScene());
                    ProjectPacker.clearProjectLocation();
                }
                if (ImGui.menuItem("Open")) {
                    ProjectPacker.open();
                }
                ImGui.separator();
                if (ImGui.menuItem("Save")) {
                    ProjectPacker.save();
                }
                if (ImGui.menuItem("Save as")) {
                    ProjectPacker.saveAs(ProjectPacker.selectProjectLocationForSave());
                }
                ImGui.separator();
                ImGui.menuItem("Settings");
                ImGui.separator();
                if (ImGui.menuItem("Quit")) {
                    ProjectPacker.save();
                    glfwSetWindowShouldClose(Window.windowHandle(), true);
                }

                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Edit")) {
                if (ImGui.menuItem("Import Resources")) {

                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Window")) {

                if (ImGui.menuItem("Console", "", is)) {
                    Window.get().setShowConsole(is.get());
                }
                ImGui.end();
            }
            ImGui.separator();
            if (ImGui.beginMenu("Run")){
                if(ImGui.menuItem("Run Scene")){
                    Window.get().pushScene(new RuntimeScene(gameObjetcs, editorDictionary.values()));
                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }

    }

    @Override
    public void imgui() {
        for (Map.Entry<GameObject, NodeEditor> e : editorDictionary.entrySet()
        ) {
            e.getValue().imgui();
        }
    }

    public NodeEditor[] getScriptEditors() {
        return editorDictionary.values().toArray(new NodeEditor[]{});
    }

    public void setEditorfromData(int id, String data) {
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

       GameObject obj = findObjwithID(id);
       if (obj == null)return;

        if (!editorDictionary.containsKey(obj)) {
            editorDictionary.put(obj, new NodeEditor(obj));
        }
        editorDictionary.get(obj).setData(data);
    }



    @Override
    public void UpdateEditors() {
        for (NodeEditor edits :
                editorDictionary.values()) {
            edits.setName(findObjwithID( edits.getObjID()));
        }
    }
}

