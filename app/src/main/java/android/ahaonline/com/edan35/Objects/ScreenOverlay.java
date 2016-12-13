package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-10.
 */

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.FrameShaderProgram;
import android.ahaonline.com.edan35.programs.ShaderBlur;
import android.ahaonline.com.edan35.programs.ShaderProgram;

import static android.opengl.GLES30.*;



/**
 * Created by Felix on 2016-12-04.
 */

public class ScreenOverlay  {

    private VertexBuffer vertexBuffer;
    private VertexBuffer texCoordBuffer;
    private FrameShaderProgram fshader;
    private ShaderBlur bshader;

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
        this.fshader = shaderLightProgram;
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getPositionAttributeLocation(),
                2, 0);
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getTextureAttributeLocation(),
                2, 0);
    }

    public void bindShader(ShaderBlur shaderLightProgram) {
        this.bshader = shaderLightProgram;
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getPositionAttributeLocation(),
                2, 0);
        vertexBuffer.setVertexAttribPointer(0,
                shaderLightProgram.getTextureAttributeLocation(),
                2, 0);
    }

    public void draw() {

        glDrawArrays(GL_TRIANGLES, 0, 6);
        if(fshader != null) {
            glDisableVertexAttribArray(fshader.getPositionAttributeLocation());
            glDisableVertexAttribArray(fshader.getTextureAttributeLocation());
        } else if(bshader != null) {
            glDisableVertexAttribArray(bshader.getPositionAttributeLocation());
            glDisableVertexAttribArray(bshader.getTextureAttributeLocation());
        }
    }
}
