package android.ahaonline.com.edan35.programs;

import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES30.*;

/**
 * Created by felix on 17/11/2016.
 */
public class ShaderBuilder {


        /**
         * Loads and compiles a vertex shader, returning the OpenGL object ID.
         */
        private static int compileVertexShader(String shaderCode) {
            return compileShader(GL_VERTEX_SHADER, shaderCode);
        }


        /**
         * Loads and compiles a fragment shader, returning the OpenGL object ID.
         */
        private static int compileFragmentShader(String shaderCode) {
            return compileShader(GL_FRAGMENT_SHADER, shaderCode);
        }

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

        /**
         * Links a vertex shader and a fragment shader together into an OpenGL
         * program. Returns the OpenGL program object ID, or 0 if linking failed.
         */
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

            int vertexShader = compileVertexShader(vertexShaderSource);
            int fragmentShader = compileFragmentShader(fragmentShaderSource);

            program = linkProgram(vertexShader, fragmentShader);

            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

            return program;
        }



}