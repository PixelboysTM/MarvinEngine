package engine;

import engine.renderer.DebugDraw;
import engine.renderer.FrameBuffer;
import engine.util.LogHelper;
import engine.util.Settings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import scenes.Scene;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.TestNodeScene;
import engine.input.KeyListener;
import engine.input.MouseListener;
import engine.util.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private FrameBuffer frameBuffer;
    private LogHelper logHelper;
    private boolean showConsole = true;

    public float r,g,b,a;

    private static Window window = null;

    public ArrayList<Scene> sceneStack;

    public void setSingle(Scene scene){
        sceneStack.clear();
        pushScene(scene);
    }

    public void pushScene(Scene newScene){
        sceneStack.add(0, newScene);
        //currentScene.load();
        newScene.init();
        newScene.start();
    }
    public void popScene(){
        sceneStack.remove(0);
        if (sceneStack.size() == 0){
            sceneStack.add(0, new LevelEditorScene());
        }
        sceneStack.get(0).init();
        sceneStack.get(0).start();
    }

    private Window(){
        this.width = Settings.ViewportWidth;
        this.height = Settings.ViewportHeight;
        this.title = Settings.WindowName;
        g = 1;
        b = 1;
        r = 1;
        a = 1;
    }

    public static Window get(){
        if(window == null){
            window = new Window();
        }
        return window;
    }

    public void run(){
        //System.out.println("Hello LWJFL " + Version.getVersion())
        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free
        glfwTerminate();
        glfwSetErrorCallback(null).free();


    }

    private void init() {
        //Error Handling
        GLFWErrorCallback.createPrint(System.err).set();

        //Init glfw
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initalize GLFW.");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width,this.height, this.title, NULL, NULL);
        if (glfwWindow == 0){
            throw new IllegalStateException("Failed to create Window!");
        }

        //Bind Callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (window1, width1, height1) -> {
            Window.setWidth(width1);
            Window.setHeigth(height1);
        });

        //Make OpneGl context
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(0); // V-Sync = 1

        //Make the window visible
        glfwShowWindow(glfwWindow);

        //Poll openGL functions
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        this.frameBuffer = new FrameBuffer(Settings.ViewportWidth, Settings.ViewportHeight); //TODO: Query screen size
        glViewport(0,0,Settings.ViewportWidth, Settings.ViewportHeight);

        //Icon setting
        ByteBuffer imageData;
        int width, height;
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        imageData = stbi_load("assets/images/icon.png", w,h,comp, 4);
        if (imageData == null){
            assert false : "Could not load icon image";
        }
        width = w.get();
        height = h.get();
        GLFWImage iconImage = GLFWImage.malloc(); GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
        iconImage.set(width, height, imageData);
        iconBuffer.put(0, iconImage);

        glfwSetWindowIcon(glfwWindow, iconBuffer);

        logHelper = new LogHelper();
        sceneStack = new ArrayList<>();
        pushScene(new TestNodeScene()); // TODO: StartupScene
    }

    private static void setHeigth(int height1) {
        get().height = height1;
    }

    private static void setWidth(int width1) {
        get().width = width1;
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;



        while (!glfwWindowShouldClose(glfwWindow) && !KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            glfwPollEvents();

            DebugDraw.beginFrame();

            this.frameBuffer.bind();
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSetWindowTitle(glfwWindow,title + " FPS: " + (int)(1/dt) );

            //Render
            if (dt >= 0){
                DebugDraw.draw();
                sceneStack.get(0).Update(dt);
            }
            this.frameBuffer.unbind();

            sceneStack.get(0).lateUpdate(dt);

            this.imGuiLayer.update(dt, sceneStack.get(0));
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        //currentScene.saveExit(); TODO: Add autosave on close;
    }


    public static Scene getScene(){
        return get().sceneStack.get(0);
    }

    public static int getWidth(){
        return get().width;
    }
    public static int getHeight(){
        return get().height;
    }
    public static long windowHandle(){
        return get().glfwWindow;
    }
    public static ImGuiLayer getImGuiLayer(){
        return get().imGuiLayer;
    }

    public static FrameBuffer getFrameBuffer() {
        return get().frameBuffer;
    }
    public static float getTargetAspectRatio(){
        return Settings.TargetAspectRatio;//16.0f/9.0f;
    }
    public static LogHelper getLogHelper(){
       return get().logHelper;
    }

    public boolean isShowConsole() {
        return showConsole;
    }

    public void setShowConsole(boolean showConsole) {
        this.showConsole = showConsole;
    }
}
