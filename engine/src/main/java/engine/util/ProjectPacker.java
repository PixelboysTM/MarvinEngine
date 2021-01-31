package engine.util;
import com.google.gson.*;
import engine.Window;
import engine.editor.NodeEditor.NodeEditor;
import engine.renderer.Texture;
import org.lwjgl.PointerBuffer;
import scenes.TestNodeScene;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class ProjectPacker {

    private static String filePath;
    private static boolean isProjectOpen(){return filePath != null;}

    public static void open(){
        PointerBuffer outPath = memAllocPointer(1);

        try {
            String path = checkResult(
                    NFD_OpenDialog("marvin;zip,zipped", null, outPath),
                    outPath

            );
            filePath = path;
            loadFile(path);
        } finally {
            memFree(outPath);
        }

    }
    public static String selectProjectLocationForSave(){
        PointerBuffer savePath = memAllocPointer(1);

        try {
           String re = checkResult(
                    NFD_SaveDialog("marvin;zip,zipped", null, savePath),
                    savePath
            );
           filePath = re;
            return re;
        } finally {
            memFree(savePath);
        }

    }

    public static void save(){
        if (!isProjectOpen()){
            selectProjectLocationForSave();
        }
        if (filePath != null){
            saveAs(filePath);
        }
    }

    public static void saveAs(String path){
        if (path == null) return;
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path));

            out.putNextEntry(new ZipEntry("scenes/scene.lvl"));
            String sceneText = Window.getScene().getSceneData();
            out.write(sceneText.getBytes(StandardCharsets.UTF_8)); //Write files;
            out.closeEntry();

            for (NodeEditor editor : ((TestNodeScene)Window.getScene()).getScriptEditors()){
                out.putNextEntry(new ZipEntry("scripts/" + editor.getObjID() + ".edit"));
                out.write(editor.getSaveData().getBytes(StandardCharsets.UTF_8));
                out.closeEntry();
            }


            for (String id : ResourceManager.textureIds()){
                out.putNextEntry(new ZipEntry("assets/images/" + id + ".img"));
                String p = System.getProperty("java.io.tmpdir") + "marvin\\im\\" + id + ".mim";
                System.out.println(p);
                File f = new File(p);
                File parent = f.getParentFile();
                if (!parent.exists() && !parent.mkdirs())
                    throw new IllegalStateException("Coudlnt create dir");

                InputStream in = new BufferedInputStream(new FileInputStream(f));

                byte[] buffer = new byte[1024];
                int lengthRead = 0;
                while ((lengthRead = in.read(buffer)) > 0){
                    out.write(buffer, 0, lengthRead);
                    out.flush();
                }
                out.closeEntry();
                in.close();
            }

            out.close();
        } catch (IOException e) {
            System.out.println("ff");
            e.printStackTrace();
        }


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
    private static void loadFile(String path) { //Load data to scene for example assets, level, scripts
        System.out.println(path);
        if (path == null) return;
        try {
           ZipFile file = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = file.entries();

            List<Tuple<String, String>> data = new ArrayList<>();
            Map<String, List<Byte>> imData = new HashMap<>();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                if(entry.isDirectory()){
                    System.out.println("dir  : " + entry.getName());
                } else {
                    System.out.println("file : " + entry.getName());
                    if (entry.getName().endsWith(".img")){

                        String temp = System.getProperty("java.io.tmpdir");// + "marvin\\im\\" + id + ".mim";
                        String id = entry.getName();
                        id = id.replace("assets/images/","");
                        id = id.replace(".img", "");
                        System.out.println(id);
                        File f = new File(temp + "marvin\\im\\" + id + ".mim");
                        File parent = f.getParentFile();
                        if (!parent.exists() && !parent.mkdirs())
                            throw new IllegalStateException("Coudlnt create dir");
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));

                        byte[] buffer = new byte[1024];
                        int lengthRead = 0;
                        InputStream in = new BufferedInputStream( file.getInputStream(entry));
                        while ((lengthRead = in.read(buffer)) > 0){
                            out.write(buffer, 0, lengthRead);
                            out.flush();
                        }
                        out.close();
                        in.close();
                        ResourceManager.addTexture(temp + "marvin\\im\\" + id + ".mim", id);

                        continue;
                    }

                    char[] buf = new char[1024];
                    InputStreamReader reader = new InputStreamReader(file.getInputStream(entry));
                    StringBuilder sb = new StringBuilder();
                    int length;
                    while ((length = reader.read(buf, 0, buf.length)) != -1){
                        sb.append(buf, 0, length);
                    }

                    //System.out.println(sb.toString());
                    data.add(new Tuple<>( entry.getName(), sb.toString()));
                }
            }
            file.close();
            List<Tuple<String, String>> scenes = new ArrayList<>();
            List<Tuple<String, String>> scripts = new ArrayList<>();
            for (Tuple<String, String> t :
                    data) {
                if (t.x.startsWith("scenes/"))
                    scenes.add(t);
                else if(t.x.startsWith("scripts/"))
                    scripts.add(t);
            }
            for (int i = 0; i < scenes.size(); i++) {
                handleFileData(scenes.get(i).x, scenes.get(i).y);
            }
            for (int i = 0; i < scripts.size(); i++) {
                handleFileData(scripts.get(i).x, scripts.get(i).y);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleImageData(String name, String spec, List<Byte> data){
        name = name.substring(14);
        name = name.replace(".id", "");
        Gson g = new GsonBuilder().create();
        JsonObject obj = g.fromJson(spec, JsonObject.class);
        int[] w = new int[] { obj.get("width").getAsInt()};
        int[] h = new int[] { obj.get("height").getAsInt()};
        int[] c = new int[] { obj.get("channels").getAsInt()};
        byte[] bufData = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            bufData[i] = data.get(i);
        }
        ByteBuffer buf;
        buf = ByteBuffer.wrap(bufData);
        ResourceManager.addTextureFromData(name, buf, w[0],h[0],c[0]);
    }

    private static void handleFileData(String name, String toString) {
        if (name.startsWith("scenes/")){
            Window.get().setSingle(new TestNodeScene());
            Window.getScene().loadSceneFromData(toString);
        }else if(name.startsWith("scripts/")) {
            name = name.substring(8);
            name = name.replace(".edit", "");
            int id = Integer.parseInt(name);
            ((TestNodeScene) Window.getScene()).setEditorfromData(id, toString);

        }
        //TODO: Make data (Image data)
    }

    public static void clearProjectLocation() {
        filePath = null;
    }
}
