package se.umu.jayo0002.iremind.models.exceptions;

/**
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 *
 * This Class implements IException and Extends RunTime Exception.
 */
public class ExceptionBuilder extends RuntimeException implements IException{

    /**
     * A private field for the classification of the Exception
     */
    private final Classification classification;

    /**
     * A private field for the description of the Exception
     */
    private final String description;

    /**
     * A constructor to construct an Exception Object.
     * @param classification
     */
    public ExceptionBuilder(Classification classification){
        this.classification = classification;
        this.description = classification.getDescription();
    }

    /**
     * It return the classification of the Exception.
     * @return
     */
    @Override
    public Classification getClassification() {
        return this.classification;
    }

    /**
     * It returns the description of the Exception.
     * @return
     */
    @Override
    public String getDescription() {
        return this.description;
    }
}
