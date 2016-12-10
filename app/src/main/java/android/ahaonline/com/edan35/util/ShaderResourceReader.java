package android.ahaonline.com.edan35.util;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by felix on 15/11/2016.
 */
public class ShaderResourceReader {
    /**
     * Returns a string representation of a shader resource
     */
    public static String readShaderFromResource(Context context,
                                                int resourceId) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            context.getResources().openRawResource(resourceId)));

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                sb.append(nextLine);
                sb.append('\n');
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while reading shader resource: " + resourceId, e);
        }

        return sb.toString();
    }
}

