package ca.senecacollege.prj666.photokingdom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueueItem;

/**
 * Adapter for RecyclerView in PhotowarQueueFragment
 *
 * @author Wonho
 */
public class PhotowarQueueAdapter extends RecyclerView.Adapter<PhotowarQueueAdapter.ViewHolder> {

    // Photowar queue items data
    private List<PhotowarQueueItem> mPhotowarQueueItems;

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        ImageView imageView;
        TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);

            // Set references for views
            textViewDate = (TextView)itemView.findViewById(R.id.textViewDate);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            textViewName = (TextView)itemView.findViewById(R.id.textViewName);
        }
    }

    public PhotowarQueueAdapter(List<PhotowarQueueItem> photowarQueueItems) {
        mPhotowarQueueItems = photowarQueueItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photowar_queue, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set views
        holder.textViewDate.setText(mPhotowarQueueItems.get(position).getDate());
        holder.imageView.setImageResource(mPhotowarQueueItems.get(position).getImgResId());
        holder.textViewName.setText(mPhotowarQueueItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mPhotowarQueueItems.size();
    }
}
