package com.treinamento.mdomingos.startapp.utils;

public class UploadStorage {
    private String imageUrl;

    public UploadStorage(){}

    public  UploadStorage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
