package algonquin.cst2335.ayisat;

import static java.net.URLEncoder.encode;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import algonquin.cst2335.ayisat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    protected RequestQueue queue = null;
    protected String cityName;
    final protected  String API_KEY="31e20d886922cc1856308ed89aae685d";
    private ImageLoader imageLoader;
    private JSONObject response;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.btnGet.setOnClickListener(e->{


            cityName = binding.edtCity.getText().toString();

            queue = Volley.newRequestQueue(this);

            imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String url) {
                    return null;
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    // No need to implement in this example
                }
            });

            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + encode(cityName,  "UTF-8") + "&appid="+API_KEY+ "&units=metric";
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }


            Log.w("mervat1", stringURL);



            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                        JSONObject mainObject = null;

                        JSONObject weatherObject = null;

                        double current ;
                        double min ;
                        double max ;
                        int humidity ;

                        String iconCode;

                        try {

                            mainObject = response.getJSONObject("main");
                            current = mainObject.getDouble("temp");
                            min = mainObject.getDouble("temp_min");
                            max = mainObject.getDouble("temp_max");
                            humidity = mainObject.getInt("humidity");

                            JSONArray weatherArray = response.getJSONArray("weather");
                            if (weatherArray.length() > 0) {
                                weatherObject = weatherArray.getJSONObject(0);
                                iconCode = weatherObject.getString("icon");
                                String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
                                ImageLoader.ImageListener listener = ImageLoader.getImageListener(binding.img, 0, 0);
                                imageLoader.get(iconUrl, listener);

                            }


                        } catch (JSONException ex) {
                            Log.w("mervat","4");
                            throw new RuntimeException(ex);
                        }


                        runOnUiThread( ( ) -> {
                            binding.temp.setText("The current temperature is:  " + current);
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.minTemp.setText("The min temperature is:  " + min);
                            binding.minTemp.setVisibility(View.VISIBLE);
                            binding.maxTemp.setText("The max temperature is:  " + max);
                            binding.maxTemp.setVisibility(View.VISIBLE);
                            binding.humidityTemp.setText("The humidity temperature is:  " + humidity);
                            binding.humidityTemp.setVisibility(View.VISIBLE);
                            // do this for all the textViews...
                        });

                    }, (error) -> { });
            queue.add (request);




        });

    }
}