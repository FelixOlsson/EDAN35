package android.ahaonline.com.edan35;

/**
 * Created by felix on 17/11/2016.
 */

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.ShaderProgram;
import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import static android.ahaonline.com.edan35.Constants.BYTES_PER_FLOAT;
import static android.ahaonline.com.edan35.Constants.COORDS_PER_VERTEX;


/**
 * Created by felix on 15/11/2016.
 */
public class Cube {
    private FloatBuffer vertexBuffer;
    private FloatBuffer vertexBufferColor;

    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int a_Color;
    private int mMVPMatrixHandle;

    private final int vertexCount = cubeCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT; // 4 bytes per vertex

    private final String vertexShaderCode;
    private final String fragmentShaderCode;




    static float cubeCoords[] = {   // in counterclockwise order:
            -1.0f,-1.0f,-1.0f, // triangle 1 : begin
            -1.0f,-1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f, // triangle 1 : end
            1.0f, 1.0f,-1.0f, // triangle 2 : begin
            -1.0f,-1.0f,-1.0f,
            -1.0f, 1.0f,-1.0f, // triangle 2 : end
            1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f,-1.0f,
            1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f,-1.0f, 1.0f,
            1.0f,-1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f,-1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f,-1.0f,
            -1.0f, 1.0f,-1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f,-1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f,-1.0f, 1.0f
    };

    // Set color with red, gree, blue and alpha (opacity) values
    float color[] = {0.583f,  0.771f,  0.014f,
            0.609f,  0.115f,  0.436f,
            0.327f,  0.483f,  0.844f,
            0.822f,  0.569f,  0.201f,
            0.435f,  0.602f,  0.223f,
            0.310f,  0.747f,  0.185f,
            0.597f,  0.770f,  0.761f,
            0.559f,  0.436f,  0.730f,
            0.359f,  0.583f,  0.152f,
            0.483f,  0.596f,  0.789f,
            0.559f,  0.861f,  0.639f,
            0.195f,  0.548f,  0.859f,
            0.014f,  0.184f,  0.576f,
            0.771f,  0.328f,  0.970f,
            0.406f,  0.615f,  0.116f,
            0.676f,  0.977f,  0.133f,
            0.971f,  0.572f,  0.833f,
            0.140f,  0.616f,  0.489f,
            0.997f,  0.513f,  0.064f,
            0.945f,  0.719f,  0.592f,
            0.543f,  0.021f,  0.978f,
            0.279f,  0.317f,  0.505f,
            0.167f,  0.620f,  0.077f,
            0.347f,  0.857f,  0.137f,
            0.055f,  0.953f,  0.042f,
            0.714f,  0.505f,  0.345f,
            0.783f,  0.290f,  0.734f,
            0.722f,  0.645f,  0.174f,
            0.302f,  0.455f,  0.848f,
            0.225f,  0.587f,  0.040f,
            0.517f,  0.713f,  0.338f,
            0.053f,  0.959f,  0.120f,
            0.393f,  0.621f,  0.362f,
            0.673f,  0.211f,  0.457f,
            0.820f,  0.883f,  0.371f,
            0.982f,  0.099f,  0.879f};

    private Context context;

    public Cube(Context context) {

        this.context = context;

        vertexShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_vertex_shader);
        fragmentShaderCode = ShaderResourceReader.readShaderFromResource(context,R.raw.test_fragment_shader);

        vertexBuffer = new VertexBuffer(cubeCoords).getVertexBuffer();
        vertexBufferColor = new VertexBuffer(color).getVertexBuffer();


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

        a_Color = GLES20.glGetAttribLocation(mProgram, "aColor");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(a_Color);

        GLES20.glVertexAttribPointer(a_Color, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBufferColor);

       /* // get handle to fragment shader´s vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
*/

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Display the vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(a_Color);
    }
}

