package android.ahaonline.com.edan35.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.ahaonline.com.edan35.util.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES30.*;
/**
 * Created by felix on 17/11/2016.
 */
public class VertexBuffer {

    private final int bufferId;

    public VertexBuffer(float[] vertexData) {

        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);

        bufferId = buffers[0];

        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);


        FloatBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexArray.position(0);

        glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT,
                vertexArray, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * @param dataOffset
     * @param attributeLocation
     * @param componentCount
     * @param stride
     */
    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride) {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);

        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, dataOffset);
        glEnableVertexAttribArray(attributeLocation);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
