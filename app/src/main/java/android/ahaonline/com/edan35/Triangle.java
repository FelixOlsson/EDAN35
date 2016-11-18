package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.ahaonline.com.edan35.Constants.*;

/**
 * Created by felix on 15/11/2016.
 */
public class Triangle {
    private FloatBuffer vertexBuffer;

    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT; // 4 bytes per vertex

    private final String vertexShaderCode;
    private final String fragmentShaderCode;





    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f   // bottom right
    };

    // Set color with red, gree, blue and alpha (opacity) values
    float color[] = { 1.0f, 0.0f, 0.0f, 0.5f };

    private Context context;

    public Triangle(Context context) {

        this.context = context;

        vertexShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_vertex_shader);
        fragmentShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_fragment_shader);

        vertexBuffer = new VertexBuffer(triangleCoords).getVertexBuffer();

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader´s vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader´s vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Display the vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
