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
    private final int uColorLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ShaderTestProgram(Context context) {

        super(context, R.raw.test_vertex_shader,
    R.raw.test_fragment_shader);

        //Uniforms
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);

        //Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);

}

    public void setUniforms(float[] mvpMatrix, float[] color) {
        //glUseProgram(program);
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        glUniform4fv(uColorLocation, 1, color, 0);



    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
