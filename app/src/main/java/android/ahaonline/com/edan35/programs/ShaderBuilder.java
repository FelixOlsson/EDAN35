package android.ahaonline.com.edan35.programs;

import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES30.*;

/**
 * Created by felix on 17/11/2016.
 */
public class ShaderBuilder {



        private static int compileShader(int type, String shaderCode) {

            final int shaderObjectId = glCreateShader(type);


            glShaderSource(shaderObjectId, shaderCode);

            glCompileShader(shaderObjectId);

            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);


            if (compileStatus[0] == 0) {
                Log.v(TAG, "Error:"  + glGetShaderInfoLog(shaderObjectId));
                glDeleteShader(shaderObjectId);
                return 0;
            }

            return shaderObjectId;

        }


        private static int linkProgram(int vertexShaderId, int fragmentShaderId) {

            final int programObjectId = glCreateProgram();

            glAttachShader(programObjectId, vertexShaderId);
            glAttachShader(programObjectId, fragmentShaderId);

            glLinkProgram(programObjectId);

            final int[] linkStatus = new int[1];
            glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);


            if (linkStatus[0] == 0) {
                Log.v(TAG, "Error:"  + glGetProgramInfoLog(programObjectId));
                glDeleteProgram(programObjectId);

                return 0;
            }

            return programObjectId;
        }


        public static int buildProgram(String vertexShaderSource,
                                       String fragmentShaderSource) {
            int program;

            int vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderSource);
            int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource);

            program = linkProgram(vertexShader, fragmentShader);

            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

            return program;
        }



}