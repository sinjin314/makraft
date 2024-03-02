package com.ynov.core.game_launcher;

import com.ynov.core.EngineManager;
import com.ynov.core.WindowManager;
import com.ynov.core.utils.Constants;
import org.lwjgl.Version;

public class Launcher {

    private static WindowManager window;
    private static GameLauncher game;

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        window = new WindowManager(Constants.TITLE, 1600, 900, false);
        game = new GameLauncher();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static GameLauncher getGame() {
        return game;
    }
}
