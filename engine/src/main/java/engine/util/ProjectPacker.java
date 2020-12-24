package engine.util;
import engine.Window;
import engine.editor.NodeEditor.NodeEditor;
import org.lwjgl.PointerBuffer;
import scenes.TestNodeScene;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
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
                out.putNextEntry(new ZipEntry("scripts/" + editor.getName() + ".edit"));
                out.write(editor.getSaveData().getBytes(StandardCharsets.UTF_8));
                out.closeEntry();
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
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                if(entry.isDirectory()){
                    System.out.println("dir  : " + entry.getName());
                } else {
                    System.out.println("file : " + entry.getName());
                    char[] buf = new char[1024];
                    InputStreamReader reader = new InputStreamReader(file.getInputStream(entry));
                    StringBuilder sb = new StringBuilder();
                    int length;
                    while ((length = reader.read(buf, 0, buf.length)) != -1){
                        sb.append(buf, 0, length);
                    }

                    System.out.println(sb.toString());
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
                handleFileData(scenes.get(i).x, scripts.get(i).y);
            }
            for (int i = 0; i < scripts.size(); i++) {
                handleFileData(scenes.get(i).x, scripts.get(i).y);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleFileData(String name, String toString) {
        if (name.startsWith("scenes/")){
            Window.getScene().loadSceneFromData(toString);
        }else if(name.startsWith("scripts/")){
            name = name.substring(8);
            name = name.replace(".edit", "");
            ((TestNodeScene)Window.getScene()).setEditorfromData(name, toString);
        }
        //TODO: Make data
    }

    public static void clearProjectLocation() {
        filePath = null;
    }
}
