package com.example.keresoro.madass;

/**
 * Created by Keresoro on 15/04/2018.
 */
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public class AddRestaurants extends AppCompatActivity implements OnClickListener
{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurants);

        Button AddRest = (Button)findViewById(R.id.xaddrest);
        AddRest.setOnClickListener(this);
    }
    public void onClick(View view)
    {
        //// Edit Texts initialisation
        TextView restname = (TextView) findViewById(R.id.xrestname);
        EditText editname = (EditText) findViewById(R.id.xeditname);
        TextView restad = (TextView) findViewById(R.id.xrestad);
        EditText editrestad = (EditText) findViewById(R.id.xeditrestad);
        TextView cusine = (TextView) findViewById(R.id.xcusine);
        EditText editcusine = (EditText) findViewById(R.id.xeditcusine);
        TextView rating = (TextView) findViewById(R.id.xrating);
        EditText editrating = (EditText) findViewById(R.id.xeditrating);

        //Get everything into variables
        String namerest = editname.getText().toString();
        String adrest = editrestad.getText().toString();
        String scusine = editcusine.getText().toString();
        int srating = Integer.parseInt(editrating.getText().toString());

        if (srating >10 || srating <0 ) {
            new AlertDialog.Builder(this).setMessage("Enter Value from 1-10 only!").
                    setPositiveButton("OK", null).show();
        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            // Send variables to main activity via bundle
            bundle.putString("com.example.restname",namerest);
            bundle.putString("com.example.raddress", adrest);
            bundle.putString("com.example.cusine", scusine);
            bundle.putInt("com.example.rating", srating);

            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
