import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class YandexAPI {
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private static final String API_KEY = "12345";
    private static final double LAT = 55.75;
    private static final double LON = 37.62;
    private static final int LIMIT = 7;

    public static void main(String[] args) {
        try {
            String urlString = API_URL + "?lat=" + LAT + "&lon=" + LON + "&limit=" + LIMIT;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println("Полный ответ JSON: " + content.toString());

            JSONObject jsonResponse = new JSONObject(content.toString());
            double factTemp = jsonResponse.getJSONObject("fact").getDouble("temp");
            System.out.println("Текущая температура: " + factTemp + "°C");

            double totalTemp = 0;
            for (int i = 0; i < LIMIT; i++) {
                totalTemp += jsonResponse.getJSONArray("forecasts")
                        .getJSONObject(i)
                        .getJSONObject("parts")
                        .getJSONObject("day")
                        .getDouble("temp_avg");
            }
            double averageTemp = totalTemp / LIMIT;
            System.out.println("Средняя температура за " + LIMIT + " дней: " + averageTemp + "°C");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

