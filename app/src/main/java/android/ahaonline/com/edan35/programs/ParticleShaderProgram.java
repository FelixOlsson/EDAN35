package android.ahaonline.com.edan35.programs;

import android.ahaonline.com.edan35.R;
import android.content.Context;


import static android.opengl.GLES30.*;

/**
 * Created by Felix on 2016-12-13.
 */

public class ParticleShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTimeLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;
    private final int uTextureUnitLocation;
    private final int uNoiseLocation;

    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);

        // Uniforms
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uNoiseLocation = glGetUniformLocation(program, "u_Noise");

        // Attributes
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);
    }

    public void setUniforms(float[] matrix, float elapsedTime, int textureId, int textureId2) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, textureId2);
        glUniform1i(uNoiseLocation, 1);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }
    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
