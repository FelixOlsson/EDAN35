package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Cube;
import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.Objects.Sphere;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.TextureHelper;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.R.attr.mode;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_TEST;
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

    private int texture;

    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private final float[] transposdMatrix = new float[16];
    private final float[] inversedMatrix = new float[16];

    public MyGLRenderer(Context context) { this.context = context; }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GLES20.glEnable(GL_CULL_FACE);
        GLES20.glEnable(GL_DEPTH_TEST);
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

    }

    @Override
    public void onSurfaceChanged(GL10 unued, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 3f, 100f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -27, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //rotateM(viewMatrix, 0, -45, 1f, 0f, 0f);
       // rotateM(viewMatrix, 0, -45, 0f, 1f, 0f);

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





    }

    public void handleTouchDrag(float deltaX, float deltaY) {

    }


}
