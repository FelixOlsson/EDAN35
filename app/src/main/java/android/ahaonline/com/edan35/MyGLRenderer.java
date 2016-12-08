package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.SkyBox;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.SkyBoxShaderProgram;
import android.ahaonline.com.edan35.programs.TextureHelper;
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




public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private ShaderTestProgram shaderTestProgram;
    private ShaderLightProgram shaderLightProgram;
    private TextureShaderProgram textureShaderProgram;
    private Model model;
    private Light light;
    private Dialog loadScreen;

    private float xRotation, yRotation;

    private SkyBoxShaderProgram skyboxProgram;
    private SkyBox skybox;
    private int skyboxTexture;

    private int texture;
    private int rotationtemp = 0;
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private final float[] transposdMatrix = new float[16];
    private final float[] inversedMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    public MyGLRenderer(Context context, Dialog loadScreen) {
        this.context = context;
        this.loadScreen = loadScreen;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        textureShaderProgram = new TextureShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.earth);
        model = new Model();
        model.loadModel(context, R.raw.cube2);
        shaderTestProgram = new ShaderTestProgram(context);
        shaderLightProgram = new ShaderLightProgram(context);
        //scaleM(model.getModelMatrix(), 0, 3f, 3f, 3f);
        light = new Light();
        model.scale(3f);
        model.translate(1f,1f, -1f);

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

        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 3f, 100f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -27, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //rotateM(viewMatrix, 0, -45, 1f, 0f, 0f);
        //rotateM(viewMatrix, 0, -180, 0f, 1f, 0f);

        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, light.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        shaderLightProgram.useProgram();
        light.bindShader(shaderLightProgram);
        shaderLightProgram.setUniforms(modelViewProjectionMatrix);
        light.draw();


        model.rotateX(1.0f);
        //model.translate(1.0f, 1.0f, -1.0f);
        model.transformMatrix();
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, model.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);


        textureShaderProgram.useProgram();
        model.bindShader(textureShaderProgram);
        Matrix.invertM(inversedMatrix, 0, model.getModelMatrix(), 0);
        Matrix.transposeM(normalMatrix, 0, inversedMatrix, 0);
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, model.getModelMatrix(), texture, light, normalMatrix);
        model.draw();

        drawSkybox();




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


}
