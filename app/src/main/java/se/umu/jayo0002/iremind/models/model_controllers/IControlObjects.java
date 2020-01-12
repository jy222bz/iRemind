package se.umu.jayo0002.iremind.models.model_controllers;

/**
 * This interface for controlling objects.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public interface IControlObjects {


    /**
     * To control whether the object is null or not.
     * @param object
     * @param <T>
     * @return boolean
     */
    <T> boolean isObjectValid(T object);
}
