package com.example.genius.android45;
//NICK NASTA ALEX SILVA
import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Photo implements Serializable  {
	protected String name;
	protected HashMap<String,ArrayList<String>> tags = new HashMap<String,ArrayList<String>>();
	protected int photoId;
	protected String caption;
	protected Uri photoUri;
	protected String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Photo(String uri, String name){
		this.uri=uri;
		this.name = name;
	}
	public Photo(Integer photoId, String name){
		this.photoId = photoId;
		this.name = name;
		this.caption=null;
		this.tags = new HashMap<>();
		this.tags.put("Person",new ArrayList<String>());
		this.tags.put("Location",new ArrayList<String>());
	}
	public Photo(Uri photoUri, String name){
		this.photoUri = photoUri;
		this.name = name;
		this.caption=null;
		this.tags = new HashMap<>();
		this.tags.put("Person",new ArrayList<String>());
		this.tags.put("Location",new ArrayList<String>());
	}
//    public Photo(Integer file, String name, String caption, HashMap<String,ArrayList<String>> tags){
//        this.photoFile = file;
//        this.name = name;
//        this.caption=caption;
//    }

	public HashMap<String, ArrayList<String>> getTags() {
		return tags;
	}

	public void setTags(HashMap<String, ArrayList<String>> tags) {
		this.tags = tags;
	}

	public void removeTag(String key, String tagVal){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(tagVal);
		if(tags.isEmpty()){
			return;
		}
		if(tags.containsKey(key) && tags.get(key).contains(tagVal)){
			tags.get(key).remove(tagVal);
		}
		else{
			return;
		}
	}

	public void addTag(String key, String tagVal){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(tagVal);

		if(!tags.isEmpty()){
			if(tags.containsKey(key)){
				tags.get(key).add(tagVal);
				return;
			}
			else{
				tags.put(key, temp);
				return;
			}
		}
		else{
			tags.put(key, temp);
			return;

		}
	}

	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public void setPhotoName(String name){
		this.name = name;
	}

	public String getPhotoName() {
		return this.name;
	}

	public int getPhotoId(){
		return this.photoId;
	}


	private String trimmedName(){
		return this.name.split("\\.")[0];
	}

	@Override
	public String toString(){
		return trimmedName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {

		return name;
	}

	public Uri getPhotoUri() {

		return photoUri;
	}
}
