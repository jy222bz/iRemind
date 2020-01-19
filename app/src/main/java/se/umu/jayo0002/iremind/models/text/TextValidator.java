package se.umu.jayo0002.iremind.models.text;

/**
 * A class that provides two static methods.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class TextValidator {

    /**
     * A private constructor.
     */
    private TextValidator(){}


    /**
     * It checks whether the title is valid, according to the rules.
     * @param title
     * @param theMaximumAmountOfChars
     * @return boolean
     */
    public static boolean isTitleNotValid(String title, int theMaximumAmountOfChars){
        if (title == null)
            return true;
        else return title.length() > theMaximumAmountOfChars;
    }

    /**
     * It checks whether the note is valid, according to the rules.
     * @param note
     * @param theMaximumAmountOfChars
     * @return boolean
     */
    public static boolean isNoteValid(String note, int theMaximumAmountOfChars){
        if (note == null)
            return false;
        return note.length() <= theMaximumAmountOfChars;
    }
}
