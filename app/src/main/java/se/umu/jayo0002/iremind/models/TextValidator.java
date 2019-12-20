package se.umu.jayo0002.iremind.models;

/**
 * A class that provides two static methods.
 */
public class TextValidator {


    /**
     * It checks whether the title is valid, according to the rules.
     * @param title
     * @return boolean
     */
    public static boolean isTitleValid(String title){
        if (title == null)
            return false;
        else return title.length() <= 12;
    }

    /**
     * It checks whether the note is valid, according to the rules.
     * @param note
     * @return boolean
     */
    public static boolean isNoteValid(String note){
        if (note == null)
            return false;
        return note.length() <= 300;
    }
}
