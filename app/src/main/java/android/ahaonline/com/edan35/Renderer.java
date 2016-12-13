package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.ScreenOverlay;
import android.ahaonline.com.edan35.Objects.SkyBox;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderBlur;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.SkyBoxShaderProgram;
import android.ahaonline.com.edan35.util.Camera;
import android.ahaonline.com.edan35.util.Geometry;
import android.ahaonline.com.edan35.util.TextureHelper;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.app.Dialog;
import android.content.Context;

import static android.opengl.GLES30.*;


import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.ahaonline.com.edan35.util.Geometry.*;
/**
 * Created by felix on 15/11/2016.
 */




public class Renderer implements GLSurfaceView.Renderer {

    private Context context;
    private Camera camera;
    private ShaderLightProgram shaderLightProgram;
    private TextureShaderProgram textureShaderProgram, textureShaderProgram2;
    private ShaderBlur shaderBlur;

    private FrameShaderProgram frameShaderProgram;
    private ArrayList<Model> asteroids = new ArrayList<>();
    private ArrayList<Model> lights = new ArrayList<>();
    private Model spaceship;
    private ScreenOverlay screenOverlay;
    private Light light;
    private Dialog loadScreen;
    private SkyBoxShaderProgram skyboxProgram;
    private SkyBox skybox;
    private int skyboxTexture;

