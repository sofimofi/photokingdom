package ca.senecacollege.prj666.photokingdom;

import java.util.List;
import java.util.Set;

import ca.senecacollege.prj666.photokingdom.models.GooglePlace;

/**
 * Interface for GooglePlacesAPI async Task Completion
 *
 * @author Sofia
 */

public interface OnGooglePlacesApiTaskCompleted {
    void OnTaskCompleted(Set<GooglePlace> googlePlaces);
}
