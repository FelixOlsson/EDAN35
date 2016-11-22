package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.Objects.Cube;
import android.ahaonline.com.edan35.Objects.Sphere;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
    private Cube cube, cube2;
    private Sphere sphere;
    private ShaderTestProgram shaderTestProgram;

    // modelViewProjectionMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];

    public MyGLRenderer(Context context) { this.context = context; }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GL_DEPTH_TEST);
        GLES20.glEnable(GL_CULL_FACE);
         //sphere = new Sphere(3f,3,3, context);

        shaderTestProgram = new ShaderTestProgram(context);
        cube = new Cube(context);
        cube2 = new Cube(context);
        setIdentityM(cube.getModelMatrix(), 0);
        scaleM(cube.getModelMatrix(), 0, 0.5f, 0.5f, 0.5f);

        setIdentityM(cube2.getModelMatrix(), 0);
        scaleM(cube2.getModelMatrix(), 0, 0.5f, 0.5f, 0.5f);
        cube2.translate(3.5f, 0, 0);

    }

    @Override
    public void onSurfaceChanged(GL10 unued, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 1f, 100f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -7, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        rotateM(viewMatrix, 0, -45, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -45, 0f, 1f, 0f);

        cube.rotateX(1.0f);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, cube.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);



        //triangle.draw(modelViewProjectionMatrix);
        //square.draw(modelViewProjectionMatrix);
        //shaderTestProgram.useProgram();
        shaderTestProgram.useProgram();
        cube.bindShader(shaderTestProgram);
        shaderTestProgram.setUniforms(modelViewProjectionMatrix);
        cube.draw();

        cube2.rotateZ(2f);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, cube2.getModelMatrix(), 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        //shaderTestProgram.useProgram();
        cube2.bindShader(shaderTestProgram);
        shaderTestProgram.setUniforms(modelViewProjectionMatrix);
        cube2.draw();

        /*sphere.bindShader(shaderTestProgram);
        shaderTestProgram.setUniforms(modelViewProjectionMatrix);
        sphere.draw();*/


    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        cube.scale(1.001f);
    }


}
