package engine;

//import engine.input.KeyListener;
//import engine.input.MouseListener;
//import imgui.type.ImBoolean;
//import scenes.Scene;
//import imgui.*;
//import imgui.callback.ImStrConsumer;
//import imgui.callback.ImStrSupplier;
//import imgui.flag.*;
//import imgui.gl3.ImGuiImplGl3;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UncheckedIOException;
//import java.util.Objects;
//
//import static org.lwjgl.glfw.GLFW.*;
//
//
//public class ImGuiLayer {
//    private long glfwWindow; // pointer to the current GLFW window
//
//    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
//
//    public ImGuiLayer(long glfwWindow){
//        this.glfwWindow = glfwWindow;
//    }
//
//
//    // LWJGL3 renderer (SHOULD be initialized)
//    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
//    private String glslVersion = null; // We can initialize our renderer with different versions of the GLSL
//
//
//    private String filePath;
//    private int fontSize;
//    private boolean shouldUpdateFont = false;
//    public void ChangeFont(String fontFile, int size){
//        filePath = fontFile;
//        fontSize = size;
//        shouldUpdateFont = true;
//    }
//
//
//    private void destroyImgui(){
//        imGuiGl3.dispose();
//        ImGui.destroyContext();
//    }
//
//
//
//    // Initialize Dear ImGui.
//    public void setupImGui() {
//        // IMPORTANT!!
//        // This line is critical for Dear ImGui to work.
//        ImGui.createContext();
//
//        // ------------------------------------------------------------
//        // Initialize ImGuiIO config
//        final ImGuiIO io = ImGui.getIO();
//
//        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
//        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
//        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
//        io.setConfigViewportsNoTaskBarIcon(true);
//
//        // ------------------------------------------------------------
//        // Fonts configuration
//        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt
//
//        final ImFontAtlas fontAtlas = io.getFonts();
//        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed
//
//        // Glyphs could be added per-font as well as per config used globally like here
//        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
//
//
//        fontConfig.setPixelSnapH(true);
//        fontAtlas.addFontFromFileTTF("assets/fonts/ComicNeue-Bold.ttf", 24, fontConfig);
//        fontConfig.setMergeMode(true);
//        fontAtlas.addFontFromFileTTF("assets/fonts/ComicNeue-Light.ttf", 24, fontConfig);
//        fontAtlas.addFontFromFileTTF("assets/fonts/ComicNeue-Regular.ttf", 24, fontConfig);
//
//        fontConfig.destroy(); // After all fonts were added we don't need this config more
//
//
//
//
//
//        // When viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look identical to regular ones.
//        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            final ImGuiStyle style = ImGui.getStyle();
//            style.setWindowRounding(0.0f);
//            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
//        }
//
//        // ------------------------------------------------------------
//        // Mouse cursors mapping
//        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
//        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//
//        final int[] keyMap = new int[ImGuiKey.COUNT];
//        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
//        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
//        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
//        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
//        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
//        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
//        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
//        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
//        keyMap[ImGuiKey.End] = GLFW_KEY_END;
//        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
//        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
//        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
//        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
//        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
//        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
//        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
//        keyMap[ImGuiKey.A] = GLFW_KEY_A;
//        keyMap[ImGuiKey.C] = GLFW_KEY_C;
//        keyMap[ImGuiKey.V] = GLFW_KEY_V;
//        keyMap[ImGuiKey.X] = GLFW_KEY_X;
//        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
//        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
//        io.setKeyMap(keyMap);
//
//
//        //ImGui.pushStyleColor();
//        ImGuiStyle style = ImGui.getStyle();
//        style.setFrameRounding(2);
//        style.setWindowBorderSize(0);
//        style.setChildBorderSize(0);
//
//        style.setWindowRounding(0);
//        style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
//        style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);
//        style.setColor(ImGuiCol.WindowBg, 0.11f, 0.11f, 0.11f, 0.94f);
//        style.setColor(ImGuiCol.ChildBg, 0.00f, 0.00f, 0.00f, 0.00f);
//        style.setColor(ImGuiCol.PopupBg, 0.08f, 0.08f, 0.08f, 0.94f);
//        style.setColor(ImGuiCol.Border, 0.43f, 0.43f, 0.50f, 0.50f);
//        style.setColor(ImGuiCol.BorderShadow, 0.00f, 0.00f, 0.00f, 0.00f);
//        style.setColor(ImGuiCol.FrameBg, 0.34f, 0.34f, 0.34f, 0.54f);
//        style.setColor(ImGuiCol.FrameBgHovered, 0.26f, 0.98f, 0.31f, 0.40f);
//        style.setColor(ImGuiCol.FrameBgActive, 0.26f, 0.98f, 0.31f, 0.67f);
//        style.setColor(ImGuiCol.TitleBg, 0.00f, 0.27f, 0.02f, 1.00f);
//        style.setColor(ImGuiCol.TitleBgActive, 0.16f, 0.48f, 0.18f, 1.00f);
//        style.setColor(ImGuiCol.TitleBgCollapsed, 0.12f, 0.69f, 0.16f, 0.86f);
//        style.setColor(ImGuiCol.MenuBarBg, 0.14f, 0.14f, 0.14f, 1.00f);
//        style.setColor(ImGuiCol.ScrollbarBg, 0.02f, 0.02f, 0.02f, 0.53f);
//        style.setColor(ImGuiCol.ScrollbarGrab, 0.31f, 0.31f, 0.31f, 1.00f);
//        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.41f, 0.41f, 0.41f, 1.00f);
//        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.51f, 0.51f, 0.51f, 1.00f);
//        style.setColor(ImGuiCol.CheckMark, 0.00f, 1.00f, 0.00f, 1.00f);
//        style.setColor(ImGuiCol.SliderGrab, 0.11f, 0.46f, 0.13f, 1.00f);
//        style.setColor(ImGuiCol.SliderGrabActive, 0.25f, 0.46f, 0.26f, 1.00f);
//        style.setColor(ImGuiCol.Button, 0.47f, 0.98f, 0.51f, 0.40f);
//        style.setColor(ImGuiCol.ButtonHovered, 0.47f, 0.98f, 0.51f, 0.60f);
//        style.setColor(ImGuiCol.ButtonActive, 0.47f, 0.98f, 0.51f, 1.00f);
//        style.setColor(ImGuiCol.Header, 0.64f, 0.64f, 0.64f, 0.31f);
//        style.setColor(ImGuiCol.HeaderHovered, 0.18f, 0.67f, 0.21f, 0.58f);
//        style.setColor(ImGuiCol.HeaderActive, 0.18f, 0.69f, 0.22f, 1.00f);
//        style.setColor(ImGuiCol.Separator, 0.34f, 0.34f, 0.34f, 0.50f);
//        style.setColor(ImGuiCol.SeparatorHovered, 0.05f, 0.40f, 0.08f, 0.78f);
//        style.setColor(ImGuiCol.SeparatorActive, 0.06f, 0.47f, 0.09f, 1.00f);
//        style.setColor(ImGuiCol.ResizeGrip, 0.26f, 0.98f, 0.31f, 0.00f);
//        style.setColor(ImGuiCol.ResizeGripHovered, 1.00f, 1.00f, 1.00f, 0.67f);
//        style.setColor(ImGuiCol.ResizeGripActive, 0.57f, 0.57f, 0.57f, 0.95f);
//        style.setColor(ImGuiCol.Tab, 0.18f, 0.58f, 0.21f, 0.86f);
//        style.setColor(ImGuiCol.TabHovered, 0.26f, 0.98f, 0.31f, 0.80f);
//        style.setColor(ImGuiCol.TabActive, 0.20f, 0.68f, 0.23f, 1.00f);
//        style.setColor(ImGuiCol.TabUnfocused, 0.07f, 0.15f, 0.08f, 0.97f);
//        style.setColor(ImGuiCol.TabUnfocusedActive, 0.14f, 0.42f, 0.16f, 1.00f);
//        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.98f, 0.31f, 0.70f);
//        style.setColor(ImGuiCol.DockingEmptyBg, 0.20f, 0.20f, 0.20f, 1.00f);
//        style.setColor(ImGuiCol.PlotLines, 0.61f, 0.61f, 0.61f, 1.00f);
//        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
//        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
//        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
//        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.98f, 0.31f, 0.35f);
//        style.setColor(ImGuiCol.DragDropTarget, 1.00f, 1.00f, 0.00f, 0.90f);
//        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.98f, 0.31f, 1.00f);
//        style.setColor(ImGuiCol.NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f);
//        style.setColor(ImGuiCol.NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f);
//        style.setColor(ImGuiCol.ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f);
//
//
//
//        // ------------------------------------------------------------
//        // GLFW callbacks to handle user input
//
//        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
//            if (action == GLFW_PRESS) {
//                io.setKeysDown(key, true);
//            } else if (action == GLFW_RELEASE) {
//                io.setKeysDown(key, false);
//            }
//
//            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
//            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
//            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
//            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
//
//            if (!io.getWantCaptureKeyboard()){
//                KeyListener.keyCallback(w,key,scancode,action,mods);
//            }
//
//
//        });
//
//        glfwSetCharCallback(glfwWindow, (w, c) -> {
//            if (c != GLFW_KEY_DELETE) {
//                io.addInputCharacter(c);
//            }
//        });
//
//        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
//            final boolean[] mouseDown = new boolean[5];
//
//            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
//            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
//            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
//            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
//            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;
//
//            io.setMouseDown(mouseDown);
//
//            if (!io.getWantCaptureMouse() && mouseDown[1]) {
//                ImGui.setWindowFocus(null);
//            }
//
//            if(!io.getWantCaptureMouse()){
//                System.out.println("Continue to scene");
//                MouseListener.mouseButtonCallback(w, button, action, mods );
//            }
//        });
//
//        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
//            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
//            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
//        });
//
//        io.setSetClipboardTextFn(new ImStrConsumer() {
//            @Override
//            public void accept(final String s) {
//                glfwSetClipboardString(glfwWindow, s);
//            }
//        });
//
//        io.setGetClipboardTextFn(new ImStrSupplier() {
//            @Override
//            public String get() {
//                final String clipboardString = glfwGetClipboardString(glfwWindow);
//                if (clipboardString != null) {
//                    return clipboardString;
//                } else {
//                    return "";
//                }
//            }
//        });
//
//        imGuiGl3.init(glslVersion);
//    }
//
//    // Main application loop
//    public void update(float dt, Scene scene) {
//
//        startFrame(dt);
//        setupDockspace();
//        ImGui.newFrame();
//        scene.sceneImgui();
//        ImGui.showDemoWindow();
//
//        ImGui.end();
//        ImGui.render();
//        endFrame();
//
//
//    }
//
//    private void setupDockspace() {
//        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
//
//        ImGui.setWindowPos(0.0f,0.0f, ImGuiCond.Always);
//        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
//        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
//        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
//        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove
//                | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
//
//        ImGui.begin("Dockspace demo", new ImBoolean(true), windowFlags);
//        ImGui.popStyleVar(2);
//
//        //Dockspace
//
//    }
//
//
//    private void startFrame(float deltaTime) {
//        float[] winWidth = {Window.getWidth()};
//        float[] winHeight = {Window.getHeight()};
//        double[] mousePosX = {0};
//        double[] mousePosY = {0};
//        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);
//
//        // We SHOULD call those methods to update Dear ImGui state for the current frame
//        final ImGuiIO io = ImGui.getIO();
//        io.setDisplaySize(winWidth[0], winHeight[0]);
//        io.setDisplayFramebufferScale(1f, 1f);
//        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
//        io.setDeltaTime(deltaTime);
//
//        // Update the mouse cursor
//        final int imguiCursor = ImGui.getMouseCursor();
//        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
//        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//    }
//
//    private void endFrame() {
//        imGuiGl3.renderDrawData(ImGui.getDrawData());
////
////        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
////            final long backupWindowPtr = glfwGetCurrentContext();
////            ImGui.updatePlatformWindows();
////            ImGui.renderPlatformWindowsDefault();
////            glfwMakeContextCurrent(backupWindowPtr);
////        }
//    }
//
//
//    private byte[] loadFromResources(final String fileName) {
//        try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName));
//             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
//
//            final byte[] data = new byte[16384];
//
//            int nRead;
//            while ((nRead = is.read(data, 0, data.length)) != -1) {
//                buffer.write(data, 0, nRead);
//            }
//
//            return buffer.toByteArray();
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
//    }
//}

