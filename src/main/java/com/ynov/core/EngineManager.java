package com.ynov.core;

import com.ynov.core.game_launcher.Launcher;
import com.ynov.core.utils.Constants;
import com.ynov.core.utils.ILogic;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;


public class EngineManager {

    public static final long NANO_SECOND = 1_000_000_000L;
    public static final float FRAMERATE = 1_000f;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANO_SECOND;
            frameCounter += passedTime;

            input();

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose())
                    stop();

                if (frameCounter >= NANO_SECOND) {
                    setFps(frames);
                    window.setTitle( Constants.TITLE + " / FPS : " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }

            }

            if (render) {
                update(frametime);
                render();
                frames++;
            }

        }
        cleanup();
    }

    private void stop() {
        if (!isRunning)
            return;
        isRunning = false;
    }

    private void input() {
        mouseInput.input();
        gameLogic.input();
    }

    private void render() {
        gameLogic.render();
        window.update();
    }

    private void update(float interval) {
        gameLogic.update(interval, mouseInput);

    }

    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
