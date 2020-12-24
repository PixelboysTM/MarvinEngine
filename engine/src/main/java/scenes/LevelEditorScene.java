package scenes;

import engine.Prefabs;
import engine.renderer.DebugDraw;
import org.joml.Vector3f;
import engine.ecs.Transform;
import engine.Window;
import engine.ecs.GameObject;
import engine.ecs.components.*;
import engine.renderer.Camera;
import engine.util.AssetPool;
import engine.util.Time;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class LevelEditorScene extends Scene {

    Spritesheet sprites;

    public LevelEditorScene() {
        System.out.println("Loaded Level Editor");
    }

    MouseControls mouseControls = new MouseControls();

    @Override
    public void init() {
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
        Window.get().a = 0;
        this.camera = new Camera(new Vector2f(0, 0));
        loadResources();
        sprites = AssetPool.getSpritesheet("assets/images/decorationsAndBlocks.png");
        if (loadedLevel){
            if (gameObjetcs.size() > 0)
                this.activeGameObject = gameObjetcs.get(0);
            return;
        }

        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj1 = new GameObject("OBJ1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)),2);
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1,1,1,1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new Rigidbody());

        GameObject obj2 = new GameObject("OBJ2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)),3);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("testImage.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);

        addGameObjectToScene(obj1);
        addGameObjectToScene(obj2);
        this.activeGameObject = obj1;




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

        for (GameObject g : gameObjetcs){
            if (g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTextureFull(spr.getTexture().getFilepath()));
                }
            }
        }

        System.out.println("Finished preloading internal Assets.");
        System.out.println("Loading took: " + (Time.getTime() - t) + "s");
    }

    @Override
    public void sceneImgui() {
        ImGui.begin("Scene Outline");
        if (ImGui.collapsingHeader("Scene Hierarchy")) {
            for (int i = 0; i < gameObjetcs.size(); i++) {
                ImGui.pushID("hallo" + i);
                if (ImGui.treeNode(gameObjetcs.get(i).getName())) {

                    ImGui.treePop();
                }
                if (ImGui.isItemClicked(0)) {
                    activeGameObject = gameObjetcs.get(i);
                }
                if (ImGui.isItemClicked(1)) {
                    ImGui.openPopup("SceneContext");
                }
                if (ImGui.beginPopup("ScenContext")){
                    if(ImGui.menuItem("Delete")){
                        gameObjetcs.remove(gameObjetcs.get(i));
                    }
                    ImGui.endPopup();
                }
                ImGui.popID();
            }
        }

        ImGui.separator();
        if (ImGui.button("Add new GameObject")){
            gameObjetcs.add(new GameObject("unnamed", new Transform(), 0));
        }
        ImGui.end();
    }

    float x = 0, y = 0;
    float angle = 0.0f;
    @Override
    public void Update(float dt) {
        mouseControls.update(dt);

        DebugDraw.addCircle2D(new Vector2f(x,y), 64, new Vector3f(0,1,0), 1);
        x += 50f * dt;
        y += 50f * dt;
        DebugDraw.addBox2D(new Vector2f(600,400), new Vector2f(64,32), angle, new Vector3f(0,1,0),1);
        angle += 50.0f * dt;

        //System.out.println("FPS: " + 1.0f/dt);
        for (GameObject g : gameObjetcs) {
            g.update(dt);
        }


        renderer.render();
    }

    @Override
    public void imgui(){
        super.imgui();
//        if (ImGui.beginMainMenuBar())
//        {
//            ImGui.button("Home");
//            ImGui.spacing();
//            if (ImGui.beginMenu("File"))
//            {
//                if (ImGui.menuItem("Save", "CTRL+S")) {
//                    saveExit();
//                }
//                if (ImGui.menuItem("Save AS")) {}
//                if (ImGui.menuItem("Open", "CTRL+O")) {
//                    load();
//                }
//                if (ImGui.menuItem("Quit", "CTRL+Q")) {
//                    glfwSetWindowShouldClose(Window.windowHandle(), true);
//                }
//
//                ImGui.endMenu();
//            }
//            ImGui.spacing();
//            if (ImGui.beginMenu("Edit"))
//            {
//                if (ImGui.menuItem("Undo", "CTRL+Z")) {}
//                if (ImGui.menuItem("Redo", "CTRL+Y", false, false)) {}  // Disabled item
//                ImGui.separator();
//                if (ImGui.menuItem("Paste", "CTRL+V")) {}
//                if (ImGui.menuItem("Cut", "CTRL+X")) {}
//                if (ImGui.menuItem("Copy", "CTRL+C")) {}
//                ImGui.endMenu();
//            }
//            ImGui.spacing();
//            if (ImGui.beginMenu("View")){
//                if (ImGui.beginMenu("Font")){
//                    if (ImGui.menuItem("Regular")){
//                       // Window.getImGuiLayer().ChangeFont("assets/fonts/ComicNeue-Regular.ttf", 24);
//                    }
//                    if (ImGui.menuItem("Light")){
//                     //   Window.getImGuiLayer().ChangeFont("assets/fonts/ComicNeue-Light.ttf", 24);
//                    }
//                    if (ImGui.menuItem("Bold")){
//                      //  Window.getImGuiLayer().ChangeFont("assets/fonts/ComicNeue-Bold.ttf", 24);
//                    }
//
//                    ImGui.endMenu();
//                }
//                ImGui.endMenu();
//            }
//            ImGui.spacing();
//            if (ImGui.beginMenu("Window")){
//                ImGui.menuItem("Inspector");
//                if(ImGui.beginMenu("2D")){
//                    ImGui.menuItem("Animator");
//                    ImGui.endMenu();
//                }
//                ImGui.endMenu();
//            }
//
//            ImGui.endMainMenuBar();
//        }

        ImGui.begin("Test Window");
        //decorationsAndBlocks.png

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x,texCoords[2].y )){
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                System.out.println("Picking up.");
                mouseControls.pickupObject(object);
            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();


    }

}
