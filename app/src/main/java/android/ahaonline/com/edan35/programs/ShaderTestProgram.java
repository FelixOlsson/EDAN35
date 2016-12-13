package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.opengl.GLES30.*;

/**
 * Created by felix on 18/11/2016.
 */
public class ShaderTestProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;
    private final int uDiffuseTextureLocation;



    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;


    public ShaderTestProgram(Context context) {

        super(context, R.raw.test_vertex_shader,
    R.raw.test_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, "u_MVPMatrix");
        uDiffuseTextureLocation = glGetUniformLocation(program, "u_Texture");


        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);


}

    public void setUniforms(float[] mvpMatrix, int textureId) {

        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);

        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D, textureId);

        glUniform1i(uDiffuseTextureLocation, 0);


    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureCoordinatesAttributeLocation() {
        return  aTextureCoordinateLocation;
    }

}
