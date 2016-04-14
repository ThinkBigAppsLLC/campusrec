package edu.fsu.campusrec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;

    private View v;
    private MapView map;
    private GoogleMap gMap;

    public MapFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.fragment_map, container, false);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        map = (MapView) v.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        return v;
    }

    @Override
    public void onResume() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Log.i("GoogleMapsApi", "MapReady");
        this.gMap = gMap;
        for(Facility fac : MainActivity.facilities) {
            gMap.addMarker(new MarkerOptions()
                    .title(fac.getName())
                    .snippet(fac.getStatus())
                    .position(fac.getLoc())
            );
        }
        map.onResume();
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.facilities.get(0).getLoc(), 5));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    //*****************************************
    // Google Api Client Required Methods
    //*****************************************

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("GoogleMapsApi", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
