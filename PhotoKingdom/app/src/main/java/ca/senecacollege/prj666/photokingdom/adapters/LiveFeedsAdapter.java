package ca.senecacollege.prj666.photokingdom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.LiveFeed;

/**
 * Fragment for live feeds
 *
 * @author Zhihao, Wonho
 */

public class LiveFeedsAdapter extends RecyclerView.Adapter<LiveFeedsAdapter.ViewHolder> {

    // Live feeds data
    private List<LiveFeed> mFeeds;

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

    public LiveFeedsAdapter(List<LiveFeed> feeds) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set views
        holder.textViewDate.setText(mFeeds.get(position).getDate());
        holder.textViewMsg.setText(mFeeds.get(position).getMsg());

        if (mFeeds.get(position).getImgRes1() == 0) {
            // Message feed
            hidePhotowarViews(holder);
        } else {
            // Photowar feed
            holder.imageView1.setImageResource(mFeeds.get(position).getImgRes1());
            holder.imageView2.setImageResource(mFeeds.get(position).getImgRes2());
            holder.textViewName1.setText(mFeeds.get(position).getName1());
            holder.textViewName2.setText(mFeeds.get(position).getName2());

            showPhotowarViews(holder);
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
}
