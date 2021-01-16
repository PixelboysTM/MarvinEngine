package engine.ecs.components;

import engine.ecs.Transform;
import engine.ecs.Component;
import engine.renderer.RenderBatch;
import engine.renderer.Texture;
import engine.util.AssetPool;
import engine.util.ResourceManager;
import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpriteRenderer extends Component {


    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient Transform lastTransform;
    private transient boolean isDirty = true;


//    public SpriteRenderer(Vector4f color) {
//        this.color = color;
//        this.sprite = new Sprite(null);
//        isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        isDirty = true;
//    }

    @Override
    public void start() {
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color.set(color);
            isDirty = true;
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    @Override
    public void imgui() {
        if (ImGui.collapsingHeader("Sprite Renderer")) {
            ImGui.text("Color Picker:");
            float[] imCol = {color.x, color.y, color.z, color.w};
            if (ImGui.colorPicker4("", imCol)) {
                color.set(imCol[0], imCol[1], imCol[2], imCol[3]);
                isDirty = true;
            }

            ImGui.separator();
            ImGui.text("Texture");
            if (true || ResourceManager.textureIds().length > 0){
                String[] keys = ResourceManager.textureIds();

                ImInt in = new ImInt();
                int i;
                String path = "Select texture";
                if (sprite.getTexture() == null){
                    i = -1;
                }
                else {
                    path = sprite.getTexture().getFilepath();
                    path = path.replace("custom_", "");
                    //System.out.println("<yellow>" + path);
                    i = indexof(path, keys);
                }
                in.set(i);

                if(ImGui.beginCombo("##tex_combo", path)){

                    if (ImGui.selectable("No texture")){
                        sprite.setTexture(null);
                    }
                    ImGui.separator();
                    ImGui.text("Project Assets");
                    for (int j = 0; j < keys.length; j++) {
                        boolean is_selected = path.equals(keys[j]);
                        if (ImGui.selectable(keys[j], is_selected)){
                            path = keys[j];
                            sprite.setTexture(ResourceManager.getTexture(path));

                        }

                        if (ImGui.isItemHovered()){
                            ImGui.beginTooltip();
                            ImGui.image(ResourceManager.getTexture(keys[j]).getId(), 100,100, 0,1,1,0);
                            ImGui.endTooltip();
                        }

                        if (is_selected)
                            ImGui.setItemDefaultFocus();
                    }
                    ImGui.separator();
                    ImGui.text("internal Assets"); // TODO: Break path down to name
                    String[] assets = AssetPool.getAssetsTextures();
                    for (int j = 0; j < assets.length; j++) {
                        boolean is_selected = path.equals(assets[j]);
                        if (ImGui.selectable(assets[j], is_selected)){
                            path = assets[j];
                            sprite.setTexture(AssetPool.getTextureFull(path));

                        }

                        if (ImGui.isItemHovered()){
                            ImGui.beginTooltip();
                            ImGui.image(AssetPool.getTextureFull(assets[j]).getId(), 100,100, 0,1,1,0);
                            ImGui.endTooltip();
                        }

                        if (is_selected)
                            ImGui.setItemDefaultFocus();
                    }

                    ImGui.endCombo();
                }

//
//
//                ImGui.pushID("texture_pick");
//                if (ImGui.combo("", in, keys, keys.length)){
//                        sprite.setTexture(ResourceManager.getTexture(keys[in.get()]));
//                }
//                ImGui.popID();
//                if (sprite.getTexture() != null){
//                    if (ImGui.button("Remove Texture")){
//                        sprite.setTexture(null);
//                    }
//                }
            }else {
                ImGui.textWrapped("Import Images to use them on Objects.");
            }
        }
        ImGui.spacing();
    }

    private static <T> List<T> getList(T[] clone) {
        List<T> ls = new ArrayList<>();
        for (int i = 0; i < clone.length; i++) {
            ls.add(clone[i]);
        }
        return ls;
    }

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
        isDirty = true;
    }

    private static <T> int indexof(T item, T[] vars ){
        for (int i = 0; i < vars.length; i++) {
            if (vars[i].equals(item)){
                return i;
            }
        }
        return -1;
    }
}
