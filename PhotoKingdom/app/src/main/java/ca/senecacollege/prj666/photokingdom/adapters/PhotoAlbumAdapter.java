package ca.senecacollege.prj666.photokingdom.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.senecacollege.prj666.photokingdom.PhotoFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Photo;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;

/**
 * @author zhihao
 */
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
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String path = RetrofitServiceGenerator.getBaseUrl() + photoLists.get(position).getPhotoFilePath();
        Picasso.with(context).load(path).into(viewHolder.img);

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoFragment photoFragment = PhotoFragment.newInstance(photoLists.get(position).getId());
                // move to photo fragment
                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, photoFragment)
                        .addToBackStack(null)
                        .commit();
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
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
