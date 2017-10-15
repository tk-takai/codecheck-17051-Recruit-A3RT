package mypackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyUtil {
    public static final String EOL = "\r\n";
    private static final String BOUNDARY = "---*#konasiddkasud#";
    public static final String TWO_HYPHENS = "--";

    public static String getImageInfluence(String apiKey, String predict, String imageFile) {
        // WebAPIのホストアドレス
        final String host = "https://api.a3rt.recruit-tech.co.jp/image_influence/v1/meat_score";
        // WebAPIから返ってくる文字列
        String result = "";
        try {
            // サーバへの接続を開始
            URL url = new URL(host);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);

            // サーバへのリクエストの準備
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            File file = new File(imageFile);
            FileInputStream fis = new FileInputStream(file);

            // サーバへのリクエストの序盤を記述
            StringBuilder builder = new StringBuilder();
            builder.append(TWO_HYPHENS + BOUNDARY + EOL);
            builder.append("Content-Disposition: form-data; name=\"apikey\"" + EOL);
            builder.append(EOL);
            builder.append(apiKey + EOL);
            builder.append(TWO_HYPHENS + BOUNDARY + EOL);
            builder.append("Content-Disposition: form-data; name=\"predict\"" + EOL);
            builder.append(EOL);
            builder.append(predict + EOL);
            builder.append(TWO_HYPHENS + BOUNDARY + EOL);
            builder.append("Content-Disposition: form-data; name=\"imagefile\"; filename=\"" + imageFile + "\"" + EOL);
            builder.append("Content-Type: image/jpeg" + EOL);
            builder.append(EOL);

            // 画像バイナリを一定サイズ読み取りつつサーバへのリクエストを開始
            con.setChunkedStreamingMode(0);
            out.writeBytes(builder.toString());
            int bytesAvailable = fis.available();
            int maxBufferSize = 1 * 1024 * 1024;
            int bufferSize = (bytesAvailable > maxBufferSize) ? maxBufferSize : bytesAvailable;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                out.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = (bytesAvailable > maxBufferSize) ? maxBufferSize : bytesAvailable;
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            // サーバへのリクエストを終了
            StringBuilder lastBuilder = new StringBuilder();
            lastBuilder.append(EOL + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + EOL);
            out.writeBytes(lastBuilder.toString());
            out.flush();
            out.close();

            // レスポンスを取得
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tmp = "";
                while ((tmp = in.readLine()) != null) {
                    result += tmp;
                }
                in.close();
            } else {
                result = "Server Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}