package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

// with serializable we can store all the content (albums and photos) of @photoAlbums into a photoFile to export and import
public class Album implements Serializable {
	protected String name;
	//protected String description;
    protected int numPhotos;
    protected String startDate;
    protected String endDate;
    protected static String currAlbum;
	protected ArrayList<Photo> photoPaths;

	public HashMap<String, ArrayList<Photo>> photoAlbums = new HashMap<>();

    // (must be in album Interface if i'm not mistaken)


	public Album(){};

//	public Album album (String name, String description,ArrayList<File> photoPaths){
//		this.name=name;
//		this.description=description;
//		this.photoPaths=photoPaths;
//		return this;
//	}
    public Album (String name){
        this.name = name;
        photoAlbums.put(name, new ArrayList<Photo>());
	}
    public Album (String name, int numPhotos, String startDate, String endDate){
        this.name = name;
        this.numPhotos = numPhotos;
        this.startDate = startDate;
        this.endDate = endDate;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumPhotos(int numPhotos){ this.numPhotos = numPhotos;
	}
	public int getNumPhotos(){ return this.numPhotos;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
		}
	public String getStartDate(){
		return this.startDate;
		}

	public String getEndDate(){
		return this.endDate;
		}
    public void setEndDate(String endDate){
    	this.endDate = endDate;
    	}

	public void setCurrAlbum(String currAlbum){this.currAlbum = currAlbum;}
    public String getCurrentAlbum(){ return this.currAlbum;}

    public HashMap<String, ArrayList<Photo>> getFullHashAlbum(){ return this.photoAlbums;}

	/** retrieves the users albums in the HashMap
	 * returns of Set containing all the albums
	 * @return
	 */
	public Set<String> getHashAlbum(){
		return photoAlbums.keySet();
	}

	/** add album to hashTable with empty Photo List
	 * gets logged in user to find the specific album
	 * @param albumName
	 */
	public void addHashAlbum(String albumName){
		photoAlbums.put(albumName, new ArrayList<Photo>());
	}

	/** delete album from HashTable
     * @param albumName
     */
	public void delHashAlbum(String albumName){
		photoAlbums.remove(albumName);
	}

	/** add album to hashTable with empty Photo List
	 * gets logged in user to find the specific album
	 * @param albumName
	 */
	public void renameHashAlbum(String albumName, String removeKey){

		//HashMap<String, ArrayList<Photo>> temp = new HashMap<>();
		//temp = photoAlbums;
		ArrayList<Photo> tempPhotos = photoAlbums.get(removeKey);
		//System.out.println(tempPhotos.toString());
//		temp.remove(removeKey);
		photoAlbums.remove(removeKey);
		//System.out.println(tempPhotos.toString());
		//temp.put(albumName, new ArrayList<Photo>());
		//photoAlbums.put(this.username, temp);
		//photoAlbums.put(albumName, new ArrayList<Photo>());
		photoAlbums.put(albumName, tempPhotos);
		//System.out.println(this.photoAlbums.values().toString());
	}


	/** return the users photos from a single album
	 * @param album
	 * @return
	 */
	public ArrayList<Photo> getHashPhotos(String album){
		return photoAlbums.get(album);
	}

	/** Add photo to HashTable
	 * @param album
	 * @param photo
	 */
	public void addHashPhotos(String album, Photo photo){
    	ArrayList<Photo> tempPhotos = photoAlbums.get(album);
    	tempPhotos.add(photo);
    	photoAlbums.put(album, tempPhotos);
	}

	/** Delete photo from HashTable
	 * @param album
	 * @param photo
	 */
	public void delHashPhoto(String album, Photo photo){
		photoAlbums.get(album).remove(photo);
	}






//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

    @Override
    public String toString(){
        return this.getName(); //+ "\t" + this.getNumPhotos() + "\t" +  this.getEndDate() + "\t" +  this.getStartDate();
        //return this.getName();
    }
}
