package com.ynov.core.utils;

import com.ynov.core.MouseInput;

public interface ILogic {

    void init() throws Exception;
    void input();
    void update(float interval, MouseInput mouseInput);
    void render();
    void cleanup();

}
