package se.umu.jayo0002.iremind.models.exceptions;

/**
 * Enum Class for the classifications and the descriptions the of Exceptions.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public enum Classification {

    ALARM_INVALID("The Time And The Date of The REMINDER Are In The Past!"),

    LAT_LNG_NULL("The LatLng Object Is NULL!"),

    LOCATION_INFO_NULL("The LocationInfo Object Is NULL!");

    /**
     * Private field for the description.
     */
    private final String description;

    /**
     * Constructor to construct the object.
     * @param description
     */
    Classification(String description) {
        this.description = description;
    }

    /**
     * It returns the description.
     * @return String
     */
    public String getDescription(){
        return this.description;
    }
}
