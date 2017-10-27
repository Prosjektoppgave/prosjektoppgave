package functions;

import java.io.InputStream;
import java.net.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import classes.Station;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Distance {

    public static void getDrivingTimes(HashMap<Integer, Station> stations) throws IOException, JSONException {

        for (Station origin: stations.values()) {
            for (Station destination: stations.values()) {
                if(origin.getId() != destination.getId()) {
                    int drivingTimeSek = getDrivingTimeBetweenCoordinates(origin, destination);
                    double drivingTimeMin = ((double) drivingTimeSek)/60;
                    origin.addLengthToStation(destination.getId(), drivingTimeMin);
                }
            }
        }
    }

    private static int getDrivingTimeBetweenCoordinates(Station origin, Station destination) throws IOException, JSONException{
        double originLongitude = origin.getLongitude();
        double originLatitude = origin.getLatitude();
        double destinationLongitude = destination.getLongitude();
        double destinationLatitude = destination.getLatitude();
        String urlRequestGoogleMaps = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + originLatitude + "," + originLongitude + "&destinations=" + destinationLatitude + "," + destinationLongitude + "&mode=driving&language=en-EN&sensor=false";
        JSONObject json = readJsonFromUrl(urlRequestGoogleMaps);
        return getDrivingDurationAsInt(json);
    }

    private static JSONObject readJsonFromUrl(String urlRequestGoogleMaps) throws IOException, JSONException {
        InputStream is = new URL(urlRequestGoogleMaps).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static int getDrivingDurationAsInt(JSONObject json) throws JSONException{
        JSONArray rows = json.getJSONArray("rows");
        JSONObject firstObjectRows = rows.getJSONObject(0);
        JSONArray elements = firstObjectRows.getJSONArray("elements");
        JSONObject firstObjectElements = elements.getJSONObject(0);
        JSONObject duration = firstObjectElements.getJSONObject("duration");
        return Integer.parseInt(duration.get("value").toString());
    }
}





