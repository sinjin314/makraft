package com.ynov.core;

import org.lwjgl.Version;

public class Launcher {

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        WindowManager window = new WindowManager("Makraft", 1600, 900, false);

        while (!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();

    }

}
