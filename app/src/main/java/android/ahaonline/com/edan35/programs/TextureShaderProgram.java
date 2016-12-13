package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.Objects.Light;
import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.R;
import android.ahaonline.com.edan35.util.Camera;
import android.content.Context;

import java.util.ArrayList;

import static android.opengl.GLES30.*;
/**
 * Created by Felix on 2016-11-24.
 */

public class TextureShaderProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;
    private final int uMMatrixLocation;

    private final int uViewPositionLocation;
    private final int uNumberOfLightsLocation;


    private final int uDiffuseTextureLocation;
    private final int uSpecularTextureLocation;
    private final int uShininessLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;
    private final int aNormalLocation;

    public TextureShaderProgram(Context context) {

        super(context, R.raw.texture_vertex_shader,
                R.raw.texture_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        uMMatrixLocation = glGetUniformLocation(program, U_M_MATRIX);
        uViewPositionLocation = glGetUniformLocation(program, U_VIEW_POS);
        uNumberOfLightsLocation = glGetUniformLocation(program, "u_NR_Point_Lights");


        uDiffuseTextureLocation = glGetUniformLocation(program, "material.diffuseTex");
        uSpecularTextureLocation = glGetUniformLocation(program, "material.specularTex");
        uShininessLocation = glGetAttribLocation(program, "material.shininess");

        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);

    }

    public void setUniforms(float[] mvpMatrix, float[] mMatrix, int textureDiffuse, int textureSpecular, Camera camera, ArrayList<Model> lights, float shininess) {

       glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        glUniformMatrix4fv(uMMatrixLocation, 1, false, mMatrix, 0);
        glUniform3f(uViewPositionLocation, camera.getX(), camera.getY(), camera.getZ());
       glUniform1f(uShininessLocation, shininess);

        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D, textureDiffuse);

        glUniform1i(uDiffuseTextureLocation, 0);

        glActiveTexture(GL_TEXTURE1);

        glBindTexture(GL_TEXTURE_2D, textureSpecular);

        glUniform1i(uSpecularTextureLocation, 0);
        String pre = "light[";
        String post = "].";
        String position = "position";
        String ambient = "ambient";
        String diffuse = "diffuse";
        String specular = "specular";
        String constant = "constant";
        String linear = "linear";
        String quadratic = "quadratic";
        for(int i = 0; i < lights.size(); i++) {
            glUniform3f(glGetUniformLocation(program, pre + i + post + position), lights.get(i).getX(), lights.get(i).getY(), lights.get(i).getZ());
            glUniform3f(glGetUniformLocation(program, pre + i + post + ambient), lights.get(i).getAmbient()[0], lights.get(i).getAmbient()[1], lights.get(i).getAmbient()[2]);
            glUniform3f(glGetUniformLocation(program, pre + i + post + diffuse), lights.get(i).getDiffuse()[0], lights.get(i).getDiffuse()[1], lights.get(i).getDiffuse()[2]);
            glUniform3f(glGetUniformLocation(program, pre + i + post + specular), lights.get(i).getSpecular()[0], lights.get(i).getSpecular()[1], lights.get(i).getSpecular()[2]);
            glUniform1f(glGetUniformLocation(program, pre + i + post + constant), lights.get(i).getConstant());
            glUniform1f(glGetUniformLocation(program, pre + i + post + linear), lights.get(i).getLinear());
            glUniform1f(glGetUniformLocation(program, pre + i + post + quadratic), lights.get(i).getLinear());
        }

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
