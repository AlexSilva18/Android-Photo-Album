package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class PhotosActivity extends AppCompatActivity {

    Album currAlbum = Android45.currAlbum;
    ImageButton backButton;
    private ArrayAdapter<Photo> listAdapter;
    private Integer images[];
    public static ArrayList<Photo> photos = new ArrayList<>();
    photoLayout photoAdapter;
    GridView gridView;
    static Photo selectedPhoto;
    ImageButton addPhoto;
    public static final int PICK_IMAGE = 100;
    public static boolean addingPhoto =false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photoAdapter = new photoLayout(this);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(photoAdapter);
        selectedPhoto=null;
        photos = currAlbum.getHashPhotos(currAlbum.getName());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedPhoto = photos.get(position);
                Toast.makeText(PhotosActivity.this, "" + selectedPhoto.getPhotoName(),
                        Toast.LENGTH_SHORT).show();
                selectedPhoto = photos.get(position);
            }
        });

        addPhoto=findViewById(R.id.addSearchAlbum);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_albums, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.myAlbums: //code here
                //Toast.makeText(PhotosActivity.this, "Have A Nice Day!!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PhotosActivity.this, Android45.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
    public void addPhoto(View view) {
        // add new photo with photoId, name constructor, photoId = R.drawable.photoName
        // Photo newPhoto = new Photo(...);
        // addHashPhoto
        //currAlbum.addHashPhotos(currAlbum.getName(), newPhoto);
        // on return run onCreate once more to refill the photo application
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == -1) {
            addingPhoto = true;
            super.onActivityResult(requestCode, resultCode, data);
            System.out.print("THIS WORKED");
            Uri imageUri = data.getData();
            //imageUri = getUriToResource(R.drawimageUri);
            Photo ph = new Photo(imageUri, imageUri.toString());
            for(Photo p:photos){
                if(p.getPhotoUri().equals(ph.getPhotoUri())){
                    Toast.makeText(PhotosActivity.this, "Picture Already In Album", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //photos.add(ph);

            currAlbum.addHashPhotos(currAlbum.name, ph);
            System.out.println(currAlbum.getHashPhotos(currAlbum.name).toString());
            //photoAdapter.add(ph);
            //This refreshes the View
            photoAdapter = new photoLayout(this);
            gridView = findViewById(R.id.gridView);
            gridView.setAdapter(photoAdapter);
            Android45.saveInput(Android45.context,Android45.inputFile);
        }
        else{
            return;
        }
    }

    public void openPhoto(View view) {
        System.out.println("DISPLAY PHOTO");
        if(selectedPhoto == null){
            Toast.makeText(PhotosActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedPhoto.getName().isEmpty()){
            Toast.makeText(PhotosActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(PhotosActivity.this, FullScreenActivity.class));
    }

    public void delPhoto(View view) {
        if(selectedPhoto == null){
            Toast.makeText(PhotosActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
            return;
        }
//        confirmation(PhotosActivity.this);
//        if(!Android45.yesFlag)
//            return;
        int index = photos.indexOf(selectedPhoto);
        photos.remove(selectedPhoto);
        currAlbum.delHashPhoto(currAlbum.getName(), selectedPhoto);
        photoAdapter.remove(index);
        //refresh adapter still not working
        //photoAdapter.removePhoto(currAlbum, selectedPhoto);
        //gridView.setAdapter(listAdapter);
        selectedPhoto = null;
        //This refreshes the View
        photoAdapter = new photoLayout(this);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(photoAdapter);
        Android45.saveInput(Android45.context,Android45.inputFile);
    }


    public void getSelection(View view) {
        if(selectedPhoto == null){
            Toast.makeText(PhotosActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(PhotosActivity.this, CopyImageActivity.class));
    }
}
