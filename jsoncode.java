import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PolynomialSolver {
    public static void main(String[] args) throws FileNotFoundException, JSONException {
        String[] testCases = {"testcase1.json", "testcase2.json"};

        for (String testCase : testCases) {
            JSONObject input = readJsonFile(testCase);
            int n = input.getJSONObject("keys").getInt("n");
            int k = input.getJSONObject("keys").getInt("k");
            int[][] points = new int[n][2];

            for (int i = 1; i <= n; i++) {
                JSONObject point = input.getJSONObject(String.valueOf(i));
                int x = i;
                int y = decodeValue(point.getString("value"), point.getInt("base"));
                points[i - 1][0] = x;
                points[i - 1][1] = y;
            }

            long secret = calculateSecret(points, k - 1);
            System.out.println("Secret for " + testCase + ": " + secret);
        }
    }

    private static JSONObject readJsonFile(String filename) throws FileNotFoundException, JSONException {
        Scanner scanner = new Scanner(new File(filename));
        String json = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return new JSONObject(json);
    }

    private static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    private static long calculateSecret(int[][] points, int degree) {
        long[] coefficients = new long[degree + 1];
        coefficients[0] = lagrangeInterpolation(points, 0);
        return coefficients[0];
    }

    private static long lagrangeInterpolation(int[][] points, int index) {
        long result = 0;
        int n = points.length;
        for (int i = 0; i < n; i++) {
            if (i != index) {
                long numerator = 1;
                long denominator = 1;
                for (int j = 0; j < n; j++) {
                    if (j != index && j != i) {
                        numerator *= (points[j][0] - points[index][0]);
                        denominator *= (points[j][0] - points[i][0]);
                    }
                }
                result += points[i][1] * numerator / denominator;
            }
        }
        return result;
    }
}