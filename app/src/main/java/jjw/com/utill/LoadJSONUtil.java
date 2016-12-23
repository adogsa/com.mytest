package jjw.com.utill;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JJW on 2016-10-26.
 */
public class LoadJSONUtil extends AsyncTask<String, Void, String> {
    public LoadJSONUtil(Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(String jsonData);

        void onError();
    }

    private Listener mListener;

    @Override
    protected String doInBackground(String... strings) {
        try {
            System.out.println("jjw : " + strings[0]   );

            return loadJSON(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        System.out.println("jjw : onPostExecute " + response);

        if (response != null) {

            mListener.onLoaded(response);

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        System.out.println("jjw loadJSON ");
        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();
        System.out.println("jjw response.toString() " + response.toString());
        return response.toString();
    }
}
