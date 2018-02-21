package com.development.cosmic_m.navigator;

/**
 * Created by Cosmic_M on 23.10.2017.
 */

public interface RouteComposeListener {
    int onAssignDestinationPoint(int tag);
    int onAssignTransitionPoint(int tag);
    void onRemoveFragment(int tag);
    void onDetailedPointShow(int tag);
    int getResourceForTransitionImage(int tag);
}
