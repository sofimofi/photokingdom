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
import ca.senecacollege.prj666.photokingdom.models.FeedEntry;
import ca.senecacollege.prj666.photokingdom.models.LiveFeed;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;

/**
 * Fragment for live feeds
 *
 * @author Zhihao, Wonho
 */

public class LiveFeedsAdapter extends RecyclerView.Adapter<LiveFeedsAdapter.ViewHolder> {
    private static final String TAG = "LiveFeedsAdapter";

    // Live feeds data
    private List<LiveFeed> mFeeds;
    private Context mContext;

    // ViewHolder for an item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewMsg;
        ImageView imageView1;
        ImageView imageView2;
        TextView textViewName1;
        TextView textViewName2;

        public ViewHolder(View itemView) {
            super(itemView);

            // Set references for views
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewMsg = (TextView) itemView.findViewById(R.id.textViewMsg);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            textViewName1 = (TextView) itemView.findViewById(R.id.textViewName1);
            textViewName2 = (TextView) itemView.findViewById(R.id.textViewName2);
        }
    }

    public LiveFeedsAdapter(Context context, List<LiveFeed> feeds) {
        mContext = context;
        mFeeds = feeds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_feed, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Set views
        holder.textViewDate.setText(mFeeds.get(position).getDate());
        holder.textViewMsg.setText(mFeeds.get(position).getMsg());

        //if (mFeeds.get(position).getPhotoPath1().isEmpty()) {
        if (mFeeds.get(position).getType() == FeedEntry.TYPE_PHOTOWAR) {
            // Photowar feed
            loadImage(holder.imageView1, mFeeds.get(position).photowar.getPhotoPath1());
            loadImage(holder.imageView2, mFeeds.get(position).photowar.getPhotoPath2());
            holder.textViewName1.setText(mFeeds.get(position).photowar.getResidentName1());
            holder.textViewName2.setText(mFeeds.get(position).photowar.getResidentName2());

            showPhotowarViews(holder);

            // Photowar click
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int photowarId = mFeeds.get(position).photowar.getPhotowarId();
                    mOnItemClickListener.onPhotowarItemClick(view, photowarId);
                }
            });
        } else {
            // Own feed
            hidePhotowarViews(holder);

            // Own click
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int ownId = mFeeds.get(position).own.getResidentId();
                    mOnItemClickListener.onOwnItemClick(view, ownId);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFeeds.size();
    }

    /**
     * Show views for photowar feeds
     * @param holder
     */
    private void showPhotowarViews(ViewHolder holder) {
        holder.imageView1.setVisibility(View.VISIBLE);
        holder.imageView2.setVisibility(View.VISIBLE);
        holder.textViewName1.setVisibility(View.VISIBLE);
        holder.textViewName2.setVisibility(View.VISIBLE);
    }

    /**
     * Hide views for photowar feeds
     * This is for message feeds
     * @param holder
     */
    private void hidePhotowarViews(ViewHolder holder) {
        holder.imageView1.setVisibility(View.GONE);
        holder.imageView2.setVisibility(View.GONE);
        holder.textViewName1.setVisibility(View.GONE);
        holder.textViewName2.setVisibility(View.GONE);
    }

    private void loadImage(ImageView imageView, final String imagePath){
        String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
        Picasso.with(mContext).load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Succeeded photo upload of " + imagePath);
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Failed photo upload of " + imagePath);
                    }
                });
    }

    // Listener to click an item
    public interface OnItemClickListener {
        void onPhotowarItemClick(View view, int photowarId);
        void onOwnItemClick(View view, int ownId);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
}
