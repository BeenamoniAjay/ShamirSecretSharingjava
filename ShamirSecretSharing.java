import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        String filePath = "path_to_your_json_file.json";
        try {
            JSONObject data = readInput(filePath);
            List<int[]> decodedValues = decodeYValues(data);
            double secretC = findSecretC(decodedValues);
            System.out.println("Secret (c): " + secretC);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readInput(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(filePath));
    }

    public static int decodeValue(int base, String value) {
        return Integer.parseInt(value, base);
    }

    public static List<int[]> decodeYValues(JSONObject data) {
        List<int[]> decodedValues = new ArrayList<>();
        for (Object key : data.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt((String) key);
                JSONObject valueObj = (JSONObject) data.get(key);
                int base = Integer.parseInt((String) valueObj.get("base"));
                String value = (String) valueObj.get("value");
                int y = decodeValue(base, value);
                decodedValues.add(new int[]{x, y});
            }
        }
        return decodedValues;
    }

    public static double lagrangeInterpolation(List<int[]> points, int x) {
        double result = 0.0;
        int n = points.size();
        
        for (int i = 0; i < n; i++) {
            int xi = points.get(i)[0];
            int yi = points.get(i)[1];
            
            double term = yi;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    term *= (double) (x - xj) / (xi - xj);
                }
            }
            result += term;
        }
        
        return result;
    }

    public static double findSecretC(List<int[]> decodedValues) {
        return lagrangeInterpolation(decodedValues, 0);
    }
}
