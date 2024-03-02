package com.ynov.core.game_launcher;

import com.ynov.core.*;
import com.ynov.core.entity.Entity;
import com.ynov.core.entity.Model;
import com.ynov.core.entity.Texture;
import com.ynov.core.lighting.DirectionalLight;
import com.ynov.core.utils.Constants;
import com.ynov.core.utils.ILogic;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.net.URL;
import java.util.*;

public class GameLauncher implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Map<Vector3i, Entity> entities;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;

    public GameLauncher() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader =  new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        entities = new HashMap<>();
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        /*float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7,
        };*/


        //Model model = loader.loadModel(vertices, textCoords, indices);
        //Model model = loader.loadOBJModel("/models/Grass_Block_Model.obj");
        //model.setTexture(new Texture(loader.loadTexture("textures/TEX1.png")), 1f);
        List<Model> models = loader.loadOBJModel("/models/tCube.obj");
        for (Model m : models) {
            System.out.println(m);
        }
        //models.getFirst().setTexture(new Texture(loader.loadTexture("textures/TEX1.png")), 1f);

        for (Model model : models) {
            Vector3i v = new Vector3i(Math.round((model.getVertexArr()[7 * 3])+0.5f), Math.round(Math.floor(model.getVertexArr()[7 * 3 + 1])+0.5f), Math.round(Math.floor(model.getVertexArr()[7 * 3 + 2])+0.5f));
            entities.put(v,
                    (new Entity(model, new Vector3f(0,0,0),
                            v,
                            new Vector3f(0, 0, 0), 1f, new Vector3f(1, 1, 1)
                            )
                    )
            );
        }

        //model.setTexture(new Texture(loader.loadTexture("textures/TEX1.png")), 1f);

        //entities.add(new Entity(model, new Vector3f(0, 0, -2f), new Vector3f(0,0,0), 1));

//        List<Model> m = loader.loadOBJModel("/models/sCube.obj");
//        //m.getFirst().setTexture(new Texture(loader.loadTexture("textures/TEX1.png")), 1f);
//        for (int i = -50; i < 50; i++) {
//            for (int j = -50; j < 50; j++) {
//                entities.put(new Vector3f(i, 3f, j),
//                        (new Entity(m.getFirst(), new Vector3f(i, 3f, j), new Vector3f(i, 2f, j),
//                                new Vector3f(0, 0, 0), 1f, new Vector3f(1, 1, 1))
//                        ));
//            }
//        }

        entities.remove(new Vector3i(1, 2,-1));

        for (Entity e : entities.values()) {
            System.out.println(e.getCoordinate());
        }


//        Model model2 = loader.loadOBJModel("/models/tree.obj");
//        model2.setTexture(new Texture(loader.loadTexture("textures/TEX1.png")), 1f);
//        entities.add(new Entity(model2, new Vector3f(5, 1.5f, 5), new Vector3f(0,0,0), 1f, new Vector3f(1, 1, 1)));

        float lightIntensity = 0.0f;
        Vector3f lightPosition = new Vector3f(-1, -10, 0);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);

    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);

        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraInc.y = 1;
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Constants.CAMERA_MOVE_SPEED, cameraInc.y * Constants.CAMERA_MOVE_SPEED, cameraInc.z * Constants.CAMERA_MOVE_SPEED);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVec();
            camera.moveRotation(rotVec.x * Constants.MOUSE_SENSITIVITY, rotVec.y * Constants.MOUSE_SENSITIVITY, 0);
        }

        for (Entity entity : entities.values()) {
            renderer.processEntity(entity);
        }



        lightAngle += 0.5f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - ((float) Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor, 0.9f);
            directionalLight.getColour().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x = 1;
            directionalLight.getColour().y = 1;
            directionalLight.getColour().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render() {
//        if (window.isResize()) {
//            GL11.glViewport(0,0, window.getWidth(), window.getHeight());
//            window.setResize(true);
//        }

        //window.setClearColour(0.0f, 0.0f, 0.0f,0.0f);
        renderer.render(camera, directionalLight);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

}
