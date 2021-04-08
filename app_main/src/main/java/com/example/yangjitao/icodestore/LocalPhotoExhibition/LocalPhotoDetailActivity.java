package com.example.yangjitao.icodestore.LocalPhotoExhibition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yangjitao.icodestore.CommonFunciton.GridItemDecoration;
import com.example.yangjitao.icodestore.R;
import com.example.yangjitao.icodestore.Utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LocalPhotoDetailActivity extends AppCompatActivity {
    private List<Photo> photoList = new ArrayList<>();

    private static Album showAlbum;

    public static void  start(Context context, Album album){
        showAlbum = album;
        context.startActivity(new Intent(context, LocalPhotoDetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_show);

        if(showAlbum == null) return;
        photoList = showAlbum.getChildPhotoList();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rc_album);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(new GridItemDecoration());
        recyclerView.setAdapter(new AlbumAdapter(this));
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHoder>{
        private RelativeLayout.LayoutParams layoutParams;
        public AlbumAdapter(Context context){
            int itemViewHeight = CommonUtils.getDisplayMetrics((Activity) context).widthPixels / 4;
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemViewHeight);
        }

        @Override
        public AlbumHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
            view.setLayoutParams(layoutParams);
            return new AlbumHoder(view);
        }

        @Override
        public void onBindViewHolder(AlbumHoder holder, int position) {
            Photo photo = photoList.get(position);
            ImageLoader.getInstance().displayImage("file://" + photo.getThumbnailPath(), holder.albumPreview);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

        public class AlbumHoder extends RecyclerView.ViewHolder{
            private ImageView albumPreview;
            private TextView albumNameView;

            public AlbumHoder(View itemView) {
                super(itemView);
                albumPreview = (ImageView)itemView.findViewById(R.id.iv_album_preview);
                albumNameView = (TextView)itemView.findViewById(R.id.tv_name);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showAlbum = null;
    }
}
