package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.ahaonline.com.edan35.programs.ShaderProgram.A_COLOR;
import static android.ahaonline.com.edan35.programs.ShaderProgram.A_POSITION;
import static android.ahaonline.com.edan35.programs.ShaderProgram.U_COLOR;
import static android.ahaonline.com.edan35.programs.ShaderProgram.U_MVP_MATRIX;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Felix on 2016-11-24.
 */

public class TextureShaderProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;
    private final int uTextureUnitLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;

    public TextureShaderProgram(Context context) {

        super(context, R.raw.test_texture_vertex_shader,
                R.raw.test_texture_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

    }

    public void setUniforms(float[] mvpMatrix, int textureid) {
        //glUseProgram(program);
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);

        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D, textureid);

        glUniform1i(uTextureUnitLocation, 0);


    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getaTextureCoordinatesAttributeLocation() {
        return  aTextureCoordinateLocation;
    }

}
