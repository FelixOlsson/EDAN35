package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.data.VertexBuffer;
import android.ahaonline.com.edan35.programs.TextureShaderProgram;
import android.content.Context;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by Felix on 2016-11-28.
 */

public class Model extends AbstractObject {

    private VertexBuffer vertexBuffer;
    private VertexBuffer vertexBufferColor;
    private VertexBuffer vertexBufferCoords;

    private Context context;

    private float vertexCoords[];
    private float uvCooords[];

    public Model() {

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
        ArrayList<Integer> tempIndexVerCoords = new ArrayList<Integer>();
        ArrayList<Integer> tempIndexTexCoords = new ArrayList<Integer>();

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
                } else if(scanner.hasNext(f)) {
                      scanner.next();
                    while(scanner.hasNext()) {
                        String temp = scanner.next();
                        String test[] = temp.split("/");
                        tempIndexVerCoords.add(Integer.valueOf(test[0]));
                        tempIndexTexCoords.add(Integer.valueOf(test[1]));
                        //tempIndexCoords.add(Integer.valueOf(test[2]));

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
                    "Error while reading shader resource: " + modelResourceId, e);
        }

        ArrayList<Float> newCoords = new ArrayList<Float>();
        ArrayList<Float> newUVCoords = new ArrayList<Float>();


        System.out.println(tempCoords.size());
        for(Integer i : tempIndexVerCoords) {

            for(Float tf : tempCoords.get(i - 1)) {
                newCoords.add(tf);
            }
            System.out.println("index: " + i);
            System.out.println("vertex" + tempCoords.get(i - 1));
        }

        for(Integer i : tempIndexTexCoords) {
            for(Float tf : tempTexCoords.get(i - 1)) {
                newUVCoords.add(tf);
            }
        }
        float[] arrResults = new float[newCoords.size()];

        for(int i = 0; i < newCoords.size(); ++i) {
            arrResults[i] = newCoords.get(i);
        }

        float[] arrResults2 = new float[newUVCoords.size()];

        for(int i = 0; i < newUVCoords.size(); ++i) {
            arrResults2[i] = newUVCoords.get( i);
        }

        float cubeCoords[] = {   // in counterclockwise order:
            -1.0f,-1.0f,-1.0f, // triangle 1 : begin
                    -1.0f,-1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f, // triangle 1 : end
                    1.0f, 1.0f,-1.0f, // triangle 2 : begin
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f,-1.0f, // triangle 2 : end
                    1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f,-1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f,-1.0f,
                    -1.0f, 1.0f,-1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f
        };

        vertexCoords = arrResults;
        uvCooords = arrResults2;

        vertexBuffer = new VertexBuffer(vertexCoords);
        // vertexBufferColor = new VertexBuffer(colorCoords);
        vertexBufferCoords = new VertexBuffer(uvCooords);
    }

    public void bindShader(TextureShaderProgram shaderTestProgram) {
        //GLES20.glUseProgram(program);
        vertexBuffer.setVertexAttribPointer(0,
                shaderTestProgram.getPositionAttributeLocation(),
                3, 0);

        vertexBufferCoords.setVertexAttribPointer(0,
                shaderTestProgram.getTextureCoordinatesAttributeLocation(),
                2, 0);


    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,vertexCoords.length);
    }
}
