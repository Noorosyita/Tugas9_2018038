package com.example.tugas6_alarmmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tugas6_alarmmanager.databinding.ActivityRestapiBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RestAPIMain extends AppCompatActivity implements
        View.OnClickListener {
    //declaration variable
    private ActivityRestapiBinding binding;
    String index;

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityRestapiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_alarm) {
                    Intent a = new Intent(RestAPIMain.this,
                            MainActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_lapangan) {
                    Intent a = new Intent(RestAPIMain.this,
                            DestinationActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_profile) {
                    Intent a = new Intent(RestAPIMain.this,
                            Profil1.class);
                    startActivity(a);
                } else if (id == R.id.nav_tambahdata) {
                    Intent a = new Intent(RestAPIMain.this,
                            TambahData.class);
                    startActivity(a);
                } else if(id ==R.id.nav_listlapangan){
                    Intent a = new Intent(RestAPIMain.this, RestAPIMain.class);
                    startActivity(a);
                }
                return true;
            }
        });
        binding.fetchButton.setOnClickListener(this);
    }

    //onclik button fetch
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fetch_button) {
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://run.mocky.io/v3/c31c9941-c4fa-4a1e-b5df-4391e6e64a70")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }

    class DOTask extends AsyncTask<URL, Void, String> {
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //get data json
        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject innerObj = jsonObject.getJSONObject("data");
            JSONArray cityArray = innerObj.getJSONArray("data");
            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("id").toString();
                if (Sobj.equals(index)) {
                    String id = obj.get("id").toString();
                    String name = obj.get("name").toString();
                    String description = obj.get("description").toString();
                    String location = obj.get("location").toString();
                    String price = obj.get("price").toString();
                    String time = obj.get("time").toString();
                    String rating = obj.get("rating").toString();

                    binding.resultId.setText(id);
                    binding.resultName.setText(name);
                    binding.resultDescription.setText(description);
                    binding.resultLocation.setText(location);
                    binding.resultPrice.setText(price);
                    binding.resultTime.setText(time);
                    binding.resultRating.setText(rating);

                    break;
                } else {
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}

