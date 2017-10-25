package distance;

import java.io.InputStream;
import java.net.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Distance {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
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

    public static int getDrivingDurationBetweenCoordinates (double originLongitude, double originLatitude, double destinationLongitude, double destinationLatitude, int depatureTime) throws IOException, JSONException{
        String urlRequestGoogleMaps = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + originLongitude + "," + originLatitude + "&destinations=" + destinationLongitude + "," + destinationLatitude + "&departure_time=" + depatureTime + "&mode=driving&language=en-EN&sensor=false";
        JSONObject json = readJsonFromUrl(urlRequestGoogleMaps);
        return getDrivingDurationAsInt(json);
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



