package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.Objects.Model;
import android.ahaonline.com.edan35.R;
import android.content.Context;

import static android.ahaonline.com.edan35.programs.ShaderProgram.A_POSITION;
import static android.ahaonline.com.edan35.programs.ShaderProgram.U_MVP_MATRIX;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Felix on 2016-12-13.
 */

public class ShaderBlur extends ShaderProgram {

    // Unifrom locations
    private final int uHorizontalLocation;
    private final int uImageLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTexCoordLocation;

    public ShaderBlur(Context context) {

        super(context, R.raw.guassian_vertex_shader,
                R.raw.guassian_fragment_shader);

        //Uniforms
        uHorizontalLocation = glGetUniformLocation(program, "horizontal");
        uImageLocation = glGetUniformLocation(program, "image");

        //Attributes
        aPositionLocation = glGetAttribLocation(program, "position");
        aTexCoordLocation = glGetAttribLocation(program, "texCoords");

    }

    public void setUniforms(float[] mvpMatrix, Model light) {

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureAttributeLocation() {return aTexCoordLocation; }
}
