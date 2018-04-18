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


public class MainActivity extends Activity implements LocationListener
{
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    ArrayList<ListRestaurants> resto = new ArrayList<>();

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


        TextView lat = (TextView) findViewById(R.id.lat1);
        TextView valuelat = (TextView) findViewById(R.id.value1);
        TextView lon = (TextView) findViewById(R.id.lon1);
        TextView valuelon = (TextView) findViewById(R.id.value2);
        //valuelat.setText(lat.toString());
        //valuelon.setText(lon.toString());

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        OverlayItem fernhurst = new OverlayItem("Fernhurst", "the village of Fernhurst", new GeoPoint(51.05, -0.72));

        // NOTE is just this.getDrawable() if supporting API 21+ only
        fernhurst.setMarker(getResources().getDrawable(R.drawable.marker));
        items.addItem(fernhurst);
        items.addItem(new OverlayItem("Blackdown", "highest point in West Sussex", new GeoPoint(51.0581, -0.6897)));
        mv.getOverlays().add(items);



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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AddRest) {
            // react to the menu item being selected...
            Intent intent = new Intent(this, AddRestaurants.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return false;
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // retrieve values from AddMarker activity
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String name = extras.getString("com.example.restname");
                String address = extras.getString("com.example.raddress");
                String cusine = extras.getString("com.example.cusine");
                int rating = extras.getInt("com.example.rating");
        //Adding restaurants to arraylist

             resto.add(new ListRestaurants(name, address, cusine, rating, latitude, longitude ));
                OverlayItem rests = new OverlayItem(name +", address: " + address, cusine+" , rating:" +rating, new GeoPoint(latitude, longitude) );
            }
        }
    }
}
