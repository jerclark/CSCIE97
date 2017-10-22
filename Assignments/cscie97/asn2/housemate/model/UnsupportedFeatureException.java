package cscie97.asn2.housemate.model;
import java.util.Set;

public class UnsupportedFeatureException extends Exception {

    UnsupportedFeatureException(String deviceFqn, String requestedFeature, Set<String> availableFeatures){
        super("Device " + deviceFqn + " doesn't support the feature:  " + requestedFeature + ". Availble features on that device are: " + availableFeatures.toString());
    }

}
