package se.umu.jayo0002.iremind.models;

/**
 * Interface that implements one void method.
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public interface OnItemClickListener {

    /**
     * It is intended to listen to the object that handles the task.
     * @param task
     */
    void onItemClick(Task task);
}