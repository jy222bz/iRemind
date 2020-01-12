package se.umu.jayo0002.iremind.models.model_controllers;

/**
 * It implements IControlObjects.
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public class ObjectController implements IControlObjects {


    /**
     * It checks whether the object is null or not.
     *
     * @param object
     * @param <T>
     * @return boolean
     */
    @Override
    public <T> boolean isObjectValid(T object) {
        return object != null;
    }
}