    private int texture, texture2, texture3;
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrixForSkybox = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private final float[] normalViewMatrix = new float[16];
    private final float[] transposdMatrix = new float[16];
    private final float[] inversedMatrix = new float[16];
    private final float[] inversedViewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];
    private final int[] frameBuffer = new int[1];
    private final int[] texColorBuffer = new int[2];
    private final int[] rbo = new int[1];
    private final int pingpongFBO[] = new int[2];
    private final int pingpongColorbuffers[]  = new int[2];

    private int height;
    private int width;


    float deltaTime = 0.0f;	// Time between current frame and last frame
    float lastFrameTime = 0.0f;

    public Renderer(Context context, Dialog loadScreen) {
        this.context = context;
        this.loadScreen = loadScreen;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Objects
        for(int i = 0; i < 10; i++) {
            Model asteroid = new Model();
            asteroid.loadModel(context, R.raw.cube);
            asteroid.scale(randomNumber(1.0f,5.0f));
            float x = randomNumber(-25.0f,25.0f);
            float y = randomNumber(-25.0f,25.0f);
            float z = randomNumber(-25.0f,25.0f);
            asteroid.translate(x,y,z);
            asteroid.transformMatrix();
            asteroids.add(asteroid);
        }

        for(int i = 0; i < 3; i++) {
            Model light = new Model();
            light.loadModel(context, R.raw.light);
            light.lightVariables(new float[]{1f,1f,1f}, new float[]{1f,1f,1f}, new float[]{1f,1f,1f}, 1.0f, 0.007f, 0.0002f);
            light.scale(randomNumber(1.0f,5.0f));
            float x = randomNumber(-10.0f,10.0f);
            float y = randomNumber(-10.0f,10.0f);
            float z = randomNumber(-10.0f,10.0f);
            light.translate(x,y,z);
            light.transformMatrix();
            lights.add(light);
        }

        spaceship = new Model();
        spaceship.loadModel(context, R.raw.spaceship);
        spaceship.translate(0,0,0);
        spaceship.rotateX(45f);
        spaceship.scale(10f);
        spaceship.transformMatrix();
        skybox = new SkyBox();

        //Textures
        textureShaderProgram = new TextureShaderProgram(context);
        textureShaderProgram2 = new TextureShaderProgram(context);
        shaderBlur = new ShaderBlur(context);
        skyboxProgram = new SkyBoxShaderProgram(context);
        shaderLightProgram = new ShaderLightProgram(context);
        frameShaderProgram = new FrameShaderProgram(context);
        texture3 = TextureHelper.loadTexture(context, R.drawable.spaceship);
        texture = TextureHelper.loadTexture(context, R.drawable.container2);
        texture2 = TextureHelper.loadTexture(context, R.drawable.container2_specular);


        //Utility
        camera = new Camera();
        light = new Light();
        screenOverlay = new ScreenOverlay();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int[] {  R.drawable.spacert, R.drawable.spacelf,
                        R.drawable.spaceup, R.drawable.spacedn,
                        R.drawable.spacebk, R.drawable.spaceft});

        setIdentityM(projectionMatrix, 0);
        loadScreen.dismiss();

        Matrix.setLookAtM(camera.getViewMatrix(), 0, 0, 0, -27, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 unued, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.1f, 100f);

        postProcessingEffect(width,height);



    }

    @Override
    public void onDrawFrame(GL10 unused) {
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);




        // Set the camera position (View matrix)

       // rotateM(camera.getViewMatrix(), 0, -45, 1f, 0f, 0f);
        //rotateM(camera.getViewMatrix(), 0, -45, 0f, 1f, 0f);
        //translateM(viewMatrix, 0, 0,0,cameraMovement++);


        travleVector(light);
        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, light.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, camera.getViewMatrix(), 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

        shaderLightProgram.useProgram();
        light.bindShader(shaderLightProgram);
        shaderLightProgram.setUniforms(modelViewProjectionMatrix, lights.get(0));
        light.draw();


        drawAsteroids();
        //drawSpaceship();
        drawLights();
        drawSkybox();
        camera.rotateX(1f);
        camera.transformMatrix();



        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        boolean horizontal = true, first_iteration = true;
        int amount = 10;
        shaderBlur.useProgram();
        for (int i = 0; i < amount; i++)
        {
            glBindFramebuffer(GL_FRAMEBUFFER, pingpongFBO[horizontal ? 1 : 0]);
            glUniform1i(glGetUniformLocation(shaderBlur.getProgram(), "horizontal"), horizontal ? 1 : 0);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, first_iteration ? texColorBuffer[1] : pingpongColorbuffers[horizontal ? 0 : 1]);  // bind texture of other framebuffer (or scene if first iteration)
            glUniform1i(glGetUniformLocation(shaderBlur.getProgram(), "image"), 0);
            screenOverlay.bindShader(shaderBlur);
            screenOverlay.draw();
            horizontal = !horizontal;
            if (first_iteration)
                first_iteration = false;
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);






    }



    private void drawSkybox() {
        glDepthFunc(GL_LEQUAL);
        setIdentityM(viewMatrixForSkybox, 0);
        //rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        //rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrixForSkybox, 0);
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(projectionMatrix, viewMatrixForSkybox, skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
        glDepthFunc(GL_LESS);
    }

    private void postProcessingEffect(int width, int height) {
        glGenFramebuffers(frameBuffer.length, frameBuffer, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);

        glGenTextures(texColorBuffer.length, texColorBuffer, 0);

        for(int i = 0; i < 2; i++) {

            glBindTexture(GL_TEXTURE_2D, texColorBuffer[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glBindTexture(GL_TEXTURE_2D, 0);


            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texColorBuffer[i], 0);
        }

        glGenRenderbuffers(1, rbo, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, rbo[0]);

        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo[0]);
        int[] attachments = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1 };
        glDrawBuffers(2, attachments, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


        glGenFramebuffers(2, pingpongFBO, 0);
        glGenTextures(2, pingpongColorbuffers, 0);
        for (int i = 0; i < 2; i++)
        {
            glBindFramebuffer(GL_FRAMEBUFFER, pingpongFBO[i]);
            glBindTexture(GL_TEXTURE_2D, pingpongColorbuffers[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, width, height, 0, GL_RGB, GL_FLOAT, null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE); // We clamp to the edge as the blur filter would otherwise sample repeated texture values!
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, pingpongColorbuffers[i], 0);


        }




    }

    private void drawAsteroids() {

        for(Model asteroid: asteroids) {
            asteroid.rotateX(0.5f);
            asteroid.translate(0 , 0, - 0.01f);

            asteroid.transformMatrix();
            multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, asteroid.getModelMatrix(), 0);
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

            textureShaderProgram.useProgram();
            asteroid.bindShader(textureShaderProgram);
            invertM(inversedMatrix, 0, asteroid.getModelMatrix(), 0);
            Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

            invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
            Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);

            textureShaderProgram.setUniforms(modelViewProjectionMatrix, asteroid.getModelMatrix(), texture, texture2, camera, lights, 1.0f);
            asteroid.draw();
        }


    }

    private void drawLights() {

        for(Model light : lights) {
            light.rotateY(0.5f);
            light.translate(0 , 0, - 0.01f);

            light.transformMatrix();
            multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, light.getModelMatrix(), 0);
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

            shaderLightProgram.useProgram();
            light.bindShader(shaderLightProgram);
            shaderLightProgram.setUniforms(modelViewProjectionMatrix, light);
            light.draw();
        }


    }

    public void drawSpaceship() {
        spaceship.rotateX(0.5f);
        spaceship.transformMatrix();
        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, spaceship.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        textureShaderProgram2.useProgram();
        spaceship.bindShader(textureShaderProgram2);
        invertM(inversedMatrix, 0, spaceship.getModelMatrix(), 0);
        Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

        invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
        Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);

        textureShaderProgram2.setUniforms(modelViewProjectionMatrix, spaceship.getModelMatrix(), texture3, texture3, camera, lights, 1.0f);
        spaceship.draw();
    }

    private float randomNumber(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private void travleVector(Light light) {

    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        System.out.println("coords: " + deltaX + " " + deltaY);
        Ray ray = convertNormalized2DPointToRay(deltaX, deltaY);
        light.translate(ray.point.x,ray.point.y, 0);
        System.out.println("coords: " + ray.point.x + " " + ray.point.y);
        light.transformMatrix();
    }

    public void handleTouchPress(float deltaX, float deltaY) {
        System.out.println("coords: " + deltaX + " " + deltaY);
        Ray ray = convertNormalized2DPointToRay(deltaX, deltaY);
        light.translate(ray.point.x,ray.point.y, 0);
        light.transformMatrix();

    }

    private Ray convertNormalized2DPointToRay(
            float normalizedX, float normalizedY) {

        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc =  {normalizedX, normalizedY,  1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(
                nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(
                farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Point nearPointRay =
                new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);

        Point farPointRay =
                new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Ray(nearPointRay,
                Geometry.vectorBetween(nearPointRay, farPointRay));
    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

}
