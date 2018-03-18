package ca.senecacollege.prj666.photokingdom;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.AttractionForMapView;


/**
 * Interface for AttractionsForMapView API async Task Completion
 */
public interface OnAttractionsForMapViewTaskCompleted {
    void OnTaskCompleted(List<AttractionForMapView> googlePlaces);
}
