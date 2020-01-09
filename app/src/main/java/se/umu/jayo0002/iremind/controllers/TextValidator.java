package se.umu.jayo0002.iremind.controllers;

/**
 * A class that provides two static methods.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class TextValidator {


    /**
     * It checks whether the title is valid, according to the rules.
     * @param title
     * @param theMaximumAmountOfChars
     * @return boolean
     */
    public static boolean isTitleValid(String title, int theMaximumAmountOfChars){
        if (title == null)
            return false;
        else return title.length() <= theMaximumAmountOfChars;
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
