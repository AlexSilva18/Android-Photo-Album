package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Android45 extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<Album> listAdapter;
    protected static ArrayList<Album> albums = new ArrayList<>();
    private Album selectedAlbum = null;
    static public Album currAlbum = null;
    static boolean yesFlag = false;
    static boolean response = false;
    private final static int readStorageResult=0;
    boolean appendFile = false;
    protected static final String inputDir = "sampleData";
    protected static final String inputFile = "input.dat";
    Button saveButton;
    EditText editAlbum;
    Button cancelEdit;
    ImageButton searchButton;
    public static Context context;

    @Override
    public void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);
        getIds();
        hideEditButtons();
        readExternalStoragePermission();
        Toast.makeText(Android45.this, R.string.clickandhold, Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.mainListView);
        //if(albums.size()==0) {
        //   makeStock();

       // }
        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, albums);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedItem = (Album) parent.getItemAtPosition(position);
                selectedAlbum = selectedItem;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedItem = (Album) parent.getItemAtPosition(position);
                selectedAlbum = selectedItem;
                openAlbum();
                return true;
            }
        });
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        saveInput(context,inputFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIds();
        hideEditButtons();
        readExternalStoragePermission();
        context= getApplicationContext();
        Toast.makeText(Android45.this, R.string.clickandhold, Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.mainListView);
//        Album[] albumList = new Album[] {new Album("stock1"),
//                new Album("stock2"),
//                new Album("stock3")};

//        albums.addAll(Arrays.asList(albumList));
        if(albums.size()==0) {
            readFromFile(context);
            //makeStock();
        }
        //if(albums.size()==0) {
         //   makeStock();

        //}


        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, albums);


        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedItem = (Album) parent.getItemAtPosition(position);
                selectedAlbum = selectedItem;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedItem = (Album) parent.getItemAtPosition(position);
                selectedAlbum = selectedItem;
                openAlbum();
                return true;
            }
        });


