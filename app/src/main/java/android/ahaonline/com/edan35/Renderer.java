package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.ParticleShooter;
import android.ahaonline.com.edan35.Objects.ParticleSystem;
import android.ahaonline.com.edan35.Objects.ScreenOverlay;
import android.ahaonline.com.edan35.Objects.SkyBox;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;
import android.ahaonline.com.edan35.programs.ParticleShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.SkyBoxShaderProgram;
import android.ahaonline.com.edan35.util.Camera;
import android.ahaonline.com.edan35.util.Geometry;
import android.ahaonline.com.edan35.util.TextureHelper;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.app.Dialog;
import android.content.Context;
import static android.ahaonline.com.edan35.util.Geometry.*;

import static android.opengl.GLES30.*;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;



/**
 * Created by felix on 15/11/2016.
 */


public class Renderer implements GLSurfaceView.Renderer {


    private Context context;
    private Camera camera;
    private ShaderLightProgram shaderLightProgram;
    private TextureShaderProgram textureShaderProgram, textureShaderProgram2;
    private FrameShaderProgram frameShaderProgram;
    private ShaderTestProgram shaderTestProgram;
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
    private int textureParticle;
    private final float[] modelMatrix = new float[16];
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
    private final float[] modelMatrixForFire = new float[16];
    private final int[] frameBuffer = new int[1];
    private final int[] texColorBuffer = new int[2];
    private final int[] rbo = new int[1];
    private long globalStartTime;

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;

    private boolean spaceshipPressed = false;
    private ArrayList<List> ar;

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
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        final float angleVarianceInDegrees = 25f;
        final float speedVariance = 5f;
        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(250);
        globalStartTime = System.nanoTime();
        redParticleShooter = new ParticleShooter(new float[]{0f, 0f, 0f}, new float[]{0f, 0f, -0.5f}, Color.rgb(255, 50, 5), angleVarianceInDegrees, speedVariance);

        // Objects
        for(int i = 0; i < 10; i++) {
            Model asteroid = new Model();
            asteroid.loadModel(context, R.raw.asteroid1);
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
            light.lightVariables(new float[]{randomNumber(0.3f,10.5f),randomNumber(0.3f,0.5f),randomNumber(0.3f,0.5f)}
                    , new float[]{randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f)},
                    new float[]{randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f)}, 1.0f, 0.007f, 0.0002f);
            light.scale(randomNumber(1.0f,5.0f));
            float x = randomNumber(-10.0f,10.0f);
            float y = randomNumber(-10.0f,10.0f);
            float z = randomNumber(-10.0f,10.0f);
            light.translate(x,y,z);
            light.transformMatrix();
            lights.add(light);
        }

        spaceship = new Model();
        spaceship.loadModel(context, R.raw.spaceship1);
        spaceship.translate(0,0,0);
        //spaceship.scale(2f);
        spaceship.transformMatrix();
        skybox = new SkyBox();

        setIdentityM(modelMatrixForFire, 0);
        translateM(modelMatrixForFire, 0, spaceship.getX(), spaceship.getY() - 0.4f, -1);

        //Textures
        textureShaderProgram = new TextureShaderProgram(context);
        textureShaderProgram2 = new TextureShaderProgram(context);
        shaderTestProgram = new ShaderTestProgram(context);
        skyboxProgram = new SkyBoxShaderProgram(context);
        shaderLightProgram = new ShaderLightProgram(context);
        frameShaderProgram = new FrameShaderProgram(context);
        texture3 = TextureHelper.loadTexture(context, R.drawable.metal);
        texture = TextureHelper.loadTexture(context, R.drawable.seamlessstonetexture);
        texture2 = TextureHelper.loadTexture(context, R.drawable.container2_specular);
        textureParticle = TextureHelper.loadTexture(context, R.drawable.particle_texture);


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
        this.width = width;
        this.height = height;
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


        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, light.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, camera.getViewMatrix(), 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

        shaderLightProgram.useProgram();
        light.bindShader(shaderLightProgram);
        shaderLightProgram.setUniforms(modelViewProjectionMatrix, lights.get(0));

        drawAsteroids();
        drawSpaceship();
        drawLights();
        drawSkybox();
        drawFire();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);

        frameShaderProgram.useProgram();
        screenOverlay.bindShader(frameShaderProgram);
        frameShaderProgram.setUniforms(texColorBuffer[0], texColorBuffer[1]);
        screenOverlay.draw();
    }

    private void drawSkybox() {
        glDepthFunc(GL_LEQUAL);
        setIdentityM(viewMatrixForSkybox, 0);
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
        glGenTextures(2, texColorBuffer, 0);

        for(int i = 0; i < 2; i++) {
            glBindTexture(GL_TEXTURE_2D, texColorBuffer[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texColorBuffer[i], 0);
        }

        glGenRenderbuffers(1, rbo, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, rbo[0]);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo[0]);
        int[] attachments = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1 };
        glDrawBuffers(2, attachments, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);


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

        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, spaceship.getModelMatrix(), 0);
        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, spaceship.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        shaderTestProgram.useProgram();
        spaceship.bindShader(shaderTestProgram);
        invertM(inversedMatrix, 0, spaceship.getModelMatrix(), 0);
        Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

        invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
        Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);
        float[] color = new float[] {0.329412f, 0.329412f, 0.329412f};

        shaderTestProgram.setUniforms(modelViewProjectionMatrix, texture3);
        spaceship.draw();
    }

    private float randomNumber(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }


    public void handleTouchDrag(float normalizedX, float normalizedY) {
        Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

        Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, -1));

        Point touchedPoint = Geometry.intersectionPoint(ray, plane);
        spaceship.setIdentitiy();
        spaceship.translate(touchedPoint.x, touchedPoint.y, 0);
        spaceship.transformMatrix();
        setIdentityM(modelMatrixForFire, 0);
        translateM(modelMatrixForFire, 0, spaceship.getX(), spaceship.getY() - 0.4f, -1);

    }

    public void handleTouchPress(float normalizedX, float normalizedY) {

    }

    private void drawFire() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrixForFire, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                camera.getViewMatrix(), 0);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticleShooter.addParticles(particleSystem, currentTime, 5);

        particleProgram.useProgram();
        particleProgram.setUniforms(modelViewProjectionMatrix, currentTime, textureParticle);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();

        glDisable(GL_BLEND);
    }

    private Ray convertNormalized2DPointToRay(
            float normalizedX, float normalizedY) {

        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc =  {normalizedX, normalizedY,  1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);


        Point nearPointRay = new Point(nearPointWorld[0]/nearPointWorld[3], nearPointWorld[1]/nearPointWorld[3], nearPointWorld[2]/nearPointWorld[3]);

        Point farPointRay = new Point(farPointWorld[0]/farPointWorld[3], farPointWorld[1]/farPointWorld[3], farPointWorld[2]/farPointWorld[3]);

        return new Ray(nearPointRay,
                Geometry.vectorBetween(nearPointRay, farPointRay));
    }






}
