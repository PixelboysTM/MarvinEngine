package engine.util;

import imgui.ImGui;
import imgui.type.ImBoolean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LogHelper {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private ByteArrayOutputStream stream;
    private List<String> log;
    private PrintStream def;

    public LogHelper(){
        stream = new ByteArrayOutputStream();
        log = new ArrayList<>();
        def = System.out;
        System.setOut(new PrintStream(stream));
    }

    public boolean imgui(){
        ImBoolean close = new ImBoolean();
        ImGui.begin("Console", close);
        try {
            String newS = stream.toString("UTF8");
            if (!newS.equals("")){
                stream.reset();
                String[] ar = newS.split("\n");
                for (int i = 0; i < ar.length; i++) {
                    printToConsole(ar[i]);
                    log.add(ar[i]);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < log.size(); i++) {
            String toLog = log.get(log.size() - 1 - i);
            if (toLog.contains("<green>"))
                ImGui.textColored(ImGui.getColorU32(0.4f,0.9f,0.1f, 1), toLog.replace("<green>", ""));
            else if (toLog.contains("<red>"))
                ImGui.textColored(ImGui.getColorU32(0.9f,0.4f,0.1f, 1), toLog.replace("<red>", ""));
            else if (toLog.contains("<yellow>"))
                ImGui.textColored(ImGui.getColorU32(0.9f,0.8f,0.2f, 1), toLog.replace("<yellow>", ""));
            else
                ImGui.textWrapped(toLog);
        }
        ImGui.end();
        return !close.get();
    }

    private void printToConsole(String s) {
        if (s.contains("<green>"))
            def.println(s.replace("<green>", ANSI_GREEN) + ANSI_RESET);
        else if (s.contains("<red>"))
            def.println(s.replace("<red>", ANSI_RED) + ANSI_RESET);
        else if (s.contains("<yellow>"))
            def.println(s.replace("<yellow>", ANSI_YELLOW) + ANSI_RESET);
        else
            def.println(s);
    }
}
