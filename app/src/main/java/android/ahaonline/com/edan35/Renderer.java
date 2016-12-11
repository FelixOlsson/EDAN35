package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.ScreenOverlay;
import android.ahaonline.com.edan35.Objects.SkyBox;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.SkyBoxShaderProgram;
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
    private ShaderTestProgram shaderTestProgram;
    private ShaderLightProgram shaderLightProgram;
    private TextureShaderProgram textureShaderProgram, textureShaderProgram2;
    private FrameShaderProgram frameShaderProgram;
    private ArrayList<Model> asteroids = new ArrayList<>();
    private Model spaceship;
    private ScreenOverlay screenOverlay;
    private Light light;
    private Dialog loadScreen;

    private float xRotation, yRotation;
    private float cameraMovement = 1f;

    private SkyBoxShaderProgram skyboxProgram;
    private SkyBox skybox;
    private int skyboxTexture;

    private int texture, texture2, texture3;
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private final float[] normalViewMatrix = new float[16];
    private final float[] transposdMatrix = new float[16];
    private final float[] inversedMatrix = new float[16];
    private final float[] inversedViewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];
    private final int[] frameBuffer = new int[1];
    private final int[] texColorBuffer = new int[1];
    private final int[] rbo = new int[1];
    private final int[] quadVAO = new int[1];
    private final int[] quadVBO = new int[1];
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
        // Set the background frame color


        textureShaderProgram = new TextureShaderProgram(context);
        textureShaderProgram2 = new TextureShaderProgram(context);
        frameShaderProgram = new FrameShaderProgram(context);
        texture3 = TextureHelper.loadTexture(context, R.drawable.spaceship);
        texture = TextureHelper.loadTexture(context, R.drawable.container2);
        texture2 = TextureHelper.loadTexture(context, R.drawable.container2_specular);
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

        spaceship = new Model();
        spaceship.loadModel(context, R.raw.spaceship);
        spaceship.translate(0,0,0);
        spaceship.rotateX(45f);
        spaceship.scale(10f);
        spaceship.transformMatrix();


        shaderTestProgram = new ShaderTestProgram(context);
        shaderLightProgram = new ShaderLightProgram(context);
        light = new Light();
        screenOverlay = new ScreenOverlay();

        skyboxProgram = new SkyBoxShaderProgram(context);
        skybox = new SkyBox();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int[] {  R.drawable.spacert, R.drawable.spacelf,
                          R.drawable.spaceup, R.drawable.spacedn,
                          R.drawable.spacebk, R.drawable.spaceft});

        setIdentityM(projectionMatrix, 0);
        loadScreen.dismiss();


    }

    @Override
    public void onSurfaceChanged(GL10 unued, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.1f, 150f);

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
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -27, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        //translateM(viewMatrix, 0, 0,0,cameraMovement++);

        travleVector(light);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, light.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

        shaderLightProgram.useProgram();
        light.bindShader(shaderLightProgram);
        shaderLightProgram.setUniforms(modelViewProjectionMatrix);
        light.draw();


        drawAsteroids();
        drawSkybox();



        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);

        frameShaderProgram.useProgram();
        screenOverlay.bindShader(frameShaderProgram);
        frameShaderProgram.setUniforms(texColorBuffer[0]);
        screenOverlay.draw();




    }



    private void drawSkybox() {
        glDepthFunc(GL_LEQUAL);
        setIdentityM(viewMatrix, 0);
        //rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        //rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(projectionMatrix, viewMatrix, skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
        glDepthFunc(GL_LESS);
    }

    private void postProcessingEffect(int width, int height) {
        glGenFramebuffers(frameBuffer.length, frameBuffer, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);

        glGenTextures(1, texColorBuffer, 0);
        glBindTexture(GL_TEXTURE_2D, texColorBuffer[0]);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);


        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texColorBuffer[0], 0);

        glGenRenderbuffers(1, rbo, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, rbo[0]);

        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo[0]);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void drawAsteroids() {

        for(Model asteroid: asteroids) {
            asteroid.rotateX(0.5f);
            asteroid.translate(0 , 0, - 0.01f);

            asteroid.transformMatrix();
            multiplyMM(modelViewMatrix, 0, viewMatrix, 0, asteroid.getModelMatrix(), 0);
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

            textureShaderProgram.useProgram();
            asteroid.bindShader(textureShaderProgram);
            invertM(inversedMatrix, 0, asteroid.getModelMatrix(), 0);
            Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

            invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
            Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);

            textureShaderProgram.setUniforms(modelViewProjectionMatrix, asteroid.getModelMatrix(), texture, light, normalMatrix, new float[]{0, 0, 0f}, normalViewMatrix, texture2);
            asteroid.draw();
        }
        spaceship.rotateX(0.5f);
        spaceship.transformMatrix();
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, spaceship.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        textureShaderProgram2.useProgram();
        spaceship.bindShader(textureShaderProgram2);
        invertM(inversedMatrix, 0, spaceship.getModelMatrix(), 0);
        Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

        invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
        Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);

        textureShaderProgram2.setUniforms(modelViewProjectionMatrix, spaceship.getModelMatrix(), texture3, light, normalMatrix, new float[]{0, 0, 0f}, normalViewMatrix, texture3);
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
        // We'll convert these normalized device coordinates into world-space
        // coordinates. We'll pick a point on the near and far planes, and draw a
        // line between them. To do this transform, we need to first multiply by
        // the inverse matrix, and then we need to undo the perspective divide.
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc =  {normalizedX, normalizedY,  1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(
                nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(
                farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        // Why are we dividing by W? We multiplied our vector by an inverse
        // matrix, so the W value that we end up is actually the *inverse* of
        // what the projection matrix would create. By dividing all 3 components
        // by W, we effectively undo the hardware perspective divide.
        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        // We don't care about the W value anymore, because our points are now
        // in world coordinates.
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
