package se.umu.jayo0002.iremind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.OnItemClickListener;
import se.umu.jayo0002.iremind.models.Task;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable{
    private OnItemClickListener mListener;
    private List<Task> mTasks = new ArrayList<>();
    private List<Task> mTasksFull = new ArrayList<>();
    private int lastPosition = -1;
    void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }

    /**
     * Called when RecyclerView needs a new {@link TaskAdapter.TaskHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(TaskHolder, int)
     */
    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new TaskHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link TaskHolder to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link TaskHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(TaskHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.mTaskTitle.setText(task.getTitle());
        holder.mTaskDate.setText(task.getDate());
        holder.mTaskTime.setText(task.getTime());
        Objects.requireNonNull(holder.itemView).setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    Task getTaskAt(int position) {
        return mTasksFull.get(position);
    }

    void setAll(List<Task> tasks) {
        this.mTasks = tasks;
        mTasksFull = new ArrayList<>(mTasks);
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView mTaskTitle;
        private final TextView mTaskTime;
        private final TextView mTaskDate;
        TaskHolder(View view) {
            super(view);
            mTaskTitle = view.findViewById(R.id.tittle_tv);
            mTaskTime = view.findViewById(R.id.time_tv);
            mTaskDate = view.findViewById(R.id.date_tv);
            view.setOnClickListener(v -> {
                if (mListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    mListener.onItemClick(mTasksFull.get(getAdapterPosition()));
            });
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Task> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mTasksFull);
            } else {
                String text = charSequence.toString().toLowerCase().trim();
                for (Task task : mTasksFull) {
                    if (task.getTitle().toLowerCase().contains(text))
                        filteredList.add(task);
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults ;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mTasks.clear();
            mTasks.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}