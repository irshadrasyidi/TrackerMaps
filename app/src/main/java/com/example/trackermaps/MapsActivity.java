package com.example.trackermaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static android.graphics.Color.BLUE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager lm;
    private LocationListener ll;
    private GoogleMap mMap;

    private ArrayList<LatLng> points;
    Polyline line;

    private class lokasiListener implements LocationListener{
        private TextView txtLat, txtLong;

        @Override
        public void onLocationChanged(Location location) {

            txtLat = (TextView) findViewById(R.id.CurrentLat);
            txtLong = (TextView) findViewById(R.id.CurrentLong);
            txtLat.setText(String.valueOf(location.getLatitude()));
            txtLong.setText(String.valueOf(location.getLongitude()));
            Toast.makeText(getBaseContext(), "GPS capture", Toast.LENGTH_LONG).show();

            Double dblLat = Double.parseDouble(txtLat.getText().toString());
            Double dblLong = Double.parseDouble(txtLong.getText().toString());

            LatLng latLng = new LatLng(dblLat, dblLong);

            gotoPeta(dblLat, dblLong, 40);

            points.add(latLng);

            redrawLine();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        points = new ArrayList<LatLng>();

        LocationManager mylocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mylocationListener = new lokasiListener();

        assert mylocationManager != null;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        mylocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.000001f, mylocationListener);
        mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.000001f , mylocationListener);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

        }
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.2819705, 112.795323);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,10));
    }

    private void gotoPeta(Double lat, Double lng, float z){
        LatLng Lokasibaru = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in " + lat + ":" + lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru, z));
    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }

        line = mMap.addPolyline(options); //add Polyline
    }

}
