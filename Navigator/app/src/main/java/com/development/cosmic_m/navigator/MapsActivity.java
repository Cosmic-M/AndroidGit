package com.development.cosmic_m.navigator;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.development.cosmic_m.navigator.Modules.DirectionFinder;
import com.development.cosmic_m.navigator.Modules.DirectionFinderListener;
import com.development.cosmic_m.navigator.Modules.MemoryPlace;
import com.development.cosmic_m.navigator.Modules.Route;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.google.android.gms.maps.GoogleMap.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, OnMarkerClickListener {

    private static final String TAG = "TAG";
    private static final int REQUEST_NEW_POINT = 250;
    private GoogleMap mMap;
    private EditText mOrigin;
    private EditText mDestination;

    private LatLng mDestinationPoint;
    private Marker mDestinationMarker;
    private List<LatLng> mTransitionPoints = new ArrayList<>();
    private List<MemoryPlace> mListSavedLocations;
    private RelativeLayout mContainerMini;
    private Button mFindPath;
    private Button mShowAllPlaces;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LatLng myLocation;
    private LocationManager locationManager;
    private ImageView mImage;
    private ImageView mHidePicture;
    private ImageView mTargetBtn;
    private ImageView mTransitBtn;
    private ChoiceDialog choiceDialog;

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG, "onSaveInstanceState(Bundle saveInstanceState)");
        saveInstanceState.putParcelable("destination", mDestinationPoint);
        saveInstanceState.putParcelable("myLocation", myLocation);
        saveInstanceState.putParcelableArrayList("latlng", (ArrayList<? extends Parcelable>) mTransitionPoints);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate(Bundle savedInstanceState)");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState != null => TRUE");
            mDestinationPoint = savedInstanceState.getParcelable("destination");
            myLocation = savedInstanceState.getParcelable("myLocation");
            mTransitionPoints = savedInstanceState.getParcelableArrayList("latlng");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFindPath = (Button) findViewById(R.id.btnFindPath);
        mShowAllPlaces = (Button) findViewById(R.id.showAllPlaces);
        mContainerMini = (RelativeLayout) findViewById(R.id.container_for_mini_id);
        mImage = (ImageView) findViewById(R.id.image_id);
        mTargetBtn = (ImageView) findViewById(R.id.btn_target);
        mTransitBtn = (ImageView) findViewById(R.id.btn_transit);
        mHidePicture = (ImageView) findViewById(R.id.btn_remove_picture);
        mTargetBtn.setBackgroundResource(R.mipmap.finish_flag);
        mTransitBtn.setBackgroundResource(R.mipmap.transition_flag);
        mContainerMini.setVisibility(View.INVISIBLE);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "find path...");
                if (myLocation == null && mDestinationPoint == null) {
                    Toast.makeText(MapsActivity.this, "Destination Or Current Location Is Absent!", Toast.LENGTH_SHORT).show();
                } else {
                    sendRequest();
                }
            }
        });
        mShowAllPlaces.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "showing all places...");
                updateCamera();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "GPS_PROVIDER and NETWORK_PROVIDER not be called");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 10, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, mLocationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.i(TAG, "onOptionsItemSelected called");
        final Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.clear_all_appointed_points_item:
                Log.i(TAG, "you select: CLEAR");
                Toast.makeText(this, "CLEAR", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                alertDialog.setTitle(R.string.request);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDestinationPoint = null;
                        mTransitionPoints.clear();
                        mDestinationMarker = null;
                        updateCamera();
                    }
                });
                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                return true;
            case R.id.preview_points_item:
                Log.i(TAG, "you select: PREVIEW");
                Toast.makeText(this, "PREVIEW", Toast.LENGTH_SHORT).show();
                intent = PlacePagerActivity.newIntent(this);
                startActivity(intent);
                return true;
            case R.id.new_point_item:
                Log.i(TAG, "you select: NEW POINT");
                Toast.makeText(this, "NEW POINT", Toast.LENGTH_SHORT).show();
                if (myLocation == null) {
                    Toast.makeText(this, "try later, current location is't detected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent = AddPointActivity.newIntent(MapsActivity.this);
                intent.putExtra("latitude", myLocation.latitude);
                intent.putExtra("longitude", myLocation.longitude);

                if (isNearSavedLocation(myLocation) != null) {
                    LatLng near = isNearSavedLocation(myLocation);
                    String stringSubtitle = getString(R.string.add_point_question_dialog, "point lat:"
                            + new Formatter().format("%.6f", near.latitude)
                            + ", lng:" + new Formatter().format("%.6f", near.longitude));
                    choiceDialog = ChoiceDialog.newInstance(stringSubtitle);
                    //choiceDialog.show(getSupportFragmentManager(), "choice_dialog");
                    getSupportFragmentManager().beginTransaction()
                            .add(choiceDialog, "choice_dialog")
                            .commit();
                }
                else {
                    startActivityForResult(intent, REQUEST_NEW_POINT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_NEW_POINT) {
            updateCamera();
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "LocationListener/onLocationChanged called");
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            Toast.makeText(MapsActivity.this, "YOUR LOCATION IS APPOINTED", Toast.LENGTH_SHORT).show();
            updateCamera();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private LatLng isNearSavedLocation(@NonNull LatLng currentLocation) {
        for (int i = 0; i < mListSavedLocations.size(); i++) {
            LatLng latLng = mListSavedLocations.get(i).getLatLng();
            if ((currentLocation.latitude < (latLng.latitude + 0.005)
                    && currentLocation.latitude > (latLng.latitude - 0.005))
                    && (currentLocation.longitude < (latLng.longitude + 0.005)
                    && currentLocation.longitude > (latLng.longitude - 0.005))) {
                return mListSavedLocations.get(i).getLatLng();
            }
        }
        return null;
    }

    private void sendRequest() {
        String origin = String.valueOf(myLocation.latitude) + ", " + String.valueOf(myLocation.longitude);
        Log.i(TAG, "origin = " + origin);
        String destination = String.valueOf(mDestinationPoint.latitude) + ", " + String.valueOf(mDestinationPoint.longitude);
        Log.i(TAG, "destination = " + destination);
        Log.i(TAG, "after initialize string's points");
        if (origin.isEmpty()) {
            Toast.makeText(this, "Enter the origin of route", Toast.LENGTH_SHORT).show();
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Enter destination point", Toast.LENGTH_SHORT).show();
        }
        try {
            try {
                new DirectionFinder(this, origin, destination, convertLatLngListToStringList(mTransitionPoints)).execute();
            } catch (UnsupportedEncodingException e) {
                Log.i(TAG, e.getMessage());
                e.printStackTrace();
            }
        } catch (UnsupportedOperationException exc) {
            Log.i(TAG, exc.getMessage());
            exc.printStackTrace();
        }
    }

    private static List<String> convertLatLngListToStringList(List<LatLng> latLngsList) {
        List<String> list = new ArrayList<>();
        for (LatLng latLng : latLngsList) {
            list.add(String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
        }
        return list;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady called");
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        updateCamera();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        int tag;
        if (marker != null) {
            tag = (int) marker.getTag();
            final MemoryPlace mp = PlaceLab.get(this).getMemoryPlace().get(tag);
            File file = PlaceLab.get(this).getPhotoFile(mp);
            Bitmap bitmap = PictureUtils.getScaledBitmap(file.getPath(), this);
            mImage.setImageBitmap(bitmap);

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = DetailPlaceActivity.newInstance(getApplicationContext(), mp);
                    startActivity(intent);
                }
            });

            if (mTransitionPoints.contains(mp.getLatLng())) {
                mTransitBtn.setBackgroundResource(R.mipmap.transit_cancel);
            } else {
                mTransitBtn.setBackgroundResource(R.mipmap.transition_flag);
            }
            mContainerMini.setVisibility(View.VISIBLE);

            mHidePicture.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mContainerMini.setVisibility(View.INVISIBLE);
                }
            });

            mTargetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTransitionPoints.remove(mp.getLatLng());
                    if (!mp.getLatLng().equals(mDestinationPoint)) {
                        if (mDestinationMarker != null) {
                            mDestinationMarker.setIcon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    }
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mDestinationPoint = mp.getLatLng();
                    mDestinationMarker = marker;
                    mTransitBtn.setBackgroundResource(R.mipmap.transition_flag);
                }
            });

            mTransitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mTransitionPoints.contains(mp.getLatLng())) {
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        mTransitBtn.setBackgroundResource(R.mipmap.transit_cancel);
                        mTransitionPoints.add(mp.getLatLng());
                        if (mp.getLatLng().equals(mDestinationPoint)) {
                            mDestinationPoint = null;
                            mDestinationMarker = null;
                        }
                    } else {
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mTransitBtn.setBackgroundResource(R.mipmap.transition_flag);
                        mTransitionPoints.remove(mp.getLatLng());
                    }
                }
            });
        } else {
            Log.i(TAG, "marker == null -> true");
        }
        return false;
    }

    private void updateCamera() {
        if (mMap == null) {
            Log.i(TAG, "mMap = null");
            return;
        }
        originMarkers.clear();
        mMap.clear();
        mListSavedLocations = PlaceLab.get(getApplicationContext()).getMemoryPlace();
        Log.i(TAG, "List<MemoryPlace> list.size() = " + mListSavedLocations.size());
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (int i = 0; i < mListSavedLocations.size(); i++) {
            bounds.include(mListSavedLocations.get(i).getLatLng());
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title("HUE_RED")
                    .position(mListSavedLocations.get(i).getLatLng())));
            originMarkers.get(i).setTag(i);
            if (mListSavedLocations.get(i).getLatLng().equals(mDestinationPoint)) {
                Marker marker = originMarkers.get(i);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            if (mTransitionPoints.contains(mListSavedLocations.get(i).getLatLng())) {
                Marker marker = originMarkers.get(i);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }
        }
        if (myLocation != null) {
            bounds.include(myLocation);
        }
        int margin = getResources().getDimensionPixelSize(R.dimen.map_insert_margin);
        final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds.build(), margin);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        final LatLngBounds.Builder finalBounds = bounds;
        mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.i(TAG, "onMapLoaded/bounds = " + finalBounds.toString());
                mMap.animateCamera(update);
                //Your code where exception occurs goes here...
            }
        });
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.tvClock)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_icon))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.finish_icon))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
