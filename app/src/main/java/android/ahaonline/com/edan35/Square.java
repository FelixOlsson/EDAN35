package android.ahaonline.com.edan35;

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.ShaderProgram;
import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import static android.ahaonline.com.edan35.Constants.*;

/**
 * Created by felix on 15/11/2016.
 */
public class Square {


    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT; // 4 bytes per vertex

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


    private final String vertexShaderCode;
    private final String fragmentShaderCode;

    private Context context;

    static float squareCoords [] = {
            -0.5f, 0.5f, 0.0f,  // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f,  // bottom right
            0.5f, 0.5f, 0.0f    // top right
    };

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    public Square(Context context) {
        this.context = context;

        vertexShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_vertex_shader);
        fragmentShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_fragment_shader);

        vertexBuffer = new VertexBuffer(squareCoords)
.getVertexBuffer();
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate value * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        mProgram = ShaderProgram.buildProgram(vertexShaderCode, fragmentShaderCode);
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
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,drawListBuffer);

        // Display the vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
