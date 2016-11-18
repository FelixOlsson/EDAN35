package android.ahaonline.com.edan35.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glGenBuffers;

/**
 * Created by felix on 17/11/2016.
 */
public class VertexBuffer {
    //private final int bufferId;
    private final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexArray;

    public VertexBuffer(float[] vertexData) {


        vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexArray.position(0);
    }

    public FloatBuffer getVertexBuffer() {
        return vertexArray;
    }
}
