package ca.senecacollege.prj666.photokingdom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Photo;

public class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.ViewHolder>{
    private ArrayList<Photo> photoLists;
    private Context context;

    public PhotoAlbumAdapter(Context context, ArrayList<Photo> photoLists) {
        this.photoLists = photoLists;
        this.context = context;
    }

    @Override
    public PhotoAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo_album, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoAlbumAdapter.ViewHolder viewHolder, final int position) {
         //viewHolder.title.setText(photoLists.get(position).getTitle());
         viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
         //viewHolder.img.setImageResource((photoLists.get(position).getId()));

         Picasso.with(context).load(photoLists.get(position).getId()).into(viewHolder.img);

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open photo detail ?
                Toast.makeText(context, photoLists.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            //title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }


}
