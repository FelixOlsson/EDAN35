package android.ahaonline.com.edan35.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES30.*;
import static android.opengl.GLUtils.*;

import static android.R.attr.resource;

/**
 * Created by Felix on 2016-11-24.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resourceId) {

        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {

            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);

        if (bitmap == null) {


            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        glGenerateMipmap(GL_TEXTURE_2D);

        bitmap.recycle();

        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];

    }

    public static int loadCubeMap(Context context, int[] cubeResources) {

        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);
        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] =
                    BitmapFactory.decodeResource(context.getResources(),
                            cubeResources[i], options);

            if (cubeBitmaps[i] == null) {

                glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }

            texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, cubeBitmaps[i], 0);
        }


        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);


        glBindTexture(GL_TEXTURE_2D, 0);

        for (Bitmap bitmap : cubeBitmaps) {
            bitmap.recycle();
        }

        return textureObjectIds[0];
    }
}
