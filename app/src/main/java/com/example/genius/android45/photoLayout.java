package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class photoLayout extends ArrayAdapter{
    private Context mContext;
    Album openedAlbum = Android45.currAlbum;
    private Integer[] photoIds;
    private ArrayList<Uri> photoUris;

    photoLayout(Context mContext){
        super(mContext, R.layout.activity_photo_layout);
        this.mContext = mContext;
       // getPhotoIds(openedAlbum);
        getPhotoUris(openedAlbum);
    }

    public int getCount() {
        return photoUris.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        //getPhotoIds(openedAlbum);
        //getPhotoUris(openedAlbum);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_photo_layout, parent, false);
        }
        imageView = (ImageView) convertView.findViewById(R.id.PhotoLayout);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(2, 8, 2, 8);

        //if(PhotosActivity.addingPhoto){
        imageView.setImageURI(photoUris.get(position));
            //PhotosActivity.addingPhoto=false;
       // }
        //else{
            //imageView.setImageResource(photoIds[position]);
        //}
        return imageView;
    }


    /*private void getPhotoIds(Album album){
        int i = 0;
        ArrayList<Photo> photos = album.getHashPhotos(album.getName());
        photoIds = new Integer[photos.size()];
        for(Photo p : photos){
            //System.out.println("photo names are: " + p.getPhotoName());
            photoIds[i] = p.getPhotoId();
            i++;
        }
    }*/

    private void getPhotoUris(Album album){
        int i = 0;
        ArrayList<Photo> photos = album.getHashPhotos(album.getName());
        photoUris = new ArrayList<>();
        for(Photo p : photos){
            //System.out.println("photo names are: " + p.getPhotoName());
            photoUris.add( p.getPhotoUri());
            i++;
        }
    }

   /* public void removePhoto(Album album, Photo photo){
        ArrayList<Photo> photos = album.getHashPhotos(album.getName());
        int i = 0;
        Integer[] tempPhotos = new Integer[photos.size() - 1];
        int index = photos.indexOf(photo);
        for(int e : photoIds){
            if(e != photos.get(index).photoId)
                tempPhotos[i] = e;
            i++;
        }
        photoIds = tempPhotos;
        photos.remove(photo);
    }*/
}
