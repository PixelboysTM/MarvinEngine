package engine.ecs;

import engine.Window;
import engine.ecs.components.Rigidbody;
import engine.ecs.components.SpriteRenderer;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiButtonFlags;
import imgui.flag.ImGuiHoveredFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    private int zIndex;
    public Transform transform = new Transform();

//    public GameObject(String name) {
//        this.name = name;
//        this.components = new ArrayList<>();
//        this.zIndex = 0;
//    }
    public GameObject(String name, Transform transform, int zIndex){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c :
                components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "ERROR: Casting component";
                }
            }
        }
        return null;
    }
    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        c.generateID();
        this.components.add(c);
        c.gameObject = this;
    }
    public void update(float dt){
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }
    public void start(){
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }
    public int zIndex(){
        return zIndex;
    }

    public void imgui(){
        ImString string = new ImString(256);
        string.set(name, false);
       if( ImGui.inputText("Name", string)){
           name = string.get().trim();
           Window.getScene().UpdateEditors();
       }
       ImGui.sameLine();
      if( ImGui.button(" + ")){
          ImGui.openPopup("addDialogGOBJ");
      }
      if (ImGui.beginPopup("addDialogGOBJ")){
          ImGui.text("Add Component to GameObject");
          ImGui.separator();
          if(ImGui.menuItem("Sprite Renderer")){
              if (getComponent(SpriteRenderer.class) == null) {
                  SpriteRenderer spr = new SpriteRenderer();
                  addComponent(spr);
                  spr.start();
                  Window.getScene().renderer().add(spr);
              }
          }
          if(ImGui.menuItem("Rigidbody")){
              if (getComponent(Rigidbody.class) == null) {
                  Rigidbody spr = new Rigidbody();
                  addComponent(spr);
                  spr.start();
              }
          }
          ImGui.endPopup();
      }

       ImGui.spacing();

        transform.imgui();
        for (Component c : components){
            c.imgui();
        }
    }

    public String getName() {
        return name;
    }

    public static void init(int maxID){ ID_COUNTER = maxID;}

    public int getUid() {
        return uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }
}
