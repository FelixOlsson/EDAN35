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

        // Allocate a buffer.
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);

        if(buffers[0] == 0) {
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        bufferId = buffers[0];

        // Bind to the buffer.
        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);


        FloatBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexArray.position(0);

        // Transfer data from memory to GPU buffer.
        glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT,
                vertexArray, GL_STATIC_DRAW);

        // Unbinding the buffer
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
        //Rebinding the buffer
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);

        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, dataOffset);
        glEnableVertexAttribArray(attributeLocation);

        //Unbinding the buffer
        glBindBuffer(GL_ARRAY_BUFFER, 0);


    }
}
