package com.development.cosmic_m.navigator;

import java.io.Serializable;

/**
 * Created by Cosmic_M on 23.10.2017.
 */

public interface RouteComposeListener extends Serializable{
    void onAssignDestinationPoint(int tag);
    int onAssignTransitionPoint(int tag);
    void onExcludePoint(int tag);
    void onDetailedPointShow(int tag);
    int getResourceForTransitionImage(int tag);
}
