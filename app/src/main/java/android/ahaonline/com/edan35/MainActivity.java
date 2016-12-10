package android.ahaonline.com.edan35;

import android.app.Dialog;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dialog loading_dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        loading_dialog.create();
        loading_dialog.setContentView(R.layout.loader);
        loading_dialog.show();


        final Renderer mRenderer = new Renderer(this, loading_dialog);
        setContentView(R.layout.activity_main);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.activity_main);
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mRenderer);
        r.addView(mGLView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
