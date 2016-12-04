package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Felix on 2016-12-04.
 */

public class ShaderLightProgram extends ShaderProgram {

    // Unifrom locations
    private final int uMVPMatrixLocation;

    // Attribute locations
    private final int aPositionLocation;

    public ShaderLightProgram(Context context) {

        super(context, R.raw.test_light_vertex_shader,
                R.raw.test_light_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);

        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

    }

    public void setUniforms(float[] mvpMatrix) {
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
