package engine.editor;

import engine.Window;
import engine.renderer.Texture;
import engine.util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;

public class GameViewWindow {
    private static boolean isFocused;
    public static boolean isFocused(){
        return isFocused;
    }
    public static void imgui(){
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);

        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        isFocused = ImGui.isWindowFocused();
        ImVec2 winSize = getLargestSizeForViewport();
        ImVec2 winPos = getCenteredPositionForViewport(winSize);

        ImGui.setCursorPos(winPos.x, winPos.y);
        int texId = Window.getFrameBuffer().getTextureId();
//        int[] pixels = {};
//        glGetTexImage(GL_TEXTURE_2D, 0, GL_UNSIGNED_INT, GL_RGB, pixels);
//        for (int i = 0; i < pixels.length; i++) {
//            System.out.println("Index "+ i + " : " + pixels[i]);
//        }
        ImGui.image(texId, winSize.x, winSize.y, 0,1,1,0);
        ImGui.end();

        ImGui.popStyleVar();
    }

    private static ImVec2 getLargestSizeForViewport() {
        ImVec2 winSize = new ImVec2();
        ImGui.getContentRegionAvail(winSize);
        winSize.x -= ImGui.getScrollX();
        winSize.y -= ImGui.getScrollY();

        float aspectWidth = winSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > winSize.y){
            aspectHeight = winSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 winSize = new ImVec2();
        ImGui.getContentRegionAvail(winSize);
        winSize.x -= ImGui.getScrollX();
        winSize.y -= ImGui.getScrollY();

        float viewportX = (winSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (winSize.y / 2.0f) - (aspectSize.y / 2.0f);
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY+ ImGui.getCursorPosY());
    }
}
