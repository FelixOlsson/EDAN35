package android.ahaonline.com.edan35.Objects;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Felix on 2016-11-28.
 */

public class Model extends AbstractObject {

    private float vertexCoords[];
    private float uvCooords[];

    public Model() {

    }


    public void loadModel(Context context, int modelResourceId) {
        StringBuilder sb = new StringBuilder();

        Pattern vt = Pattern.compile("vt");
        Pattern vn = Pattern.compile("vn");
        Pattern v = Pattern.compile("v");

        ArrayList<Float> tempCoords = new ArrayList<Float>();

        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            context.getResources().openRawResource(modelResourceId)));

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                Scanner scanner = new Scanner(nextLine);
                if(scanner.hasNext(v)) {
                    System.out.println(nextLine);
                    //scanner.next();
                    System.out.println(scanner.next());
                    while(scanner.hasNext()) {
                        tempCoords.add(Float.valueOf(scanner.next()));
                    }
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while reading shader resource: " + modelResourceId, e);
        }
        System.out.println("start:");
        System.out.println(tempCoords.size());
        for(Float f : tempCoords) {
            System.out.println("coords: " + f);
        }
    }
}
