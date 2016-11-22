package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.Constants;
import android.ahaonline.com.edan35.R;
import android.ahaonline.com.edan35.data.IndexBuffer;
import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.ShaderTestProgram;
import android.content.Context;
import android.opengl.GLES20;
import android.util.FloatMath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.ahaonline.com.edan35.Constants.COORDS_PER_VERTEX;
import static android.opengl.GLES20.GL_SHORT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * Created by felix on 22/11/2016.
 */
public class Sphere {

    private float[] vertices;
    private float[] normals;
    private float[] texcoords;
    private short[] indices;

    Sphere(float radius, int rings, int sectors) {

        float R = (float)1. / (float) (rings - 1);
        float S = (float)1. / (float) (sectors - 1);
        int r, s;

        vertices = new float[(rings * sectors * 3)];
        normals = new float[(rings * sectors * 3)];
        texcoords = new float[(rings * sectors * 2)];
        /*std::vector < GLfloat >::iterator v = vertices.begin();
        std::vector < GLfloat >::iterator n = normals.begin();
        std::vector < GLfloat >::iterator t = texcoords.begin();*/
        int indexT = 0;
        int indexV = 0;
        int indexN = 0;

        for (r = 0; r < rings; r++)
            for (s = 0; s < sectors; s++) {
                float  y = (float) Math.sin(-Math.PI/2 + Math.PI * r * R);
                float  x = (float) (Math.cos(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R));
                float  z = (float) (Math.sin(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R));

                texcoords[indexT++] = s * S;
                texcoords[indexT++]  = r * R;

                vertices[indexV++] = x * radius;
                vertices[indexV++] = y * radius;
                vertices[indexV++] = z * radius;

                normals[indexN++] = -x;
                normals[indexN++] = -y;
                normals[indexN++] = -z;
            }

        indices = new short[(rings * sectors * 4)];
        //std::vector < GLushort >::iterator i = indices.begin();
        int indexI = 0;
        for (r = 0; r < rings - 1; r++)
            for (s = 0; s < sectors - 1; s++) {

                indices[indexI++] = (short)((r + 1) * sectors + s);
                indices[indexI++] = (short )((r + 1) * sectors + (s + 1));
                indices[indexI++] = (short) (r * sectors + (s + 1));
                indices[indexI++] = (short) (r * sectors + s);

            }
    }


}

