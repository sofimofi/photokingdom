package ca.senecacollege.prj666.photokingdom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotowarView;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;
import ca.senecacollege.prj666.photokingdom.utils.LoadImage;

/**
 * Adapater for AttractionPhotowarHistory
 *
 * @author sofia
 */

public class AttractionPhotowarHistoryAdapter extends RecyclerView.Adapter<AttractionPhotowarHistoryAdapter.ViewHolder> {
    private static final String TAG = "photowarHistoryAdapter";

    // AttractionPhotowar list data
    private List<AttractionPhotowarWithDetails> mAttractionPhotowars;
    private Context context;

    public AttractionPhotowarHistoryAdapter(Context context, List<AttractionPhotowarWithDetails> photowars) {
        this.context = context;
        this.mAttractionPhotowars = photowars;
    }

    // ViewHolder for an item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView warEndDateTextView;
        ImageView photo1ImageView;
        ImageView photo2ImageView;
        TextView photo1PtsTextView;
        TextView photo2PtsTextView;
        ImageView resident1Avatar;
        ImageView resident2Avatar;
        TextView resident1Name;
        TextView resident2Name;
        ImageView photo1Trophy;
        ImageView photo2Trophy;
        ImageView swordsImageView;
        RelativeLayout photowarItemLayout;
        LinearLayout photowarItemLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            // Set references for views
            warEndDateTextView = (TextView) itemView.findViewById(R.id.attractionPhotowarHistoryItemDateTextView);
            photo1ImageView = (ImageView) itemView.findViewById(R.id.attractionPhotowarHistoryItemPhoto1ImageView);
            photo2ImageView = (ImageView) itemView.findViewById(R.id.attractionPhotowarHistoryItemPhoto2ImageView);
            photo1PtsTextView = (TextView) itemView.findViewById(R.id.attractionPhotowarHistoryItemPhoto1PointsTextView);
            photo2PtsTextView = (TextView) itemView.findViewById(R.id.attractionPhotowarHistoryItemPhoto2PointsTextView);
            resident1Avatar = (ImageView) itemView.findViewById(R.id.attractionPhotowarHistoryItemResident1AvatarImageView);
            resident2Avatar = (ImageView) itemView.findViewById(R.id.attractionPhotowarHistoryItemResident2AvatarImageView);
            resident1Name = (TextView) itemView.findViewById(R.id.attractionPhotowarHistoryItemResident1TextView);
            resident2Name = (TextView) itemView.findViewById(R.id.attractionPhotowarHistoryItemResident2TextView);
            photo1Trophy = (ImageView) itemView.findViewById(R.id.photo1TrophyPhotowarHistory);
            photo2Trophy = (ImageView) itemView.findViewById(R.id.photo2TrophyPhotowarHistory);
            swordsImageView = (ImageView) itemView.findViewById(R.id.swordsImageView);
            photowarItemLayout = (RelativeLayout) itemView.findViewById(R.id.photowarItemLayout);
            photowarItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.attractionPhotowarHistoryPhotosLinearLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attraction_photowar_history_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // set data

        AttractionPhotowarWithDetails photowar = mAttractionPhotowars.get(position);

        // photo 1
        AttractionPhotowarUploadForPhotowarView photo1 = photowar.getAttractionPhotowarUploads().get(0);
        LoadImage.loadImage(context, holder.photo1ImageView, photo1.getPhoto().getPhotoFilePath());
        holder.photo1PtsTextView.setText(String.valueOf(photo1.getResidentVotesCount()));
        LoadImage.loadImage(context, holder.resident1Avatar, photo1.getPhotoResidentAvatarImagePath());
        holder.resident1Name.setText(photo1.getPhotoResidentUserName());

        // photo 2
        AttractionPhotowarUploadForPhotowarView photo2 = photowar.getAttractionPhotowarUploads().get(1);
        LoadImage.loadImage(context, holder.photo2ImageView, photo2.getPhoto().getPhotoFilePath());
        holder.photo2PtsTextView.setText(String.valueOf(photo2.getResidentVotesCount()));
        LoadImage.loadImage(context, holder.resident2Avatar, photo2.getPhotoResidentAvatarImagePath());
        holder.resident2Name.setText(photo2.getPhotoResidentUserName());

        // determine war status
        String endDate = photowar.getEndDate();
        if(!DateUtil.isBeforeNow(endDate)){
            // war is still going on
            holder.warEndDateTextView.setText(R.string.photowar_currently_ongoing);
            holder.swordsImageView.setVisibility(View.VISIBLE);
            holder.swordsImageView.setTag(photowar.getId());
        } else {
            // war is over, determine winner

            holder.warEndDateTextView.setText(DateUtil.ISO8601ToSimpleDateString(photowar.getEndDate()));

            if(photo1.getIsWinner() == 1){
                holder.photo1Trophy.setVisibility(View.VISIBLE);
            } else {
                holder.photo2Trophy.setVisibility(View.VISIBLE);
            }
        }

        // Item click
        holder.photowarItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });

        holder.photo1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });

        holder.photo2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mAttractionPhotowars.size();
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
