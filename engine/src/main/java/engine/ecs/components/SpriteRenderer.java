package engine.ecs.components;

import engine.ecs.Transform;
import engine.ecs.Component;
import engine.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

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
        }
        ImGui.spacing();
    }

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
    }
}
