package se.umu.jayo0002.iremind.models.exceptions;

/**
 * Interface for the customized Exceptions.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public interface IException {


    /**
     * It returns the classification of the Exception.
     * @return
     */
    Classification getClassification();

    /**
     * It returns the description of the Exception.
     * @return
     */
    String getDescription();
}
