package com.development.cosmic_m.navigator.Modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Cosmic_M on 11.09.2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String startAddress;
    public LatLng startLocation;
    public String endAddress;
    public LatLng endLocation;
     public List<LatLng> points;
}
