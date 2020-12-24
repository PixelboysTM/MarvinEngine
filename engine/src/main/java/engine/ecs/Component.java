package engine.ecs;

import engine.util.annotations.Slider;
import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void start() {
    }

    public void update(float dt) {
    }

    public void imgui() {
        if (ImGui.collapsingHeader(this.getClass().getSimpleName())) {
            try {
                Field[] fields = this.getClass().getDeclaredFields();
                for (Field field : fields) {
                    boolean isTransient = Modifier.isTransient(field.getModifiers());

                    if (isTransient)
                        continue;
                    DrawElements(field);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ImGui.spacing();
    }

    private void DrawElements(Field field) throws IllegalAccessException {
        boolean isPrivate = Modifier.isPrivate(field.getModifiers());
        if (isPrivate) {
            field.setAccessible(true);
        }
        Class type = field.getType();
        Object value = field.get(this);
        String name = field.getName();


        //INTEGER
        if (type == int.class) {
            int val = (int) value;
            int[] imVec = {val};
            if (!field.isAnnotationPresent(Slider.class)) {
                if (ImGui.dragInt(name, imVec)) {
                    field.set(this, imVec[0]);
                }
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        if (ImGui.dragInt(name, imVec)) {
                            field.set(this, imVec[0]);
                        }
                        break;
                    case Slider:
                        if (ImGui.sliderInt(name, imVec, h.min(), h.max())) {
                            field.set(this, imVec[0]);
                        }
                        break;
                    case Value:
                        ImInt f = new ImInt(imVec[0]);
                        if (ImGui.inputInt(name, f)) {
                            field.set(this, f.get());
                        }
                        break;
                }
            }
        }
        //FLOAT
        else if (type == float.class) {
            float val = (float) value;
            float[] imVec = {val};
            if (!field.isAnnotationPresent(Slider.class)) {
                if (ImGui.dragFloat(name, imVec)) {
                    field.set(this, imVec[0]);
                }
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        if (ImGui.dragFloat(name, imVec)) {
                            field.set(this, imVec[0]);
                        }
                        break;
                    case Slider:
                        if (ImGui.sliderFloat(name, imVec, h.min(), h.max())) {
                            field.set(this, imVec[0]);
                        }
                        break;
                    case Value:
                        ImFloat f = new ImFloat(imVec[0]);
                        if (ImGui.inputFloat(name, f)) {
                            field.set(this, f.get());
                        }
                        break;
                }
            }
        }
        //BOOLEAN
        else if (type == boolean.class) {
            boolean val = (boolean) value;
            if (ImGui.checkbox(name, val)) {
                field.set(this, !val);
            }
        }
        //VECTOR3
        else if (type == Vector3f.class) {
            Vector3f val = (Vector3f) value;
            float[] imVec = {val.x, val.y, val.z};
            if (!field.isAnnotationPresent(Slider.class)) {
                if (ImGui.dragFloat3(name, imVec)) {
                    val.set(imVec[0], imVec[1], imVec[2]);
                }
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        if (ImGui.dragFloat3(name, imVec)) {
                            val.set(imVec[0], imVec[1], imVec[2]);
                        }
                        break;
                    case Slider:
                        if (ImGui.sliderFloat3(name, imVec, h.min(), h.max())) {
                            val.set(imVec[0], imVec[1], imVec[2]);
                        }
                        break;
                    case Value:
                        if (ImGui.inputFloat3(name, imVec)) {
                            val.set(imVec[0], imVec[1], imVec[2]);
                        }
                        break;
                }
            }
        }
        //VECTOR4
        else if (type == Vector4f.class) {
            Vector4f val = (Vector4f) value;
            float[] imVec = {val.x, val.y, val.z, val.w};
            if (!field.isAnnotationPresent(Slider.class)) {
                if (ImGui.dragFloat4(name, imVec)) {
                    val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                }
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        if (ImGui.dragFloat4(name, imVec)) {
                            val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                        }
                        break;
                    case Slider:
                        if (ImGui.sliderFloat4(name, imVec, h.min(), h.max())) {
                            val.set(imVec[0], imVec[1], imVec[2], imVec[4]);
                        }
                        break;
                    case Value:
                        if (ImGui.inputFloat4(name, imVec)) {
                            val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                        }
                        break;
                }
            }
        }
        //VECTOR2
        else if (type == Vector2f.class) {
            Vector2f val = (Vector2f) value;
            float[] imVec = {val.x, val.y};
            if (!field.isAnnotationPresent(Slider.class)) {
                if (ImGui.dragFloat2(name, imVec)) {
                    val.set(imVec[0], imVec[1]);
                }
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        if (ImGui.dragFloat2(name, imVec)) {
                            val.set(imVec[0], imVec[1]);
                        }
                        break;
                    case Slider:
                        if (ImGui.sliderFloat2(name, imVec, h.min(), h.max())) {
                            val.set(imVec[0], imVec[1]);
                        }
                        break;
                    case Value:
                        if (ImGui.inputFloat2(name, imVec)) {
                            val.set(imVec[0], imVec[1]);
                        }
                        break;
                }
            }
        }
        //Matrix4f
        else if (type == Matrix4f.class) {
            Matrix4f val = (Matrix4f) value;
            float[] imVec0 = {val.get(0, 0), val.get(0, 1), val.get(0, 2), val.get(0, 3)};
            float[] imVec1 = {val.get(1, 0), val.get(1, 1), val.get(1, 2), val.get(1, 3)};
            float[] imVec2 = {val.get(2, 0), val.get(2, 1), val.get(2, 2), val.get(2, 3)};
            float[] imVec3 = {val.get(3, 0), val.get(3, 1), val.get(3, 2), val.get(3, 3)};
            ImGui.text(name);
            if (!field.isAnnotationPresent(Slider.class)) {
                ImGui.pushID(0);
                if (ImGui.dragFloat4("", imVec0)) {
                    val.set(0, 0, imVec0[0]);
                    val.set(0, 1, imVec0[1]);
                    val.set(0, 2, imVec0[2]);
                    val.set(0, 3, imVec0[3]);
                }
                ImGui.popID();
                ImGui.pushID(1);
                if (ImGui.dragFloat4("", imVec1)) {
                    val.set(1, 0, imVec1[0]);
                    val.set(1, 1, imVec1[1]);
                    val.set(1, 2, imVec1[2]);
                    val.set(1, 3, imVec1[3]);
                }
                ImGui.popID();
                ImGui.pushID(2);
                if (ImGui.dragFloat4("", imVec2)) {
                    val.set(2, 0, imVec2[0]);
                    val.set(2, 1, imVec2[1]);
                    val.set(2, 2, imVec2[2]);
                    val.set(2, 3, imVec2[3]);
                }
                ImGui.popID();
                ImGui.pushID(3);
                if (ImGui.dragFloat4("", imVec3)) {
                    val.set(3, 0, imVec3[0]);
                    val.set(3, 1, imVec3[1]);
                    val.set(3, 2, imVec3[2]);
                    val.set(3, 3, imVec3[3]);
                }
                ImGui.popID();
            } else {
                Slider h = field.getAnnotation(Slider.class);
                switch (h.handleType()) {
                    case Drag:
                        ImGui.pushID(0);
                        if (ImGui.dragFloat4("", imVec0)) {
                            val.set(0, 0, imVec0[0]);
                            val.set(0, 1, imVec0[1]);
                            val.set(0, 2, imVec0[2]);
                            val.set(0, 3, imVec0[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(1);
                        if (ImGui.dragFloat4("", imVec1)) {
                            val.set(1, 0, imVec1[0]);
                            val.set(1, 1, imVec1[1]);
                            val.set(1, 2, imVec1[2]);
                            val.set(1, 3, imVec1[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(2);
                        if (ImGui.dragFloat4("", imVec2)) {
                            val.set(2, 0, imVec2[0]);
                            val.set(2, 1, imVec2[1]);
                            val.set(2, 2, imVec2[2]);
                            val.set(2, 3, imVec2[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(3);
                        if (ImGui.dragFloat4("", imVec3)) {
                            val.set(3, 0, imVec3[0]);
                            val.set(3, 1, imVec3[1]);
                            val.set(3, 2, imVec3[2]);
                            val.set(3, 3, imVec3[3]);
                        }
                        ImGui.popID();
                        break;
                    case Slider:
                        ImGui.pushID(0);
                        if (ImGui.sliderFloat4("", imVec0, h.min(), h.max())) {
                            val.set(0, 0, imVec0[0]);
                            val.set(0, 1, imVec0[1]);
                            val.set(0, 2, imVec0[2]);
                            val.set(0, 3, imVec0[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(1);
                        if (ImGui.sliderFloat4("", imVec1, h.min(), h.max())) {
                            val.set(1, 0, imVec1[0]);
                            val.set(1, 1, imVec1[1]);
                            val.set(1, 2, imVec1[2]);
                            val.set(1, 3, imVec1[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(2);
                        if (ImGui.sliderFloat4("", imVec2, h.min(), h.max())) {
                            val.set(2, 0, imVec2[0]);
                            val.set(2, 1, imVec2[1]);
                            val.set(2, 2, imVec2[2]);
                            val.set(2, 3, imVec2[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(3);
                        if (ImGui.sliderFloat4("", imVec3, h.min(), h.max())) {
                            val.set(3, 0, imVec3[0]);
                            val.set(3, 1, imVec3[1]);
                            val.set(3, 2, imVec3[2]);
                            val.set(3, 3, imVec3[3]);
                        }
                        ImGui.popID();
                        break;
                    case Value:
                        ImGui.pushID(0);
                        if (ImGui.inputFloat4("", imVec0)) {
                            val.set(0, 0, imVec0[0]);
                            val.set(0, 1, imVec0[1]);
                            val.set(0, 2, imVec0[2]);
                            val.set(0, 3, imVec0[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(1);
                        if (ImGui.inputFloat4("", imVec1)) {
                            val.set(1, 0, imVec1[0]);
                            val.set(1, 1, imVec1[1]);
                            val.set(1, 2, imVec1[2]);
                            val.set(1, 3, imVec1[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(2);
                        if (ImGui.inputFloat4("", imVec2)) {
                            val.set(2, 0, imVec2[0]);
                            val.set(2, 1, imVec2[1]);
                            val.set(2, 2, imVec2[2]);
                            val.set(2, 3, imVec2[3]);
                        }
                        ImGui.popID();
                        ImGui.pushID(3);
                        if (ImGui.inputFloat4("", imVec3)) {
                            val.set(3, 0, imVec3[0]);
                            val.set(3, 1, imVec3[1]);
                            val.set(3, 2, imVec3[2]);
                            val.set(3, 3, imVec3[3]);
                        }
                        ImGui.popID();
                        break;
                }
            }
        }

        if (isPrivate) {
            field.setAccessible(false);
        }


    }

    public void generateID(){
        if (this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }
    public int getUid(){
        return uid;
    }

    public static void init(int maxID){
        ID_COUNTER = maxID;
    }
}
