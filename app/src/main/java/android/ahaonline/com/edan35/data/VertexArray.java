package android.ahaonline.com.edan35.data;

/**
 * Created by Felix on 2016-12-06.
 */


import java.nio.FloatBuffer;

import static android.ahaonline.com.edan35.util.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {


        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(vertexData);
    }

    public void setVertAttrib(int offset, int location, int count, int stride) {
        floatBuffer.position(offset);
        glVertexAttribPointer(location, count, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(location);

        floatBuffer.position(0);
    }

    public void updateBuffer(float[] data, int start, int count) {
        floatBuffer.position(start);
        floatBuffer.put(data, start, count);
        floatBuffer.position(0);
    }
}