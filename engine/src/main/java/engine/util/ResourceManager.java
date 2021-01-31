package engine.util;

import engine.renderer.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
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

    public static boolean addTexture(String filepath, String id){
        if (textures.containsKey(id)){
            assert false : "Add replacement source option";
            return false;
        }

        Texture t = new Texture();
        t.init(filepath);
        textures.put(id, t);
        return false;
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




        List<String> s = Arrays.asList(path.split("\\.")[0].replace("\\", "/").split("/"));
        Collections.reverse(s);
        String id = s.get(0);
        ResourceManager.addTexture(path, id);

        File original = new File(path);
        String p = System.getProperty("java.io.tmpdir") + "marvin\\im\\" + id + ".mim";
        System.out.println(p);
        File copied = new File(p);
        File parent = copied.getParentFile();
        if (!parent.exists() && !parent.mkdirs())
            throw new IllegalStateException("Coudlnt create dir");
        try {
            InputStream in = new BufferedInputStream(
                    new FileInputStream(original)
            );
            OutputStream out = new BufferedOutputStream(
                    new FileOutputStream(copied)
            );

            byte[] buffer = new byte[1024];
            int lengthRead = 0;
            while ((lengthRead = in.read(buffer)) > 0){
                out.write(buffer, 0, lengthRead);
                out.flush();
            }

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }
    public static String[] textureIds(){
        return textures.keySet().toArray(new String[0]);
    }
}
