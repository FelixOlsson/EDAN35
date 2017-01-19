package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.ParticleSpawner;
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
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Iterator;
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

    private Vec3 touchedPoint;
    private Vec3 startPoint;

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
    private int noiseTexture;
    private int life = 3;

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

    private float currentStep;

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleSpawner engine;
    private ParticleSystem particleSystemExplosions;
    private ArrayList<ParticleSpawner> explosions = new ArrayList<>();
    private ArrayList<Vec3> explosionPoints = new ArrayList<>();
    private ArrayList<Vec3> laserPoints = new ArrayList<>();

    private ArrayList<List> ar;
    private ArrayList<Model> laserShots = new ArrayList<>();

    private int height;
    private int width;


    private Toast toast;
    private View layout;

    public Renderer(Context context, Dialog loadScreen, Toast toast, View layout ) {
        this.context = context;
        this.loadScreen = loadScreen;
        this.toast = toast;
        this.layout = layout;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        final float angleVarianceInDegrees = 25f;
        final float speedVariance = 5f;
        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(500);
        globalStartTime = System.nanoTime();
        engine = new ParticleSpawner(new float[]{0f, 0f, 0f}, new float[]{0f, 0f, -0.5f}, new float[]{255, 50, 5}, angleVarianceInDegrees, speedVariance);

        particleSystemExplosions = new ParticleSystem(500);



        // Objects
        for(int i = 0; i < 15; i++) {
            Model asteroid = new Model();
            asteroid.loadModel(context, R.raw.asteroid1);
            asteroid.scale(randomNumber(0.5f,5.0f));
            float x = randomNumber(-25.0f,25.0f);
            float y = randomNumber(-25.0f,25.0f);
            float z = randomNumber(5.0f, 55.0f);
            asteroid.translate(x,y,z);
            asteroid.transformMatrix();
            asteroids.add(asteroid);
        }

        for(int i = 0; i < 15; i++) {
            Model asteroid = new Model();
            asteroid.loadModel(context, R.raw.asteroid2);
            asteroid.scale(randomNumber(0.5f,5.0f));
            float x = randomNumber(-15.0f,15.0f);
            float y = randomNumber(-15.0f,15.0f);
            float z = randomNumber(5.0f, 55.0f);
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
            float x = randomNumber(-15.0f,15.0f);
            float y = randomNumber(-15.0f,15.0f);
            float z = randomNumber(5.0f,55.0f);
            light.translate(x,y,z);
            light.transformMatrix();
            lights.add(light);
        }


        spaceship = new Model();
        spaceship.loadModel(context, R.raw.spaceship1);
        spaceship.translate(0,0,0);
        touchedPoint = new Vec3(0,0,0);
        startPoint = new Vec3(0,0,0);
        spaceship.transformMatrix();
        skybox = new SkyBox();

        setIdentityM(modelMatrixForFire, 0);
        translateM(modelMatrixForFire, 0, spaceship.getX(), spaceship.getY() - 0.1f, -1);

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
        textureParticle = TextureHelper.loadTexture(context, R.drawable.fire);
        noiseTexture = TextureHelper.loadTexture(context, R.drawable.noisetex);


        //Utility
        camera = new Camera();
        light = new Light();
        screenOverlay = new ScreenOverlay();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int[] {  R.drawable.spacelf, R.drawable.spacert,
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
        Matrix.perspectiveM(projectionMatrix, 0, 120f, ratio, 0.1f, 100f);
        postProcessingEffect(width,height);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        ControlSpaceship();

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
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glDisable(GL_DEPTH_TEST);
        drawFire();
        drawExplosion();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        drawLaser();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);

        frameShaderProgram.useProgram();
        screenOverlay.bindShader(frameShaderProgram);
        frameShaderProgram.setUniforms(texColorBuffer[0], texColorBuffer[1]);
        screenOverlay.draw();
    }

    private void drawLaser() {

       for(Model laser : laserShots) {
           setIdentityM(laser.getModelMatrix(), 0);
           translateM(laser.getModelMatrix(), 0, laser.getX(), laser.getY(), laser.getZ() + 1.0f);
           rotateM(laser.getModelMatrix(), 0, 90f, 0f, 1f, 0f);
           multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, laser.getModelMatrix(), 0);
           Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
           laser.lightVariables(new float[]{0, 0, 0.5f}
                   , new float[]{1, 0, 0},
                   new float[]{0, 0, 0}, 1.0f, 0.007f, 0.0002f);
           shaderLightProgram.useProgram();
           laser.bindShader(shaderLightProgram);
           shaderLightProgram.setUniforms(modelViewProjectionMatrix, laser);
           laser.draw();
           laser.setCoordinates();
       }
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

    private void respawnAsteroid(Model asteroid) {
        float x = randomNumber(-25.0f,25.0f);
        float y = randomNumber(-25.0f,25.0f);
        float z = randomNumber(60.0f, 75.0f);
        asteroid.setIdentitiy();
        asteroid.scale(randomNumber(0.1f, 10f));
        asteroid.translate(x,y,z);
        asteroid.transformMatrix();
    }

    private void drawAsteroids() {

        for(Model asteroid : asteroids) {

            if(asteroid.getZ() < -28.0f) {
                respawnAsteroid(asteroid);
            } else if(asteroid.getZ() < 5f && asteroid.getZ() > 0f){
                if(Math.sqrt((asteroid.getX() - spaceship.getX()) * (asteroid.getX() - spaceship.getX()) +
                        (asteroid.getY() - spaceship.getY()) * (asteroid.getY() - spaceship.getY()) +
                        (asteroid.getZ() - spaceship.getZ()) * (asteroid.getZ() - spaceship.getZ()))< (5 * asteroid.getSize())) {

                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);

                    if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        String hearts = "";

                        for(int i = 0; i < life; i++) {
                            hearts += getEmojiByUnicode(0x2764);
                        }

                        text.setText(hearts);

                        toast.show();
                        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        life--;
                    }
                }
            }

            for(Model laser : laserShots) {
                Vec3 laserDir = new Vec3(1,0,0);
                Vec3 laserPoint = new Vec3(laser.getX(), laser.getY(), laser.getZ());
                Vec3 asteroidPoint = new Vec3(asteroid.getX(), asteroid.getY(), asteroid.getZ());
                Vec3 between = new Vec3(laserPoint, asteroidPoint);

                if(laserDir.dotProduct(between) > 0 && Geometry.distancePointToPoint(laserPoint, asteroidPoint) < 4){
                    explosions = new ArrayList<>();
                    explosionPoints = new ArrayList<>();
                    float angleVarianceInDegrees2 = 180f;
                    float speedVariance2 = randomNumber(15.0f, 25.0f);
                    explosions.add(new ParticleSpawner(new float[]{0f, 0f, 0f}, new float[]{0f, 0f, -0.5f}, new float[]{255, 50, 5}, angleVarianceInDegrees2, speedVariance2));
                    explosionPoints.add(new Vec3(asteroid.getX(), asteroid.getY(), asteroid.getZ()));
                    respawnAsteroid(asteroid);
                }

            }

            asteroid.translate(0 , 0, - 0.07f);
            asteroid.transformMatrix();

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

    private void drawExplosion() {
        Iterator<ParticleSpawner> explosion = explosions.iterator();
        Iterator<Vec3> points = explosionPoints.iterator();

        while(explosion.hasNext() && points.hasNext()) {

            float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
            ParticleSpawner tempExplosion = explosion.next();
            Vec3 tempPoint = points.next();
            if( tempPoint != null) {
                float [] modelMatrixForExplosion = new float[16];

                setIdentityM(modelMatrixForExplosion, 0);
                translateM(modelMatrixForExplosion, 0, tempPoint.x, tempPoint.y, tempPoint.z);

                multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrixForExplosion, 0);
                Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
                multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                        camera.getViewMatrix(), 0);

                if(tempExplosion.once()) {
                    tempExplosion.spawnParticles(particleSystemExplosions, currentTime, 500);
                }

                particleProgram.useProgram();
                particleProgram.setUniforms(modelViewProjectionMatrix, currentTime, textureParticle, noiseTexture);
                particleSystemExplosions.bindData(particleProgram);
                particleSystemExplosions.draw();

            }
        }

    }

    private void drawLights() {

        for(Model light : lights) {

            if(light.getZ() < -40.0f) {
                light.lightVariables(new float[]{randomNumber(0.3f,10.5f),randomNumber(0.3f,0.5f),randomNumber(0.3f,0.5f)}
                        , new float[]{randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f)},
                        new float[]{randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f),randomNumber(0.0f,1.0f)}, 1.0f, 0.007f, 0.0002f);

                float x = randomNumber(-10.0f,10.0f);
                float y = randomNumber(-10.0f,10.0f);
                float z = randomNumber(50.0f,75.0f);
                light.setIdentitiy();
                light.scale(randomNumber(1.0f,5.0f));
                light.translate(x,y,z);
                light.transformMatrix();
            }

            light.translate(0 , 0, - 0.07f);

            light.transformMatrix();
            multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, light.getModelMatrix(), 0);
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

            shaderLightProgram.useProgram();
            light.bindShader(shaderLightProgram);
            shaderLightProgram.setUniforms(modelViewProjectionMatrix, light);
            light.draw();
        }

    }

    private void drawSpaceship() {

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

        shaderTestProgram.setUniforms(modelViewProjectionMatrix, texture3);
        spaceship.draw();
    }

    private float randomNumber(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private Vec3 oldPoint = new Vec3(0,0,0);


    public void handleTouchDrag(float normalizedX, float normalizedY) {


        float[] nearPointNdc = {normalizedX, normalizedY, -1.0f, 1};
        float[] farPointNdc =  {normalizedX, normalizedY,  1, 1};

        float[] nearPointFrustum = new float[4];
        float[] farPointFrustum = new float[4];

        multiplyMV(nearPointFrustum, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointFrustum, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        Vec3 nearPointRay = new Vec3(nearPointFrustum[0]/nearPointFrustum[3], nearPointFrustum[1]/nearPointFrustum[3], nearPointFrustum[2]/nearPointFrustum[3]);
        Vec3 farPointRay = new Vec3(farPointFrustum[0]/farPointFrustum[3], farPointFrustum[1]/farPointFrustum[3], farPointFrustum[2]/farPointFrustum[3]);

        Ray ray =  new Ray(nearPointRay, new Vec3(nearPointRay, farPointRay));

        Plane plane = new Plane(new Vec3(0, 0, 0), new Vec3(0, 0, -1));
        touchedPoint = Geometry.intersectionRayPlane(ray, plane);
        if(oldPoint.x != touchedPoint.x && oldPoint.y != touchedPoint.y) {
            startPoint = new Vec3(spaceship.getX(), spaceship.getY(), 0);
            currentStep = 0f;
        }
        oldPoint = touchedPoint;

    }

    private void ControlSpaceship() {

        if(Math.round(spaceship.getX() * 100.0) / 100.0 != Math.round(touchedPoint.x * 100.0) / 100.0
                && Math.round(spaceship.getY() * 100.0) / 100.0 != Math.round(touchedPoint.y * 100.0) / 100.0) {
            Vec3 distance = interpolation(startPoint, touchedPoint, currentStep++, 100f);
            spaceship.setIdentitiy();
            spaceship.translate(distance.x, distance.y, 0);
            spaceship.transformMatrix();
            setIdentityM(modelMatrixForFire, 0);
            translateM(modelMatrixForFire, 0, spaceship.getX(), spaceship.getY() - 0.1f, -1);
        }
    }

    private Vec3 interpolation(Vec3 location, Vec3 destination, float currentStep, float steps) {
        return new Vec3(location.x + currentStep * (destination.x - location.x) / steps,
                location.y + currentStep * (destination.y - location.y) / steps, 0);
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {

        float[] nearPointNdc = {normalizedX, normalizedY, -1.0f, 1};
        float[] farPointNdc =  {normalizedX, normalizedY,  100, 1};

        float[] nearPointFrustum = new float[4];
        float[] farPointFrustum = new float[4];

        multiplyMV(nearPointFrustum, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointFrustum, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        Vec3 nearPointRay = new Vec3(nearPointFrustum[0]/nearPointFrustum[3], nearPointFrustum[1]/nearPointFrustum[3], nearPointFrustum[2]/nearPointFrustum[3]);
        Vec3 farPointRay = new Vec3(farPointFrustum[0]/farPointFrustum[3], farPointFrustum[1]/farPointFrustum[3], farPointFrustum[2]/farPointFrustum[3]);

        Ray ray =  new Ray(nearPointRay, new Vec3(nearPointRay, farPointRay));
        Plane plane = new Plane(new Vec3(0, 0, 0), new Vec3(0, 0, -1));
        Vec3 p;
        p = Geometry.intersectionRayPlane(ray, plane);


        if(Geometry.intersectionPointSphere(p.x,p.y,spaceship.getX(),spaceship.getY(), 9.0f)) {
                Model laser = new Model();
                laser.loadModel(context, R.raw.laser);
                laserShots.add(laser);
                setIdentityM(laser.getModelMatrix(), 0);
                translateM(laser.getModelMatrix(), 0, spaceship.getX(), spaceship.getY(), 10f);
                rotateM(laser.getModelMatrix(), 0, 90f, 0f, 1f, 0f);
                laser.setCoordinates();
        }
    }

    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private void drawFire() {
        multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrixForFire, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                camera.getViewMatrix(), 0);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        engine.spawnParticles(particleSystem, currentTime, 10);

        particleProgram.useProgram();
        particleProgram.setUniforms(modelViewProjectionMatrix, currentTime, textureParticle, noiseTexture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }



}
