package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-10.
 */

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;



/**
 * Created by Felix on 2016-12-04.
 */

public class ScreenOverlay  {

    private VertexBuffer vertexBuffer;
    private VertexBuffer texCoordBuffer;

    public ScreenOverlay() {

        vertexBuffer = new VertexBuffer(vertCoords);
        texCoordBuffer = new VertexBuffer(texCoords);
    }

    static float vertCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,

            -1.0f,  1.0f,
            1.0f, -1.0f,
            1.0f,  1.0f,
    };

    static float texCoords[] = {
            0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f,  0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f

    };

    public void bindShader(FrameShaderProgram shaderLightProgram) {
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getPositionAttributeLocation(),
                2, 0);
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getTextureAttributeLocation(),
                2, 0);
    }

    public void draw() {

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
