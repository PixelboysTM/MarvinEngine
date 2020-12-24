package engine.util;

import engine.ecs.components.Sprite;
import engine.ecs.components.Spritesheet;
import engine.renderer.Shader;
import engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShaderFull(String resourceName){
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        }

        Shader shader = new Shader(resourceName);
        shader.compile();
        AssetPool.shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }
    public static Shader getShader(String fileName){
        return getShaderFull("assets/shaders/" + fileName);
    }

    public static Texture getTextureFull(String resourceName){
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        }

        Texture texture = new Texture();
        texture.init(resourceName);
        AssetPool.textures.put(file.getAbsolutePath(), texture);
        return texture;
    }
    public static Texture getTexture(String fileName){
        return getTextureFull("assets/images/" + fileName);
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet){
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName){
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
           assert false : "ERROR: Tried to acesss not known spritescheet.";
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
