package com.example.keresoro.madass;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    ArrayList<ListRestaurants> resto = new ArrayList<>();
    double latitude;
    double longitude;

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);


        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mv.getController().setCenter(new GeoPoint(latitude, longitude));
        Toast.makeText
                (this, "Your Location is" +
                        latitude + " " +
                        longitude, Toast.LENGTH_LONG).show();

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            public boolean onItemLongPress(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        mv.getOverlays().add(items);

    }

    public void onLocationChanged(Location newLoc) {
        latitude = Double.parseDouble(String.valueOf(newLoc.getLatitude()));
        longitude = Double.parseDouble(String.valueOf(newLoc.getLongitude()));
        mv.getController().setCenter(new GeoPoint(latitude, longitude));
        Toast.makeText
                (this, "Location=" +
                        latitude + " " +
                        longitude, Toast.LENGTH_LONG).show();
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AddRest) {
            Intent intent = new Intent(this, AddRestaurants.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.SaveRest) {
            OptionSave save = new OptionSave();
            save.execute();
            return true;
        } else if (item.getItemId() == R.id.LoadRest) {
            OptionLoad load = new OptionLoad();
            load.execute();
            mv.getOverlays().add(items);
            return true;
        } else if (item.getItemId() == R.id.savePref) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            setResult(RESULT_OK, intent);
            startActivityForResult(intent, 1);
            return true;
        } else if (item.getItemId() == R.id.loadFromWeb) {
            LoadFromWeb loadweb = new LoadFromWeb();
            loadweb.execute();
            return true;
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String name = extras.getString("com.example.restname");
                String address = extras.getString("com.example.raddress");
                String cusine = extras.getString("com.example.cusine");
                int rating = extras.getInt("com.example.rating");

                //Adding restaurants to arraylist
                resto.add(new ListRestaurants(name, address, cusine, rating, latitude, longitude));
                OverlayItem rests = new OverlayItem(name, "Name of Restaurant: " + name + " Address: " + address + "\n" + "Cusine: " + cusine + " Rating: " + rating, new GeoPoint(latitude, longitude));
                rests.setMarker(getResources().getDrawable(R.drawable.marker));
                items.addItem(rests);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean autoSave = prefs.getBoolean("savePreferences", true);
                if (autoSave == true) {
                    OptionSave savePrefs = new OptionSave();
                    savePrefs.execute();
                }
            }
        }
    }

    //save the restaurant to file

    class OptionSave extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... unused) {
            String message = "Succesfully saved the restaurants!";
            try {
                PrintWriter printWriter = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.csv", true));
                for (ListRestaurants r : resto) {
                    printWriter.println(r.toString());
                }
                printWriter.close();
            } catch (IOException e) {
                System.out.println("I/O Error: " + e);
            }
            return message;
        }

        public void onPostExecute(String message) {
            new AlertDialog.Builder(MainActivity.this).setMessage(message).setPositiveButton("OKAY", null).show();
        }
    }

    //loading the restaurants from file
    class OptionLoad extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... unused) {
            String message = "Succesfully loaded restaurants!";
            try {
                FileReader filereader = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.csv");
                BufferedReader reader = new BufferedReader(filereader);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] components = line.split(",");
                    if (components.length == 6) {
                        ListRestaurants thisrestaurant = new ListRestaurants(components[0], components[1], components[2],
                                Integer.parseInt(components[3]), Double.parseDouble(components[4]), Double.parseDouble(components[5]));
                        resto.add(thisrestaurant);
                        OverlayItem rests = new OverlayItem(thisrestaurant.Restname, "Name of Restaurant: " + thisrestaurant.Restname + " Address: " + thisrestaurant.Restaddress +
                                "\n" + "Cusine: " + thisrestaurant.Restcusine + " Rating: " + thisrestaurant.Restrating, new GeoPoint(thisrestaurant.latitude, thisrestaurant.longitude));
                        rests.setMarker(getResources().getDrawable(R.drawable.marker));
                        items.addItem(rests);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Input file data.csv could not be found!");
            } catch (IOException e) {
                System.out.println("ERROR: " + e);
            }
            return message;
        }

        public void onPostExecute(String message) {
            new AlertDialog.Builder(MainActivity.this).setMessage(message).setPositiveButton("OKAY", null).show();
        }
    }

    class LoadFromWeb extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... unused) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/get.php?year=18&username=user008&format=csv");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                if (conn.getResponseCode() == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] components = line.split(",");
                        if (components.length == 6) {
                            ListRestaurants restFromWeb = new ListRestaurants(components[0], components[2], components[1], Integer.parseInt(components[3]), Double.parseDouble(components[5]), Double.parseDouble(components[4]));
                            resto.add(restFromWeb);
                            //setting restaurant marker for current position
                            OverlayItem rests = new OverlayItem(restFromWeb.Restname, "Name of Restaurant: " + restFromWeb.Restname + " Address: " + restFromWeb.Restaddress +
                                    "\n" + "Cusine: " + restFromWeb.Restcusine + " Rating: " + restFromWeb.Restrating, new GeoPoint(restFromWeb.latitude, restFromWeb.longitude));
                            rests.setMarker(getResources().getDrawable(R.drawable.marker));
                            items.addItem(rests);
                        }
                    }
                    return "Successfully loaded from web";
                } else
                    return "HTTP ERROR: " + conn.getResponseCode();
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }
    }
}