import engine.editor.GameViewWindow;
import engine.input.KeyListener;
import engine.input.MouseListener;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {

    private long glfwWindow;

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);


        //STYLE
        ImGuiStyle style = ImGui.getStyle();
        style.setFrameRounding(2);
        style.setWindowBorderSize(0);
        style.setChildBorderSize(0);

        style.setWindowRounding(0);
        style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);
        style.setColor(ImGuiCol.WindowBg, 0.11f, 0.11f, 0.11f, 0.94f);
        style.setColor(ImGuiCol.ChildBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.PopupBg, 0.08f, 0.08f, 0.08f, 0.94f);
        style.setColor(ImGuiCol.Border, 0.43f, 0.43f, 0.50f, 0.50f);
        style.setColor(ImGuiCol.BorderShadow, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.FrameBg, 0.34f, 0.34f, 0.34f, 0.54f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.26f, 0.98f, 0.31f, 0.40f);
        style.setColor(ImGuiCol.FrameBgActive, 0.26f, 0.98f, 0.31f, 0.67f);
        style.setColor(ImGuiCol.TitleBg, 0.00f, 0.27f, 0.02f, 1.00f);
        style.setColor(ImGuiCol.TitleBgActive, 0.16f, 0.48f, 0.18f, 1.00f);
        style.setColor(ImGuiCol.TitleBgCollapsed, 0.12f, 0.69f, 0.16f, 0.86f);
        style.setColor(ImGuiCol.MenuBarBg, 0.14f, 0.14f, 0.14f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarBg, 0.02f, 0.02f, 0.02f, 0.53f);
        style.setColor(ImGuiCol.ScrollbarGrab, 0.31f, 0.31f, 0.31f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.41f, 0.41f, 0.41f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.51f, 0.51f, 0.51f, 1.00f);
        style.setColor(ImGuiCol.CheckMark, 0.00f, 1.00f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.SliderGrab, 0.11f, 0.46f, 0.13f, 1.00f);
        style.setColor(ImGuiCol.SliderGrabActive, 0.25f, 0.46f, 0.26f, 1.00f);
        style.setColor(ImGuiCol.Button, 0.47f, 0.98f, 0.51f, 0.40f);
        style.setColor(ImGuiCol.ButtonHovered, 0.47f, 0.98f, 0.51f, 0.60f);
        style.setColor(ImGuiCol.ButtonActive, 0.47f, 0.98f, 0.51f, 1.00f);
        style.setColor(ImGuiCol.Header, 0.64f, 0.64f, 0.64f, 0.31f);
        style.setColor(ImGuiCol.HeaderHovered, 0.18f, 0.67f, 0.21f, 0.58f);
        style.setColor(ImGuiCol.HeaderActive, 0.18f, 0.69f, 0.22f, 1.00f);
        style.setColor(ImGuiCol.Separator, 0.34f, 0.34f, 0.34f, 0.50f);
        style.setColor(ImGuiCol.SeparatorHovered, 0.05f, 0.40f, 0.08f, 0.78f);
        style.setColor(ImGuiCol.SeparatorActive, 0.06f, 0.47f, 0.09f, 1.00f);
        style.setColor(ImGuiCol.ResizeGrip, 0.26f, 0.98f, 0.31f, 0.00f);
        style.setColor(ImGuiCol.ResizeGripHovered, 1.00f, 1.00f, 1.00f, 0.67f);
        style.setColor(ImGuiCol.ResizeGripActive, 0.57f, 0.57f, 0.57f, 0.95f);
        style.setColor(ImGuiCol.Tab, 0.18f, 0.58f, 0.21f, 0.86f);
        style.setColor(ImGuiCol.TabHovered, 0.26f, 0.98f, 0.31f, 0.80f);
        style.setColor(ImGuiCol.TabActive, 0.20f, 0.68f, 0.23f, 1.00f);
        style.setColor(ImGuiCol.TabUnfocused, 0.07f, 0.15f, 0.08f, 0.97f);
        style.setColor(ImGuiCol.TabUnfocusedActive, 0.14f, 0.42f, 0.16f, 1.00f);
        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.98f, 0.31f, 0.70f);
        style.setColor(ImGuiCol.DockingEmptyBg, 0.20f, 0.20f, 0.20f, 1.00f);
        style.setColor(ImGuiCol.PlotLines, 0.61f, 0.61f, 0.61f, 1.00f);
        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.98f, 0.31f, 0.35f);
        style.setColor(ImGuiCol.DragDropTarget, 1.00f, 1.00f, 0.00f, 0.90f);
        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.98f, 0.31f, 1.00f);
        style.setColor(ImGuiCol.NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f);
        style.setColor(ImGuiCol.NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f);
        style.setColor(ImGuiCol.ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f);

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/ComicNeue-Regular.ttf", 20, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene currentScene) {
        startFrame(dt);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame();
        setupDockspace();
        currentScene.sceneImgui();
        ImGui.showDemoWindow();
        GameViewWindow.imgui();
        if (Window.get().isShowConsole()){
          Window.get().setShowConsole(Window.getLogHelper().imgui());
        }
        ImGui.end();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        // Get window properties and mouse position
        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }
}