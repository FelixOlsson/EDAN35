package android.ahaonline.com.edan35.programs;

/**
 * Created by Felix on 2016-12-10.
 */

import android.ahaonline.com.edan35.R;
import android.content.Context;


import static android.opengl.GLES30.*;


/**
 * Created by Felix on 2016-12-04.
 */

public class FrameShaderProgram extends ShaderProgram {

    // Unifrom locations
    private final int uTextureLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;

    public FrameShaderProgram(Context context) {

        super(context, R.raw.frame_vertex_shader,
                R.raw.frame_fragment_shader);

        //Uniforms
        uTextureLocation = glGetUniformLocation(program, "screenTexture");

        //Attributes
        aPositionLocation = glGetAttribLocation(program, "position");
        aTextureCoordinateLocation = glGetAttribLocation(program, "texCoords");

    }

    public void setUniforms(int textureid) {
        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D, textureid);

        glUniform1i(uTextureLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureAttributeLocation() {
        return aTextureCoordinateLocation;
    }

}
