package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.ecs.Component;
import engine.ecs.GameObject;
import engine.ecs.Transform;
import engine.renderer.Camera;
import engine.renderer.Renderer;
import engine.util.gson.ComponentDeserializer;
import engine.util.gson.GameObjectDeserializer;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected Renderer renderer = new Renderer();
    protected List<GameObject> gameObjetcs = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean loadedLevel = false;

    public Scene() {

    }

    public void start() {
        for (GameObject g : gameObjetcs) {
            g.start();
            this.renderer.add(g);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject g) {
        System.out.println("Running: " + isRunning);
        if (!isRunning) {
            gameObjetcs.add(g);
        } else {
            gameObjetcs.add(g);
            g.start();
            renderer.add(g);
        }
    }

    public abstract void Update(float dt);

    public void lateUpdate(float dt){}

    public void init() {
    }

    public Camera camera() {
        return camera;
    }

    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }

    public void imgui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjetcs));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSceneData(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();



        return gson.toJson(this.gameObjetcs);
    }

    public void loadSceneFromData(String data){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        int maxGoId = -1;
        int maxCompId = -1;
        gameObjetcs.clear();
        GameObject[] objs = gson.fromJson(data, GameObject[].class);
        for (int i = 0; i < objs.length; i++) {
            addGameObjectToScene(objs[i]);
            for (Component c : objs[i].getAllComponents()){
                if (c.getUid() > maxCompId)
                    maxCompId = c.getUid();
            }
            if (objs[i].getUid() > maxGoId)
                maxGoId = objs[i].getUid();
        }
        maxGoId++;
        maxCompId++;
        GameObject.init(maxGoId);
        Component.init(maxCompId);
        this.loadedLevel = true;
    }
    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            gameObjetcs.clear();
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);
                for (Component c : objs[i].getAllComponents()){
                    if (c.getUid() > maxCompId)
                        maxCompId = c.getUid();
                }
                if (objs[i].getUid() > maxGoId)
                    maxGoId = objs[i].getUid();
            }
            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.loadedLevel = true;
        }
    }

    public Renderer renderer(){
        return renderer;
    }
    public void UpdateEditors(){}

    public GameObject findObjwithID(int id) {
        for (GameObject o :
                gameObjetcs) {
            if (o.getUid() == id)
                return o;
        }
        return null;
    }
}
