package se.umu.jayo0002.iremind.models;

/**
 * Interface that implements one void method.
 */
public interface OnItemClickListener {


    /**
     * It is intended to listen to object that handles the task.
     * @param task
     */
    void onItemClick(Task task);
}