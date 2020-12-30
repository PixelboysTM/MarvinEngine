package engine.input;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class KeyCodes {
    public static final Map<String, Integer> KEY_CODES = new HashMap<String, Integer>(){
        {
            put("Space", GLFW_KEY_SPACE);
            put("Arrow up", GLFW_KEY_UP);
            put("Arrow down", GLFW_KEY_DOWN);
            put("Arrow left", GLFW_KEY_LEFT);
            put("Arrow right", GLFW_KEY_RIGHT);
            put("W", GLFW_KEY_W);
            put("A", GLFW_KEY_A);
            put("S", GLFW_KEY_S);
            put("D", GLFW_KEY_D);
        }


    };
}
