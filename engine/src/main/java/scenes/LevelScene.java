package scenes;

import scenes.Scene;
import engine.Window;

public class LevelScene extends Scene {
    public LevelScene(){
        System.out.println("Loaded Level");
    }

    @Override
    public void Update(float dt) {
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;

    }
}
