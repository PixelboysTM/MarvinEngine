package engine.util;

import imgui.ImGui;

public class Settings {
    public static final int ViewportWidth = 1600;
    public static final int ViewportHeight = 1200;
    public static final float TargetAspectRatio = (float) ViewportWidth/ (float) ViewportHeight;
    public static String WindowName = "Marvin Engine |";
    public static int InspectorID = 132562734;//ImGui.getID("inspector");
}
