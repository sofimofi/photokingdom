package ca.senecacollege.prj666.photokingdom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueue;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;

/**
 * Adapter for RecyclerView in PhotowarQueueFragment
 *
 * @author Wonho
 */
public class PhotowarQueueAdapter extends RecyclerView.Adapter<PhotowarQueueAdapter.ViewHolder> {
    private static final String TAG = "PhotowarQueueAdapter";

    private Context mContext;

    // Photowar queue items data
    private List<PhotowarQueue> mPhotowarQueues;

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

    public PhotowarQueueAdapter(Context context, List<PhotowarQueue> photowarQueues) {
        mContext = context;
        mPhotowarQueues = photowarQueues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photowar_queue, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Set views
        final PhotowarQueue photowarQueue = mPhotowarQueues.get(position);
        holder.textViewDate.setText(DateUtil.parseDateString(photowarQueue.getQueueDate()));
        holder.textViewName.setText(photowarQueue.getResidentName());
        loadImage(holder.imageView, photowarQueue.getPhotoPath());

        // Photo Click
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onPhotoItemClick(view, photowarQueue.getPhotoId());
            }
        });

        // Resident name click
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onResidentItemClick(view, photowarQueue.getResidentId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotowarQueues.size();
    }

    private void loadImage(ImageView imageView, final String imagePath){
        if (imagePath != null) {
            String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
            Picasso.with(mContext).load(imageUrl)
                    .error(R.drawable.noimage)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Succeeded photo of " + imagePath);
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Failed photo of " + imagePath);
                        }
                    });
        }
    }

    // Listener to click an item
    public interface OnItemClickListener {
        void onPhotoItemClick(View view, int photoId);
        void onResidentItemClick(View view, int residentId);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
}
