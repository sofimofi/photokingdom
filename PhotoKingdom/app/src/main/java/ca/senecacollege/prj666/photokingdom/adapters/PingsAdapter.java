package ca.senecacollege.prj666.photokingdom.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Ping;

/**
 * Adapter for RecyclerView in PingsFragment
 *
 * @author Wonho
 */
public class PingsAdapter extends RecyclerView.Adapter<PingsAdapter.ViewHolder> {

    private List<Ping> mPings;

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPingDate;
        TextView textViewExpiryDate;
        TextView textViewAttractionName;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ping, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewPingDate.setText(mPings.get(position).getPingDate());
        holder.textViewExpiryDate.setText(mPings.get(position).getExpiryDate());
        holder.textViewAttractionName.setText(mPings.get(position).getAttractionName());
    }

    @Override
    public int getItemCount() {
        return mPings.size();
    }
}
