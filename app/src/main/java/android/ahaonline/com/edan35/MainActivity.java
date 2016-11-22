package android.ahaonline.com.edan35;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MyGLRenderer mRenderer = new MyGLRenderer(this);
        mGLView = new GLSurfaceView(this);

        mGLView.setEGLContextClientVersion(2);

        mGLView.setRenderer(mRenderer);



        mGLView.setOnTouchListener(new View.OnTouchListener() {
            float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event != null) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = event.getX();
                        previousY = event.getY();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        final float deltaX = event.getX() - previousX;
                        final float deltaY = event.getY() - previousY;

                        previousX = event.getX();
                        previousY = event.getY();

                        mGLView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mRenderer.handleTouchDrag(
                                        deltaX, deltaY);

                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }

        });

        setContentView(mGLView);
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
