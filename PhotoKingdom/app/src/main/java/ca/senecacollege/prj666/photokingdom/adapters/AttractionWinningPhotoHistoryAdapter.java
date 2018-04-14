package ca.senecacollege.prj666.photokingdom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.PhotoWinning;
import ca.senecacollege.prj666.photokingdom.utils.LoadImage;

/**
 * Adapter for Attraction Winning Photo History
 * @author sofia
 */
public class AttractionWinningPhotoHistoryAdapter extends RecyclerView.Adapter<AttractionWinningPhotoHistoryAdapter.ViewHolder> {
    private static final String TAG = "winningPhotosAdapter";
    private Context context;
    private List<PhotoWinning> mWinningPhotos;

    public AttractionWinningPhotoHistoryAdapter(Context context, List<PhotoWinning> winningPhotos){
        this.context = context;
        this.mWinningPhotos = winningPhotos;
        Log.d(TAG, "Received list of winning photos, count: " + winningPhotos.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attraction_winning_photo_item, parent, false);

        return new AttractionWinningPhotoHistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PhotoWinning photo = mWinningPhotos.get(position);

        // set data
        LoadImage.loadImage(context, holder.photo, photo.getPhotoFilePath());
        LoadImage.loadImage(context, holder.residentAvatar, photo.getResidentAvatarImagePath());
        holder.residentName.setText(photo.getResidentUserName());
        holder.votes.setText(String.valueOf(photo.getVotes()));
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWinningPhotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        ImageView residentAvatar;
        TextView residentName;
        TextView votes;

        public ViewHolder(View itemView){
            super(itemView);

            // set reference for views
            photo = (ImageView) itemView.findViewById(R.id.attractionWinningPhotoHistoryItemImageView);
            residentAvatar = (ImageView) itemView.findViewById(R.id.attractionWinningPhotoHistoryItemResidentAvatarImageView);
            residentName = (TextView) itemView.findViewById(R.id.attractionWinningPhotoHistoryItemResidentTextView);
            votes = (TextView) itemView.findViewById(R.id.attractionWinningPhotoHistoryItemPointsTextView);
        }
    }

    // Listener to click an item
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
}
