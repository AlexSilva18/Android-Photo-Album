package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CopyImageActivity extends AppCompatActivity {
    private Button copyImageButton;
    private Button moveImageButton;
    private Button cancelImageButton;
    private ListView listView;
    private ArrayAdapter<Album> listAdapter;
    private static ArrayList<Album> albums = Android45.albums;
    Album selectedAlbum;
    Album currAlbum = Android45.currAlbum;
    Photo selectedPhoto = PhotosActivity.selectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_image);
        listView=findViewById(R.id.albumList);
        copyImageButton = findViewById(R.id.copyImageButton);
        moveImageButton =findViewById(R.id.moveImageButton);
        cancelImageButton = findViewById(R.id.cancelButton);
        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, albums);
        listView.setAdapter(listAdapter);
        Toast.makeText(CopyImageActivity.this, "Tap To Select An Album", Toast.LENGTH_SHORT).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedItem = (Album) parent.getItemAtPosition(position);
                selectedAlbum = selectedItem;
            }
        });
    }

    public void cancel(View view){
        startActivity(new Intent(CopyImageActivity.this, PhotosActivity.class));
    }
    public void copyImage(View view){
        if(selectedAlbum!=null) {
            Photo ph = selectedPhoto;
            for(Photo p:selectedAlbum.getHashPhotos(selectedAlbum.getName())){
                if(p.getPhotoUri().equals(ph.getPhotoUri())){
                    Toast.makeText(CopyImageActivity.this, "Picture Already In Album", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            selectedAlbum.addHashPhotos(selectedAlbum.getName(), selectedPhoto);
            Toast.makeText(CopyImageActivity.this, "Photo Successfully Copied", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CopyImageActivity.this, PhotosActivity.class));
            Android45.saveInput(Android45.context,Android45.inputFile);
        }
        else{
            Toast.makeText(CopyImageActivity.this, "No Album Selected", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void moveImage(View view){
        if(selectedAlbum!=null) {
            Photo ph = selectedPhoto;
            for(Photo p:selectedAlbum.getHashPhotos(selectedAlbum.getName())){
                if(p.getPhotoUri().equals(ph.getPhotoUri())){
                    Toast.makeText(CopyImageActivity.this, "Picture Already In Album", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            selectedAlbum.addHashPhotos(selectedAlbum.getName(), selectedPhoto);
            currAlbum.delHashPhoto(currAlbum.getName(), selectedPhoto);
            Toast.makeText(CopyImageActivity.this, "Photo Successfully Moved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CopyImageActivity.this, PhotosActivity.class));
            Android45.saveInput(Android45.context,Android45.inputFile);
        }
        else{
            Toast.makeText(CopyImageActivity.this, "No Album Selected", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
