package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    EditText personTag;
    EditText locationTag;
    ImageView searchView;
    TextView peopleTags;
    TextView locationTags;

    public HashMap<String, ArrayList<Photo>> allAlbums = new HashMap<>();
    public static ArrayList<Photo> searchedPhotos = new ArrayList<>();
    ArrayList<String> albumNames = new ArrayList<>();
    private FloatingActionButton searchPreviousImage;
    private FloatingActionButton searchNextImage;
    Photo selectedPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchInitialize(0);
        searchedPhotos.clear();
        for (Album a : Android45.albums){
            allAlbums.put(a.name, a.getHashPhotos(a.name));
        }

        for (Object o : allAlbums.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            albumNames.add((String) pair.getKey());
        }

        searchPreviousImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreviousImage();
            }
        });
        searchNextImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setNextImage();
            }
        });



//        searchView.setImageURI(selectedPhoto.photoUri);
        //photos=currAlbum.getHashPhotos(currAlbum.getName());
//        displayTags();

        //configureBackButton();
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
                startActivity(new Intent(SearchActivity.this, Android45.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
    private void searchInitialize(int flag) {
        if(flag == 1){
            searchPreviousImage.setVisibility(View.VISIBLE);
            searchNextImage.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            peopleTags.setVisibility(View.VISIBLE);
            locationTags.setVisibility(View.VISIBLE);
            return;
        }
        personTag = findViewById(R.id.personTag);
        locationTag = findViewById(R.id.locationTag);
        searchView = findViewById(R.id.searchView);
        searchPreviousImage = findViewById(R.id.previousSearchImage);
        searchNextImage = findViewById(R.id.nextSearchImage);
        searchView = findViewById(R.id.searchView);
        peopleTags = findViewById(R.id.searchPplTags);
        locationTags = findViewById(R.id.searchLocTags);

        searchView.setVisibility(View.INVISIBLE);
        peopleTags.setVisibility(View.INVISIBLE);
        locationTags.setVisibility(View.INVISIBLE);
        searchPreviousImage.setVisibility(View.INVISIBLE);
        searchNextImage.setVisibility(View.INVISIBLE);
    }

//    private void configureBackButton(){
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    public void addSearchAlbum(View view) {
        if(!searchedPhotos.isEmpty()){
            Random rand = new Random();

            for(String s : albumNames){
                System.out.println("albums: " + s);
                int c = rand.nextInt(50)+1;
                String albumName = "newAlbum" + c;
                if(!s.equals(albumName)){
                    Album newAlbum = new Album(albumName);
                    Android45.albums.add(newAlbum);
                    for(Photo p:searchedPhotos){
                        newAlbum.addHashPhotos(newAlbum.getName(),p);
                    }
                    HashMap<String, ArrayList<Photo>> hashAlbums = newAlbum.getFullHashAlbum();
                    System.out.println("hashAlbums: " + hashAlbums);
                    allAlbums.put(albumName, newAlbum.getHashPhotos(albumName));
                    Toast.makeText(SearchActivity.this, "New Album Created", Toast.LENGTH_SHORT).show();
                    Android45.saveInput(Android45.context,Android45.inputFile);
                    return;
                }
//                    Toast.makeText(SearchActivity.this, "Duplicate Album", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(SearchActivity.this, "Search Results Is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void delSearchPhoto(View view) {
        int index = searchedPhotos.indexOf(selectedPhoto);
        searchedPhotos.remove(selectedPhoto);
        if(searchedPhotos.size()==0){
            setEmpty();
        }
        else {
            setNextImage();
        }
    }

    public void startSearch(View view) {
        //System.out.println("people: " + !personTag.getText().toString().isEmpty() + " location: " + !locationTag.getText().toString().isEmpty());
        if(!personTag.getText().toString().isEmpty() && !locationTag.getText().toString().isEmpty()){
            search(personTag.getText().toString().toLowerCase(), locationTag.getText().toString().toLowerCase(), true, true);
        }
        else if (!personTag.getText().toString().isEmpty() && locationTag.getText().toString().isEmpty()) {
            search(personTag.getText().toString().toLowerCase(), null, true, false);
        }
        else if (!locationTag.getText().toString().isEmpty() && personTag.getText().toString().isEmpty()){
            search(null, locationTag.getText().toString().toLowerCase(), false, true);
        }
        else {
            Toast.makeText(SearchActivity.this, "No Tags have been found", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("SearchedPhotos: " + searchedPhotos);
        if(!searchedPhotos.isEmpty()){
            selectedPhoto = searchedPhotos.get(0);
            System.out.println("selectedPhoto: " + selectedPhoto);
            searchView.setImageURI(selectedPhoto.photoUri);
            searchInitialize(1);
            displayTags();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        else{
            Toast.makeText(SearchActivity.this, "No Photos Tags Found", Toast.LENGTH_SHORT).show();
            //return;
        }
    }

    private void search(String peopleTag, String locationTag, boolean peopleFlag, boolean locationFlag) {
        ArrayList<Photo> tempPhotos = new ArrayList<>();
        for(String a : albumNames){
            tempPhotos.addAll(Objects.requireNonNull(allAlbums.get(a)));
            //allAlbums.get(a)
        }
        if(tempPhotos.get(0) == null){
            Toast.makeText(SearchActivity.this, "No Photos Found", Toast.LENGTH_SHORT).show();
            return;
        }
        //System.out.println("tempPhotos: " + tempPhotos);
        for(Photo p : tempPhotos){
            ArrayList<String> peopleTags = p.getTags().get("Person");
            ArrayList<String> locationTags = p.getTags().get("Location");
            if(peopleFlag && locationFlag) {
                //ArrayList<String> peopleTags = p.getTags().get("People");
                //ArrayList<String> locationTags = p.getTags().get("Location");
                if(peopleTags == null && locationTags == null){
                    continue;
                }
                if(peopleTags != null) {
                    for (String t : peopleTags) {
                        if (t.contains(peopleTag) && !searchedPhotos.contains(p)) {
                            System.out.println("peopleTags: " + peopleTags + " locationTags: " + locationTags);
                            searchedPhotos.add(p);
                        }
                    }
                }
                if(locationTags != null) {
                    for (String t : locationTags) {
                        if (t.contains(locationTag) && !searchedPhotos.contains(p)) {
                            searchedPhotos.add(p);
                        }
                    }
                }
            }
            else if(peopleFlag && !locationFlag){
                //ArrayList<String> photoTags = p.getTags().get("Person");
                if(peopleTags == null){
                    continue;
                }
                for (String t : peopleTags){
                    if(t.contains(peopleTag) && !searchedPhotos.contains(p)){
                        searchedPhotos.add(p);
                    }
                }
            }
            else if(locationFlag && !peopleFlag){
                //ArrayList<String> photoTags = p.getTags().get("Location");
                if(locationTags == null){
                    continue;
                }
                for (String t : locationTags){
                    if(t.contains(locationTag) && !searchedPhotos.contains(p)){
                        searchedPhotos.add(p);
                    }
                }
            }
            else{
                Toast.makeText(SearchActivity.this, "Unable To Find Tags", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setPreviousImage(){
        if(searchedPhotos.indexOf(selectedPhoto)==0 || searchedPhotos.isEmpty()){
            return;
        }
        else {
            int index = searchedPhotos.indexOf(selectedPhoto);
            searchView.setImageURI(searchedPhotos.get(index-1).getPhotoUri());
            selectedPhoto = searchedPhotos.get(index-1);
            displayTags();
            return;
        }
    }
    private void setNextImage(){
        if(searchedPhotos.indexOf(selectedPhoto)==searchedPhotos.size()-1 || searchedPhotos.isEmpty()){
            return;
        }
        else {
            int index = searchedPhotos.indexOf(selectedPhoto);
            searchView.setImageURI(searchedPhotos.get(index+1).getPhotoUri());
            selectedPhoto=searchedPhotos.get(index+1);
            displayTags();
            return;
        }
    }

    private void setEmpty(){
        peopleTags.setVisibility(View.INVISIBLE);
        locationTags.setVisibility(View.INVISIBLE);
        searchView.setImageURI(null);
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
}
