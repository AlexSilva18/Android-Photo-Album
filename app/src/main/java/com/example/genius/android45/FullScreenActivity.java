package com.example.genius.android45;
//NICK NASTA ALEX SILVA

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FullScreenActivity extends AppCompatActivity {
    private Button saveButton;
    private Button cancelButton;
    private Button addLocation;
    private Button addPerson;
    private Button removePerson;
    private Button removeLocation;
    private EditText editTag;
    private TextView peopleTags;
    private TextView locationTags;
    private ImageView imageView;
    private FloatingActionButton previousImage;
    private FloatingActionButton nextImage;
    private String tagType=null;
    Album currAlbum = Android45.currAlbum;
    Photo selectedPhoto = PhotosActivity.selectedPhoto;
    public static ArrayList<Photo> photos = new ArrayList<>();
    private boolean addTag=false;
    private boolean removeTag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        getIds();
        hideEditButtons();
        imageView.setImageURI(selectedPhoto.photoUri);
        photos=currAlbum.getHashPhotos(currAlbum.getName());
        displayTags();
        Toast.makeText(FullScreenActivity.this, "Scroll Down To Add or Remove Tags", Toast.LENGTH_SHORT).show();
        previousImage.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
                setPreviousImage();
           }
        });
        nextImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setNextImage();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagVal = null;
                if (!editTag.getText().toString().isEmpty()) {
                    tagVal = editTag.getText().toString();
                    tagVal=tagVal.toLowerCase();
                    if(addTag) {
                        if (selectedPhoto.tags.get(tagType).contains(tagVal)) {
                            Toast.makeText(FullScreenActivity.this, "Duplicate Tag", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedPhoto.addTag(tagType, tagVal);
                        Toast.makeText(FullScreenActivity.this, "Tag Successfully Added", Toast.LENGTH_SHORT).show();
                        displayTags();
                        Android45.saveInput(Android45.context,Android45.inputFile);
                    }
                    else if(removeTag){
                        if (!selectedPhoto.tags.get(tagType).contains(tagVal)) {
                            Toast.makeText(FullScreenActivity.this, "Tag Not Found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedPhoto.removeTag(tagType, tagVal);
                        Toast.makeText(FullScreenActivity.this, "Tag Successfully Removed", Toast.LENGTH_SHORT).show();
                        System.out.println(selectedPhoto.getTags().toString());
                        displayTags();
                        Android45.saveInput(Android45.context,Android45.inputFile);
                    }
                } else {
                    Toast.makeText(FullScreenActivity.this, "Empty Tag", Toast.LENGTH_SHORT).show();
                }
                // hides soft keyboard after submit is pressed
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(saveButton.getWindowToken(), 0);
                tagType=null;
                addTag=false;
                removeTag=false;
                hideEditButtons();
                //System.out.println(selectedPhoto.getTags().toString());
               // displayTags();
            }
        });
    }

    private void setPreviousImage(){
        if(photos.indexOf(selectedPhoto)==0 || photos.isEmpty()){
            return;
        }
        else {
            int index = photos.indexOf(selectedPhoto);
            imageView.setImageURI(photos.get(index-1).getPhotoUri());
            selectedPhoto=photos.get(index-1);
            hideEditButtons();
            displayTags();
            return;
        }
    }
    private void setNextImage(){
        if(photos.indexOf(selectedPhoto)==photos.size()-1 || photos.isEmpty()){
            return;
        }
        else {
            int index = photos.indexOf(selectedPhoto);
            imageView.setImageURI(photos.get(index+1).getPhotoUri());
            selectedPhoto=photos.get(index+1);
            hideEditButtons();
            displayTags();
            return;
        }
    }
    private void displayTags(){
        if(selectedPhoto.getTags().get("Person").size()>0) {

            peopleTags.setText(formatTags("Person"));
        }
        if(selectedPhoto.getTags().get("Location").size()>0) {
            locationTags.setText(formatTags("Location"));
        }
        if(selectedPhoto.getTags().get("Person").size()==0){
            peopleTags.setText("People");
        }
        if(selectedPhoto.getTags().get("Location").size()==0){
            locationTags.setText("Locations");
        }
    }
    private String formatTags(String key){
        String ret="";
        for(int i=0;i<selectedPhoto.getTags().get(key).size();i++){
            if(!selectedPhoto.tags.get(key).get(i).equals("")) {
                ret+=("#" + selectedPhoto.tags.get(key).get(i) + " ");
            }
        }
        System.out.print(ret);
        return ret;
    }


    public void addLocation(View view){
        if(saveButton.isActivated()){
            Toast.makeText(FullScreenActivity.this, "Save or Cancel Current Operation", Toast.LENGTH_SHORT).show();
            //return;
        }
        System.out.print("HERE WE ARE");
        showEditButtons();
        tagType="Location";
        addTag=true;
    }
    public void addPerson(View view){
        if(saveButton.isActivated()){
            Toast.makeText(FullScreenActivity.this, "Save or Cancel Current Operation", Toast.LENGTH_SHORT).show();
            return;
        }
        showEditButtons();
        tagType="Person";
        addTag=true;
    }
    public void removePerson(View view){
        if(saveButton.isActivated()){
            Toast.makeText(FullScreenActivity.this, "Save or Cancel Current Operation", Toast.LENGTH_SHORT).show();
            return;
        }
        showEditButtons();
        tagType="Person";
        removeTag=true;
    }
    public void removeLocation(View view){
        if(saveButton.isActivated()){
            Toast.makeText(FullScreenActivity.this, "Save or Cancel Current Operation", Toast.LENGTH_SHORT).show();
            return;
        }
        showEditButtons();
        tagType="Location";
        removeTag=true;
    }

    public void cancel(View view){
        hideEditButtons();
    }
    private void getIds() {
        editTag = findViewById(R.id.editTag);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        addLocation = findViewById(R.id.addLocation);
        addPerson = findViewById(R.id.addPerson);
        removePerson = findViewById(R.id.removePerson);
        removeLocation = findViewById(R.id.removeLocation);
        peopleTags = findViewById(R.id.peopleTags);
        locationTags = findViewById(R.id.locationTags);
        imageView = findViewById(R.id.imageView);
        previousImage = findViewById(R.id.previousImage);
        nextImage = findViewById(R.id.nextImage);
    }
    public void hideEditButtons(){
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setActivated(false);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setActivated(false);
        editTag.setVisibility(View.INVISIBLE);
        editTag.setActivated(false);
        editTag.setText("");
    }
    public void showEditButtons(){
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setActivated(true);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setActivated(true);
        editTag.setVisibility(View.VISIBLE);
        editTag.setActivated(true);
        editTag.setText("");
    }
}
