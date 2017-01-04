package com.example.juliuscassin.photoalbum;

import java.util.ArrayList;

/**
 * Created by Julius Cassin on 4/30/2016.
 */
public class Album {

    String name;
    ArrayList<Photo> photos;

    //constructor
    public Album(String name){
        this.name = name;
        photos = new ArrayList<Photo>();
    }

    //return album name
    public String toString(){
        return this.name;
    }

    //attempt to add photo to album. If duplicate exists return false
    public boolean addPhoto(Photo newPhoto){
        //check if photo already exists
        for(Photo existingPhoto: photos){
            if(newPhoto.equals(existingPhoto)) {
                //photo already exists
                return false;
            }
        }
        //doesn't alrady exists add
        photos.add(newPhoto);
        return true;
    }

    //delete a known photo from album
    public void deletePhoto(Photo photo){
        for(Photo existingPhoto: photos){
            if(existingPhoto.equals(photo)){
                //found now delete
               photos.remove(existingPhoto);
                return;
            }
        }
    }

    //get photo given pathname
    public Photo getPhoto(String photoIdentifier){
        for(Photo photo: photos){
            if(photo.pathName.equals(photoIdentifier)){
                return photo;
            }
        }
        return null;
    }

    //for storage
    public String getString(){
        return "album" + "\n" + name;
    }
}
