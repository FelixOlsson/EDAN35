package android.ahaonline.com.edan35.data;

import android.ahaonline.com.edan35.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import static android.opengl.GLES30.*;
/**
 * Created by felix on 22/11/2016.
 */
public class IndexBuffer {
    private final int bufferId;
    public int numItems;

    public IndexBuffer(short[] indexData) {
        // Allocate a buffer.
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if(buffers[0] == 0) {
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        bufferId = buffers[0];

        // Bind to the buffer.
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        // Transfer data to native memory.

        ShortBuffer indexArray = ByteBuffer
                .allocateDirect(indexData.length * Constants.BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexArray.position(0);

        // Transfer data from memory to GPU buffer.
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity()
                * Constants.BYTES_PER_SHORT, indexArray, GL_STREAM_DRAW);

        // IMPORTANT: Unbind from buffer when we're done with it
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBufferId() {
        return bufferId;
    }

}

