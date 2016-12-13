package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.opengl.GLES30.*;

/**
 * Created by Felix on 2016-12-04.
 */

public class ShaderLightProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;
    private final int uDiffuseLocation;

    // Attribute locations
    private final int aPositionLocation;

    public ShaderLightProgram(Context context) {

        super(context, R.raw.light_vertex_shader,
                R.raw.light_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        uDiffuseLocation = glGetUniformLocation(program, "u_Diffuse");

        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

    }

    public void setUniforms(float[] mvpMatrix, Model light) {
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        glUniform3f(uDiffuseLocation, light.getDiffuse()[0], light.getDiffuse()[1], light.getDiffuse()[2]);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
