package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Felix on 2016-11-28.
 */

public class Model extends AbstractObject {

    private VertexBuffer vertexBuffer;
    private VertexBuffer vertexBufferColor;
    private VertexBuffer vertexBufferCoords;
    private VertexBuffer vertexBufferNormals;

    private Context context;

    private float vertexCoords[];
    private float uvCooords[];
    private float normals[];

    public Model() {
        setIdentityM(modelMatrix, 0);
    }


    public void loadModel(Context context, int modelResourceId) {
        this.context = context;

        StringBuilder sb = new StringBuilder();

        Pattern vt = Pattern.compile("vt");
        Pattern vn = Pattern.compile("vn");
        Pattern v = Pattern.compile("v");
        Pattern f = Pattern.compile("f");
        Pattern not = Pattern.compile("/");

        ArrayList<ArrayList<Float>> tempCoords = new ArrayList<ArrayList<Float>>();
        ArrayList<ArrayList<Float>> tempTexCoords = new ArrayList<ArrayList<Float>>();
        ArrayList<ArrayList<Float>> tempNormalCoords = new ArrayList<ArrayList<Float>>();
        ArrayList<Integer> tempIndexVerCoords = new ArrayList<Integer>();
        ArrayList<Integer> tempIndexTexCoords = new ArrayList<Integer>();
        ArrayList<Integer> tempIndexNormCoords = new ArrayList<Integer>();



        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            context.getResources().openRawResource(modelResourceId)));

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                Scanner scanner = new Scanner(nextLine);
                  if(scanner.hasNext(vt)) {
                      scanner.next();
                      ArrayList<Float> temp1 = new ArrayList<Float>();
                      while(scanner.hasNext()) {
                        temp1.add(Float.valueOf(scanner.next()));
                      }

                      tempTexCoords.add(temp1);
                  } else if (scanner.hasNext(vn)) {
                          scanner.next();
                          ArrayList<Float> temp1 = new ArrayList<Float>();
                          while(scanner.hasNext()) {
                              temp1.add(Float.valueOf(scanner.next()));
                          }

                          tempNormalCoords.add(temp1);
                  } else if(scanner.hasNext(f)) {
                      scanner.next();
                    while(scanner.hasNext()) {
                        String temp = scanner.next();
                        String test[] = temp.split("/");
                        tempIndexVerCoords.add(Integer.valueOf(test[0]));
                        tempIndexTexCoords.add(Integer.valueOf(test[1]));
                        tempIndexNormCoords.add(Integer.valueOf(test[2]));

                    }
                  } else if(scanner.hasNext(v)) {
                      scanner.next();
                      ArrayList<Float> temp1 = new ArrayList<Float>();
                      while(scanner.hasNext()) {
                          temp1.add(Float.valueOf(scanner.next()));
                      }

                      tempCoords.add(temp1);
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while loading model: " + modelResourceId, e);
        }


        vertexCoords = toFloatArray(toIndexedArrayList(tempIndexVerCoords, tempCoords));
        uvCooords = toFloatArray(toIndexedArrayList(tempIndexTexCoords, tempTexCoords));
        normals = toFloatArray(toIndexedArrayList(tempIndexNormCoords, tempNormalCoords));

        vertexBuffer = new VertexBuffer(vertexCoords);
        vertexBufferCoords = new VertexBuffer(uvCooords);
        vertexBufferNormals = new VertexBuffer(normals);
    }

    public void bindShader(TextureShaderProgram shaderTestProgram) {
        vertexBuffer.setVertexAttribPointer(0,
                shaderTestProgram.getPositionAttributeLocation(),
                3, 0);

        vertexBufferCoords.setVertexAttribPointer(0,
                shaderTestProgram.getTextureCoordinatesAttributeLocation(),
                2, 0);

        vertexBufferNormals.setVertexAttribPointer(0,
                shaderTestProgram.getNormalAttributeLocation(),
                3, 0);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,vertexCoords.length);
    }

    public float[] toFloatArray(ArrayList<Float> arrayList) {
        float[] arrayOfFloats = new float[arrayList.size()];

        for(int i = 0; i < arrayList.size(); ++i) {
            arrayOfFloats[i] = arrayList.get(i);
        }

        return arrayOfFloats;
    }

    public ArrayList<Float> toIndexedArrayList(ArrayList<Integer> index, ArrayList<ArrayList<Float>> al) {
        ArrayList<Float> newIndexedList = new ArrayList<Float>();

        for(Integer i : index) {
            for(Float tf : al.get(i - 1)) {
                newIndexedList.add(tf);
            }
        }

        return newIndexedList;
    }
}
