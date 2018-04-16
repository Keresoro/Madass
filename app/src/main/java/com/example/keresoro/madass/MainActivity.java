package com.example.keresoro.madass;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends Activity implements LocationListener
{
    MapView mv;

    Double latitude = 50.9;
    Double longitude = -1.4;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(latitude, longitude));

        TextView lat = (TextView) findViewById(R.id.lat1);
        TextView valuelat = (TextView) findViewById(R.id.value1);
        TextView lon = (TextView) findViewById(R.id.lon1);
        TextView valuelon = (TextView) findViewById(R.id.value2);
        //valuelat.setText(lat.toString());
        //valuelon.setText(lon.toString());
    }


    public void onLocationChanged(Location newLoc)
    {
        mv.getController().setCenter(new GeoPoint(newLoc.getLatitude(), newLoc.getLongitude()));
        latitude = Double.parseDouble(String.valueOf(newLoc.getLatitude()));
        longitude = Double.parseDouble(String.valueOf(newLoc.getLongitude()));
        Toast.makeText
                (this, "Location=" +
                        newLoc.getLatitude()+ " " +
                        newLoc.getLongitude() , Toast.LENGTH_LONG).show();
    }

    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider,int status,Bundle extras)
    {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.AddRest)
        {
            // react to the menu item being selected...
            return true;
        }
        return false;
    }



}
