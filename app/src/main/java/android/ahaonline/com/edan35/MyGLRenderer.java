package android.ahaonline.com.edan35;

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

/**
 * Created by felix on 15/11/2016.
 */




public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private Triangle triangle;
    private Square square;
    private Cube cube;

    // modelViewProjectionMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];

    public MyGLRenderer(Context context) { this.context = context; }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GL_DEPTH_TEST);
        GLES20.glEnable(GL_CULL_FACE);

        triangle = new Triangle(context);
        square = new Square(context);
        cube = new Cube(context);
        setIdentityM(modelMatrix, 0);
        scaleM(modelMatrix, 0, 0.5f, 0.5f, 0.5f);
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


        // Calculate the projection and view transformation
        rotateM(modelMatrix, 0, -1, 0f, 1f, 0f);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        //triangle.draw(modelViewProjectionMatrix);
        //square.draw(modelViewProjectionMatrix);
        cube.draw(modelViewProjectionMatrix);

    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
