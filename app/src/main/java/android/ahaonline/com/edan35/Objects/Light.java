package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.ShaderLightProgram;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.opengl.GLES20;

import static android.ahaonline.com.edan35.Constants.COORDS_PER_VERTEX;


/**
 * Created by Felix on 2016-12-04.
 */

public class Light extends transformController {

    private VertexBuffer vertexBuffer;

    public Light() {
        super();
        vertexBuffer = new VertexBuffer(lightCoords);
    }

    static float lightCoords[] = {   // in counterclockwise order:
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

    public void bindShader(ShaderLightProgram shaderLightProgram) {
        //GLES20.glUseProgram(program);
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getPositionAttributeLocation(),
                COORDS_PER_VERTEX, 0);
    }

    public void draw() {

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, lightCoords.length);
    }
}
