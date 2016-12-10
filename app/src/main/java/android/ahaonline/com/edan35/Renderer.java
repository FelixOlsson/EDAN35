package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.ScreenOverlay;
import android.ahaonline.com.edan35.Objects.SkyBox;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.SkyBoxShaderProgram;
import android.ahaonline.com.edan35.util.TextureHelper;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.app.Dialog;
import android.content.Context;

import static android.opengl.GLES30.*;


import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by felix on 15/11/2016.
 */




public class Renderer implements GLSurfaceView.Renderer {

    private Context context;
    private ShaderTestProgram shaderTestProgram;
    private ShaderLightProgram shaderLightProgram;
    private TextureShaderProgram textureShaderProgram;
    private FrameShaderProgram frameShaderProgram;
    private Model model;
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
        frameShaderProgram = new FrameShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.container2);
        texture2 = TextureHelper.loadTexture(context, R.drawable.container2_specular);
        model = new Model();
        model.loadModel(context, R.raw.cube);
        shaderTestProgram = new ShaderTestProgram(context);
        shaderLightProgram = new ShaderLightProgram(context);
        light = new Light();
        model.scale(3f);
        model.translate(0f,1f, -2f);
        screenOverlay = new ScreenOverlay();

        skyboxProgram = new SkyBoxShaderProgram(context);
        skybox = new SkyBox();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int[] { R.drawable.spacelf, R.drawable.spacert,
                        R.drawable.spaceup, R.drawable.spacedn,
                         R.drawable.spaceft, R.drawable.spacebk});

        loadScreen.dismiss();










    }

    @Override
    public void onSurfaceChanged(GL10 unued, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 0.1f, 100f);

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


        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, light.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        shaderLightProgram.useProgram();
        light.bindShader(shaderLightProgram);
        shaderLightProgram.setUniforms(modelViewProjectionMatrix);
        light.draw();


        model.rotateX(0.5f);

        model.transformMatrix();
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, model.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);


        textureShaderProgram.useProgram();
        model.bindShader(textureShaderProgram);
        Matrix.invertM(inversedMatrix, 0, model.getModelMatrix(), 0);
        Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);

        Matrix.invertM(inversedViewMatrix, 0, modelViewMatrix, 0);
        Matrix.transposeM(normalViewMatrix, 0, inversedViewMatrix, 0);

        textureShaderProgram.setUniforms(modelViewProjectionMatrix, model.getModelMatrix(), texture, light, normalMatrix, new float[]{0,0,0f}, normalViewMatrix, texture2);
        model.draw();

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

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;
        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }
    }

    private void drawSkybox() {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(viewProjectionMatrix, skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
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



}
