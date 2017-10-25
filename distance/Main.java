package distance;

import org.json.JSONException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JSONException{

        int durationsSeconds = Distance.getDrivingDurationBetweenCoordinates(54.406505, 18.67708, 54.446251, 18.570993, 20000);
        System.out.println(durationsSeconds);
    }

}