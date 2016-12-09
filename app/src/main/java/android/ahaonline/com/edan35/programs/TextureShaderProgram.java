package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.ahaonline.com.edan35.programs.ShaderProgram.A_COLOR;
import static android.ahaonline.com.edan35.programs.ShaderProgram.A_POSITION;
import static android.ahaonline.com.edan35.programs.ShaderProgram.U_COLOR;
import static android.ahaonline.com.edan35.programs.ShaderProgram.U_MVP_MATRIX;

import static android.opengl.GLES30.*;
/**
 * Created by Felix on 2016-11-24.
 */

public class TextureShaderProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;
    private final int uMMatrixLocation;
   // private final int uTextureUnitLocation;
    private final int uLightPosLocation;
    private final int uNormalMatrixLocation;
    private final int uViewPositionLocation;
    private final int uNormalViewLocation;
    private final int uDiffuseTextureLocation;
    private final int uSpecularTextureLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;
    private final int aNormalLocation;

    public TextureShaderProgram(Context context) {

        super(context, R.raw.test_texture_vertex_shader,
                R.raw.test_texture_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        uMMatrixLocation = glGetUniformLocation(program, U_M_MATRIX);
        //uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uLightPosLocation = glGetUniformLocation(program, U_LIGHT_POS);
        uNormalMatrixLocation = glGetUniformLocation(program, U_NORMAL_MATRIX);
        uViewPositionLocation = glGetUniformLocation(program, U_VIEW_POS);
        uNormalViewLocation = glGetUniformLocation(program, U_IT_MV_MATRIX);
        uDiffuseTextureLocation = glGetUniformLocation(program, "material.diffuseTex");
        uSpecularTextureLocation = glGetUniformLocation(program, "material.specularTex");


        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);

    }

    public void setUniforms(float[] mvpMatrix, float[] mMatrix, int textureid, Light light, float[] normal, float[] viewPos, float[] ViewNormMatrix, int textureid2) {
        //glUseProgram(program);
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        glUniformMatrix4fv(uMMatrixLocation, 1, false, mMatrix, 0);
        glUniform3f(uLightPosLocation, 0, 0, 0);
        glUniformMatrix4fv(uNormalMatrixLocation, 1, false, normal, 0);
        glUniform3f(uViewPositionLocation, viewPos[0], viewPos[1], viewPos[2]);
        glUniformMatrix4fv(uNormalViewLocation, 1, false, ViewNormMatrix, 0);

        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D, textureid);

        glUniform1i(uDiffuseTextureLocation, 0);

        glActiveTexture(GL_TEXTURE1);

        glBindTexture(GL_TEXTURE_2D, textureid2);

        glUniform1i(uSpecularTextureLocation, 0);

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureCoordinatesAttributeLocation() {
        return  aTextureCoordinateLocation;
    }
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }

}
