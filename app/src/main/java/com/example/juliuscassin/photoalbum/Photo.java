package com.example.juliuscassin.photoalbum;

import android.graphics.Bitmap;

/**
 * Created by Julius Cassin on 5/2/2016.
 */
public class Photo {

    //File path name
    String pathName;
    //bitmap photo
    Bitmap bitMap;
    //Tags
    String person=null;
    String location=null;

    public Photo(String pathName, Bitmap bitMap){
        this.pathName = pathName;
        this.bitMap = bitMap;
    }

    public Photo(String pathName, Bitmap bitMap, String person, String location){
        this.pathName = pathName;
        this.bitMap = bitMap;
        this.person = person;
        this.location = location;
    }

    //compare photos by comparing pathnames
    public boolean equals(Photo photo){
        if(this.pathName.equals(photo.pathName)){
            return true;
        }
        else{
            return false;
        }
    }

    //to set tags
    public void setPersonTag(String person){
        this.person=person;
    }
    public void setLocationTag(String location){
        this.location = location;
    }

    //To delete tags
    public void deletePersonTag(){
        this.person = null;
    }
    public void deleteLocationTag(){
        this.location = null;
    }

    //used for storage
    public String getString(){
        return "photo" + "\n" + pathName + "|" + person + "|" +  location;
    }
}
