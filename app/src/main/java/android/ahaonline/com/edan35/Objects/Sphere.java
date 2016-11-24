package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.Constants;
import android.ahaonline.com.edan35.R;
import android.ahaonline.com.edan35.data.IndexBuffer;
import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.content.Context;
import android.opengl.GLES20;
import android.util.FloatMath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.ahaonline.com.edan35.Constants.COORDS_PER_VERTEX;
import static android.ahaonline.com.edan35.Objects.Cube.cubeCoords;
import static android.opengl.GLES20.GL_SHORT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glDrawElements;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * Created by felix on 22/11/2016.
 */
public class Sphere extends AbstractObject {

    private float[] vertices;
    private float[] normals;
    private float[] texcoords;
    private int[] indices;

    private VertexBuffer vertexBuffer;
    private VertexBuffer vertexBufferColor;
    private VertexBuffer vertexBufferTexture;

    private Context context;

    private final int vertexCount;
    private final IntBuffer indexArray;


    public Sphere(float radius, int latitudeBands, int longitudeBands, Context context) {

        vertices = new float[((latitudeBands + 1) * (longitudeBands + 1) * 3 * 2)];
        normals = new float[((latitudeBands + 1) * (longitudeBands + 1) * 3)];
        texcoords = new float[((latitudeBands + 1) * (longitudeBands + 1) * 2)];

        vertexCount = vertices.length / COORDS_PER_VERTEX;

        int indexT = 0;
        int indexV = 0;
        int indexN = 0;

        for (int latNumber  = 0; latNumber  <= latitudeBands; latNumber ++) {

            float theta = (float) (latNumber * Math.PI / latitudeBands);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                float phi = (float) (longNumber * 2 * Math.PI / longitudeBands);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float x = cosPhi * sinTheta;
                float y = cosTheta;
                float z = sinPhi * sinTheta;
                float u = 1 - (longNumber / longitudeBands);
                float v = 1 - (latNumber / latitudeBands);

                normals[indexN++] = x;
                normals[indexN++] = y;
                normals[indexN++] = z;

                vertices[indexV++] = radius * x;
                vertices[indexV++] = radius * y;
                vertices[indexV++] = radius * z;

                vertices[indexV++] = u;
                vertices[indexV++] = v;




            }
        }

        indices = new int[((latitudeBands + 1) * (longitudeBands + 1) * 6)];
        int indexI = 0;
        for (int latNumber = 0; latNumber <= latitudeBands ; latNumber++) {
            for (int longNumber = 0; longNumber <= longitudeBands - 1 ; longNumber++) {

                int first = (latNumber * (longitudeBands + 1)) + longNumber;
                int second = first + longitudeBands + 1;

                indices[indexI++] = first;
                indices[indexI++] = second;
                indices[indexI++] = first + 1;

                indices[indexI++] = second;
                indices[indexI++] = second + 1;
                indices[indexI++] = first + 1;

            }
        }

        this.context = context;

        vertexBuffer = new VertexBuffer(vertices);
        vertexBufferColor = new VertexBuffer(vertices);
        vertexBufferTexture = new VertexBuffer(texcoords);

        indexArray = IntBuffer.allocate(indices.length).put(indices);

        indexArray.position(0);

    }

   /* public void bindShader(ShaderTestProgram shaderTestProgram) {
        //GLES20.glUseProgram(program);
        vertexBuffer.setVertexAttribPointer(0,
                shaderTestProgram.getPositionAttributeLocation(),
                COORDS_PER_VERTEX, 0);

        vertexBufferColor.setVertexAttribPointer(0,
                shaderTestProgram.getColorAttributeLocation(),
                COORDS_PER_VERTEX, 0);


    }*/

    public void bindShader(TextureShaderProgram shaderTestProgram) {
        //GLES20.glUseProgram(program);
        vertexBuffer.setVertexAttribPointer(0,
                shaderTestProgram.getPositionAttributeLocation(),
                3, 5 * 4);

        vertexBuffer.setVertexAttribPointer(3,
                shaderTestProgram.getaTextureCoordinatesAttributeLocation(),
                2, 5 * 4);


    }

    public void draw() {
        GLES20.glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, indexArray);
    }


}

