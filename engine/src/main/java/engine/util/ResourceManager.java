package engine.util;

import engine.renderer.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class ResourceManager {
    private static Map<String, Texture> textures = new HashMap<>();

    public static void clearRessources(){
        textures = new HashMap<>();
    }
    public static Texture getTexture(String id){
        return textures.getOrDefault(id, null);
    }
    public static boolean addTextureFromData(String id, ByteBuffer data, int w, int h, int c){
        if (textures.containsKey(id)){
            assert false : "Add replacement source option";
            return false;
        }

        Texture t = new Texture();
        t.init(data,w,h,c, id );
        textures.put(id, t);
        return true;
    }

    private static String checkResult(int result, PointerBuffer path) {
        switch (result) {
            case NFD_OKAY:
                System.out.println("Success!");
                nNFD_Free(path.get(0));
                return path.getStringUTF8();
            case NFD_CANCEL:
                System.out.println("User pressed cancel.");
                break;
            default: // NFD_ERROR
                System.err.format("Error: %s\n", NFD_GetError());

        }
        return null;
    }

    public static String loadNewTexture(){
        PointerBuffer outPath = memAllocPointer(1);
        String path = "";
        try {
            path = checkResult(
                    NFD_OpenDialog("png;jpg,jpeg", null, outPath),
                    outPath

            );
        } finally {
            memFree(outPath);
        }


        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer ch = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer f = stbi_load(path, w, h, ch, 0 );

        List<String> s = Arrays.asList(path.split("\\.")[0].replace("\\", "/").split("/"));
        Collections.reverse(s);
        String id = s.get(0);
        ResourceManager.addTextureFromData(id, f,w.get(0),h.get(0),ch.get(0));
        return id;
    }
    public static String[] textureIds(){
        return textures.keySet().toArray(new String[0]);
    }
}
