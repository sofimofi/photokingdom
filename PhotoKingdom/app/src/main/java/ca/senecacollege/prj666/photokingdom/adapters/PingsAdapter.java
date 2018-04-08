package ca.senecacollege.prj666.photokingdom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;

/**
 * Adapter for RecyclerView in PingsFragment
 *
 * @author Wonho
 */
public class PingsAdapter extends RecyclerView.Adapter<PingsAdapter.ViewHolder> {
    // Ping list data
    private List<Ping> mPings;

    // ViewHolder for an item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPingDate;
        TextView textViewExpiryDate;
        TextView textViewAttractionName;

        public ViewHolder(View itemView) {
            super(itemView);

            // Set references for views
            textViewPingDate = (TextView)itemView.findViewById(R.id.textViewPingDate);
            textViewExpiryDate = (TextView)itemView.findViewById(R.id.textViewExpiryDate);
            textViewAttractionName = (TextView)itemView.findViewById(R.id.textViewAttractionName);
        }
    }

    public PingsAdapter(List<Ping> pings) {
        mPings = pings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflation
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ping, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String pingDate = mPings.get(position).getPingDate();
        String expiryDate = mPings.get(position).getExpiryDate();

        // Set views
        holder.textViewPingDate.setText(DateUtil.parseDateString(pingDate));
        holder.textViewExpiryDate.setText(DateUtil.getExpiresIn(pingDate, expiryDate));
        holder.textViewAttractionName.setText(mPings.get(position).getAttractionName());

        // Item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPings.size();
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