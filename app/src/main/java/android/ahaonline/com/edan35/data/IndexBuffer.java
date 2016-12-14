package android.ahaonline.com.edan35.data;

import android.ahaonline.com.edan35.util.Constants;

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
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);

        bufferId = buffers[0];

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        ShortBuffer indexArray = ByteBuffer
                .allocateDirect(indexData.length * Constants.BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexArray.position(0);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity()
                * Constants.BYTES_PER_SHORT, indexArray, GL_STREAM_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBufferId() {
        return bufferId;
    }

}

