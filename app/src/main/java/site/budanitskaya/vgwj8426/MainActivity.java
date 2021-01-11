package site.budanitskaya.vgwj8426;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import app.com.sample.R;


public class MainActivity extends AppCompatActivity {

    // Create the object of TextView Class
    TextView txtCity, txtTemp, feelsLike, txthumidity, txt6, txt8;
    String cityID = "834463";
    String pictureName;
    String string_url;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        feelsLike = findViewById(R.id.feelsLike);
        txthumidity = findViewById(R.id.txthumidity);
        imgIcon = findViewById(R.id.imgIcon);

        fetchdataString();

        /* api key: df2b400ef7cce7316bace9617dc1b602 */

        Log.d("onResponse", "onResponse: String");

        MyTask mt = new MyTask();
        mt.execute();
    }


    void fetchdataString() {

        String url = "https://api.openweathermap.org/data/2.5/weather?id=625143&appid=df2b400ef7cce7316bace9617dc1b602";


        StringRequest request
                = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object1
                                    = new JSONObject(
                                    response);

                            JSONObject object2
                                    = object1.getJSONObject("main");

                            JSONArray object3
                                    = object1.getJSONArray("weather");

                            JSONObject o = object3.getJSONObject(0);

                            pictureName = o.getString("icon");


                            string_url = "https://openweathermap.org/img/wn/" + pictureName + "@2x.png";

                            Log.d("onResponse", "onResponse: " + pictureName);

                            String temp = object2.getString(
                                    "temp");

                            txtTemp.setText(Math.round(Float.parseFloat(temp.replace(',', '.')) - 273) + " °C");

                            String feel_like = object2.getString(
                                    "feels_like");

                            feelsLike.setText(Math.round(Float.parseFloat(feel_like.replace(',', '.')) - 273) + " °C");

                            String humidity = object2.getString(
                                    "humidity");

                            txthumidity.setText(humidity + " %");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                MainActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

        RequestQueue r1 = Volley.newRequestQueue(MainActivity.this.getApplicationContext());

        r1.add(request);
        r1.start();

    }

    void fetchdataBitmap() {


        ImageRequest imgRequest = new ImageRequest(string_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imgIcon.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        RequestQueue r2 = Volley.newRequestQueue(MainActivity.this.getApplicationContext());

        r2.add(imgRequest);
        r2.start();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchdataString();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            fetchdataBitmap();
        }
    }
}