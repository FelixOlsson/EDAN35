package android.ahaonline.com.edan35.programs;

import android.content.Context;
import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by felix on 17/11/2016.
 */
public class ShaderBuilder {


        /**
         * Loads and compiles a vertex shader, returning the OpenGL object ID.
         */
        public static int compileVertexShader(String shaderCode) {
            return compileShader(GL_VERTEX_SHADER, shaderCode);
        }


        /**
         * Loads and compiles a fragment shader, returning the OpenGL object ID.
         */
        public static int compileFragmentShader(String shaderCode) {
            return compileShader(GL_FRAGMENT_SHADER, shaderCode);
        }

        private static int compileShader(int type, String shaderCode) {

            // Create a new shader object.
            final int shaderObjectId = glCreateShader(type);


            // Pass in the shader source.
            glShaderSource(shaderObjectId, shaderCode);

            // Compile the shader.
            glCompileShader(shaderObjectId);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);


            // Verify the compile status.
            if (compileStatus[0] == 0) {
                // If it failed, delete the shader object.
                glDeleteShader(shaderObjectId);


                return 0;
            }

            // Return the shader object ID.
            return shaderObjectId;

        }

        /**
         * Links a vertex shader and a fragment shader together into an OpenGL
         * program. Returns the OpenGL program object ID, or 0 if linking failed.
         */
        public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

            // Create a new program object.
            final int programObjectId = glCreateProgram();


            // Attach the vertex shader to the program.
            glAttachShader(programObjectId, vertexShaderId);
            // Attach the fragment shader to the program.
            glAttachShader(programObjectId, fragmentShaderId);

            // Link the two shaders together into a program.
            glLinkProgram(programObjectId);

            // Get the link status.
            final int[] linkStatus = new int[1];
            glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);


            // Verify the link status.
            if (linkStatus[0] == 0) {
                // If it failed, delete the program object.
                glDeleteProgram(programObjectId);

                return 0;
            }

            // Return the program object ID.
            return programObjectId;
        }

        /**
         * Validates an OpenGL program. Should only be called when developing the
         * application.
         */
        public static boolean validateProgram(int programObjectId) {
            glValidateProgram(programObjectId);

            final int[] validateStatus = new int[1];
            glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);


            return validateStatus[0] != 0;
        }

        public static int buildProgram(String vertexShaderSource,
                                       String fragmentShaderSource) {
            int program;
            // Compile the shaders.
            int vertexShader = compileVertexShader(vertexShaderSource);
            int fragmentShader = compileFragmentShader(fragmentShaderSource);
            // Link them into a shader program.
            program = linkProgram(vertexShader, fragmentShader);

            return program;
        }



}