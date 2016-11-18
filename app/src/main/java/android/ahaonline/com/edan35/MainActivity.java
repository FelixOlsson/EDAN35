package android.ahaonline.com.edan35;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer(context);

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
            mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
            mGLView.onResume();
    }
}