//        listView.setAdapter(
//                new ArrayAdapter<>(this, R.layout.activity_main, albums)
//        );
        //   albums = getResources().getStringArray(R.array.)
        saveInput(context,inputFile);

    }

    private void makeStock(){
        Album stockAlbum = new Album("stock");
        ArrayList<Photo> stockPhotos = stockAlbum.getHashPhotos(stockAlbum.getName());
        Uri uri = Uri.parse("android.resource://" + "com.example.genius.android45/" + R.drawable.stock1);
        Photo ph1 = new Photo(uri, "stock1");
        stockAlbum.addHashPhotos(stockAlbum.name, ph1);
        ph1.addTag("Location", "waterfall");
        uri = Uri.parse("android.resource://" + "com.example.genius.android45/" + R.drawable.stock2);
        Photo ph2 = new Photo(uri, "stock2");
        ph2.addTag("Location", "wooded path");
        stockAlbum.addHashPhotos(stockAlbum.name, ph2);
        uri = Uri.parse("android.resource://" + "com.example.genius.android45/" + R.drawable.stock3);
        Photo ph3 = new Photo(uri, "stock3");
        stockAlbum.addHashPhotos(stockAlbum.name, ph3);
        ph3.addTag("Person", "tom");
        ph3.addTag("Location", "forest");
        uri = Uri.parse("android.resource://" + "com.example.genius.android45/" + R.drawable.stock4);
        Photo ph4 = new Photo(uri, "stock4");
        ph4.addTag("Location", "foggy lake");
        stockAlbum.addHashPhotos(stockAlbum.name, ph4);
        uri = Uri.parse("android.resource://" + "com.example.genius.android45/" + R.drawable.stock5);
        Photo ph5 = new Photo(uri, "stock5");
        ph5.addTag("Location", "mountain lake");
        stockAlbum.addHashPhotos(stockAlbum.name, ph5);
        albums.add(stockAlbum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.exitApp: //code here
                Toast.makeText(Android45.this, "Have A Nice Day!!", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void addAlbum(View view) {
        hideEditButtons();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Android45.this);
        final View addAlbumView = getLayoutInflater().inflate(R.layout.add_album, null);
        final EditText addAlbum = addAlbumView.findViewById(R.id.albumName);

        final Button submitAlbum = addAlbumView.findViewById(R.id.submitAlbum);

        submitAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumName = null;
                if(!addAlbum.getText().toString().isEmpty()){
                    albumName = addAlbum.getText().toString();
                    for (Album a : albums){
                        if(albumName.equals(a.getName())){
                            Toast.makeText(Android45.this, R.string.error_duplicate_msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Album newAlbum = new Album(albumName);
                    albums.add(newAlbum);
                    newAlbum.addHashAlbum(albumName);
                    //listAdapter.add(albumName);
                    listView.setAdapter(listAdapter);
                    saveInput(context,inputFile);
                    selectedAlbum = null;
                    addAlbum.setText("");
                    Toast.makeText(Android45.this, "Album Successfully Added", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Android45.this, R.string.empty_field, Toast.LENGTH_SHORT).show();
                    System.out.println("EMPTY FIELD");
                }
                // hides soft keyboard after submit is pressed
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(submitAlbum.getWindowToken(), 0);
            }
        });
        addAlbumView.setVisibility(View.VISIBLE);
        mBuilder.setView(addAlbumView);
        mBuilder.show();
        selectedAlbum = null;

//        dialog = mBuilder.create();
//        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case readStorageResult:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }

    private void readExternalStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"APP MUST VIEW THUMBNAILS",Toast.LENGTH_SHORT);
            }
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, readStorageResult);
        }
    }


    public void delAlbum(View view) {
        hideEditButtons();
        if(selectedAlbum == null){
            Toast.makeText(Android45.this, R.string.noSelection, Toast.LENGTH_SHORT).show();
            return;
        }
//        confirmation(Android45.this);
//        if(!yesFlag)
//            return;
        albums.remove(selectedAlbum);
        listView.setAdapter(listAdapter);
        selectedAlbum = null;
        saveInput(context,inputFile);
    }

    public void editAlbum(View view) {
        editAlbum = findViewById(R.id.editAlbumName);
        if(selectedAlbum == null){
            Toast.makeText(Android45.this, R.string.noSelection, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!selectedAlbum.getName().isEmpty()){
            editAlbum.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            cancelEdit.setVisibility(View.VISIBLE);
            editAlbum.setText(selectedAlbum.getName());
            final Button saveButton = findViewById(R.id.saveAlbum);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editAlbum.getText().toString().equals(selectedAlbum.getName())){
                        Toast.makeText(Android45.this, R.string.error_duplicate_msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int index = albums.indexOf(selectedAlbum);
                    Album selectedAlbum = albums.get(index);
                    String newAlbum = editAlbum.getText().toString();
                    for(Album a : albums){
                        if(newAlbum.equals(a.getName())){
                            //selectedAlbum = null;
                            Toast.makeText(Android45.this, R.string.error_duplicate_msg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    selectedAlbum.renameHashAlbum(newAlbum, selectedAlbum.getName());
                    selectedAlbum.setName(newAlbum);
                    listView.setAdapter(listAdapter);
                    selectedAlbum = null;
                    hideEditButtons();
                    Toast.makeText(Android45.this, R.string.renamesuccess, Toast.LENGTH_SHORT).show();
                    // hides soft keyboard after saved item
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(editAlbum.getWindowToken(), 0);
                    saveInput(context,inputFile);
                }
            });
            cancelEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideEditButtons();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(cancelEdit.getWindowToken(), 0);
                }
            });
        }
        else{
            Toast.makeText(Android45.this, R.string.noSelection, Toast.LENGTH_SHORT).show();
        }
    }

    public void openAlbum() {
        System.out.println("OPEN ALBUM");
        if(selectedAlbum == null){
            Toast.makeText(Android45.this, R.string.noSelection, Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedAlbum.getName().isEmpty()){
            Toast.makeText(Android45.this, R.string.noSelection, Toast.LENGTH_SHORT).show();
            return;
        }
        currAlbum = selectedAlbum;
        startActivity(new Intent(Android45.this, PhotosActivity.class));
    }



    public void searchAlbum(View view) {
        selectedAlbum = null;
        startActivity(new Intent(Android45.this, SearchActivity.class));
    }

    private void getIds() {
        editAlbum = findViewById(R.id.editAlbumName);
        saveButton = findViewById(R.id.saveAlbum);
        cancelEdit = findViewById(R.id.cancelEdit);
        searchButton = findViewById(R.id.search);
    }

    public void hideEditButtons(){
        saveButton.setVisibility(View.INVISIBLE);
        editAlbum.setVisibility(View.INVISIBLE);
        cancelEdit.setVisibility(View.INVISIBLE);
        editAlbum.setText("");
    }

    public static void confirmation(Context c){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        yesFlag = true;
                        response = true;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void readFromFile(Context context) {

        ArrayList<Album> toUri =new ArrayList<>();

        File dataFile = new File(context.getFilesDir(),inputDir);
        try{
            if(!dataFile.exists()){
                dataFile.mkdir();
                 //System.out.println("new .dat created");
            }
            else{
                File data = new File(dataFile,inputFile);
                ObjectInputStream inStream = new ObjectInputStream(
                        new FileInputStream(data));

                toUri = (ArrayList<Album>) inStream.readObject();
                inStream.close();
                //System.out.println("inserted into hashMap");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(toUri.size()!=0) {
            ArrayList<Photo> convert;
            for (Album a : toUri) {
                Album hold = new Album();
                ArrayList<Photo> converted = new ArrayList<>();
                convert = a.getHashPhotos(a.getName());
                hold.setName(a.getName());
                for (int i = 0; i < convert.size(); i++) {
                    String convertedUri = convert.get(i).getUri().toString();
                    Uri temp = Uri.parse(convertedUri);
                    Photo ph = new Photo(temp, convert.get(i).getName());
                    ph.tags=convert.get(i).getTags();
                    converted.add(ph);
                }
                hold.photoAlbums.put(hold.getName(), converted);
                albums.add(hold);
            }
        }
        else if(toUri.size()==0){
            makeStock();
        }

        //for(int i=0;i<albums.size();i++){
        //    System.out.println("albumListSaved: " + " Album: "+ albums.get(i).toString()+" "+albums.get(i).getHashPhotos(albums.get(i).getName()));
        //    System.out.println(i);
       //}

    }

    public static void saveInput(Context context, String fileName){
        ArrayList<Album> save = new ArrayList<>();
        ArrayList<Photo> convert;
        for (Album a : albums) {
            Album hold = new Album();
            ArrayList<Photo> converted = new ArrayList<>();
            convert =a.getHashPhotos(a.getName());
            hold.setName(a.getName());
            for(int i=0;i<convert.size();i++){
                Uri temp =convert.get(i).getPhotoUri();
                String convertedUri = temp.toString();
                Photo ph = new Photo(convertedUri,convert.get(i).getName());
                ph.tags=convert.get(i).getTags();
                converted.add(ph);
            }
            hold.photoAlbums.put(hold.getName(),converted);
            save.add(hold);
        }

        //for(int i=0;i<save.size();i++){
        //    System.out.println("albumListSaved: " + " Album: "+ save.get(i).toString()+" "+save.get(i).getHashPhotos(save.get(i).getName()));
        //    System.out.println(i);
       // }

        File input = new File(context.getFilesDir(),inputDir);
        if (!input.exists()){
            input.mkdir();
        }
        try{
            File file = new File(input,fileName);
            ObjectOutputStream outStream = new ObjectOutputStream(
                    new FileOutputStream(file)
            );

            outStream.writeObject(save);
            outStream.close();
            // System.out.println("INPUT SAVED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
