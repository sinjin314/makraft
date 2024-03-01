package com.ynov.core;

import com.ynov.core.entity.Entity;
import com.ynov.core.entity.Model;
import com.ynov.core.game_launcher.GameLauncher;
import com.ynov.core.game_launcher.Launcher;
import com.ynov.core.utils.Utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.stb.STBImage.stbi_load;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    private int bockCount = 0;

    public void  loadMTL(String fileName) {
        return;
    }

    public List<Model> loadOBJModel(String fileName) {
        List<String> lines = Utils.readAllLines(fileName);

        List<Model> models = new ArrayList<>();

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();
        Vector3f colour = new Vector3f();
        bockCount = -1;

        for (String line : lines) {
            String[] tokens = line.split(" ");
            switch (tokens[0]) {
                case "#":
                    break;
                case "usemtl":
                    // colour = MTLLoader(tokens[1]);
                    break;
                case "v":
                    // vertices
                    Vector3f verticesVec = new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    // vertex textures
                    Vector2f textureVec = new Vector2f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2])
                    );
                    textures.add(textureVec);
                    break;
                case "vn":
                    // vertex normals
                    Vector3f normalsVec = new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                    );
                    normals.add(normalsVec);
                    break;
                case "f":
                    // faces
                    processFace(tokens[1], faces);
                    processFace(tokens[2], faces);
                    processFace(tokens[3], faces);
                    break;
                case "s":
                    System.err.println("ERROR : S OBJ LOAD : setSmoothShadingEnabled");
                    //model.setSmoothShadingEnabled(!line.contains("off"));
                    break;
                case "o":
                    if (bockCount >= 0) {
                        models.add(createModel(vertices, normals, textures, faces, colour));
                        vertices = new ArrayList<>();
                        normals = new ArrayList<>();
                        textures = new ArrayList<>();
                        faces = new ArrayList<>();
                        colour = new Vector3f();
                        bockCount++;
                    } else {
                        bockCount++;
                    }
                    break;
                default:
                    System.err.println("[OBJ] Unknown Line: " + line);
            }
        }
        models.add(createModel(vertices, normals, textures, faces, colour));

        /*int[] indicesEntity = new int[12 * 3];
        float[] verticesEntity = new float[8 * 3];
        float[] normalEntity = new float[8 * 3];
        int j = 0;
        List<Model> models = new ArrayList<>();

        int vertexID = 0;
        int normalID = 0;

        for (int l = 0; l < indicesArr.length  ; l++) {

            indicesEntity[j] = indicesArr[l];
            if (j < (8 * 3) )
                verticesEntity[j] = verticesArr[vertexID + j];
            if (j < (8 * 3) )
                normalEntity[j] = normalArr[normalID + j];

            j++;

            if ((l+1) % (12 * 3) == 0) {
                System.out.println("1111111111111111111111111111111111111111111111111");
                models.add(loadModel(verticesEntity, texCoordArr, normalEntity, indicesEntity));
                indicesEntity = new int[12 * 3];
                verticesEntity = new float[8 * 3];
                normalEntity = new float[8 * 3];
                j = 0;
                normalID += j;
                vertexID += j;
            }

        }*/
        //models.add(loadModel(verticesArr, texCoordArr, normalArr, indicesArr));

        return models;
        
    }

    private Model createModel(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textures, List<Vector3i> faces, Vector3f colour) {

        List<Integer> indices = new ArrayList<>();
        float[] verticesArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            verticesArr[i * 3] = pos.x;
            verticesArr[i * 3 + 1] = pos.y;
            verticesArr[i * 3 + 2] = pos.z;
            i++;
        }

        float[] texCoordArr = new float[vertices.size() * 2];
        float[] normalArr = new float[vertices.size() * 3];

        for (Vector3i face : faces) {
            processVertex(face.x - bockCount*8, face.y - bockCount*8, face.z - bockCount*6, textures, normals, indices/*, texCoordArr*/, normalArr);
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticesArr/*, texCoordArr*/, normalArr, indicesArr);
    }

    private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList, 
                                      List<Vector3f> normalList, List<Integer> indicesList, 
                                      /*float[] textCoordArr,*/ float[] normalArr){
        indicesList.add(pos);
//        if (texCoord >= 0) {
//            Vector2f texCoordVec = texCoordList.get(texCoord);
//            textCoordArr[pos * 2] = texCoordVec.x;
//            textCoordArr[pos * 2 + 1] = 1 - texCoordVec.y;
//        }

        if (normal >= 0) {
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos * 3] = normalVec.x;
            normalArr[pos * 3 + 1] = normalVec.y;
            normalArr[pos * 3 + 2] = normalVec.z;
        }
    }

    private static void processFace(String token, List<Vector3i> faces) {
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(lineToken[0]) - 1;
        if (length > 1) {
            String textCoord = lineToken[1];
            coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
            if (length > 2) {
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }
        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }


    public Model loadModel(float[] vertices/*, float[] textureCoords*/, float[] normals, int[] indices) {
//        for (float g : vertices) {
//            System.out.println(g);
//        }
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttribList(0, 3, vertices);
//        storeDataInAttribList(1, 2, textureCoords);
        storeDataInAttribList(2, 3, normals);
        unbind();
        return new Model(id, indices.length, null, vertices);
    }

    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);

            if(buffer == null)
                throw new Exception("Image File " + filename + " not loaded " + STBImage.stbi_failure_reason());
            
            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {

        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private void storeDataInAttribList (int attribNo, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }


}
