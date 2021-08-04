package com.example.stegnoapp.stegno.CryptoTask;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.stegnoapp.stegno.Tools.CryptAlgo;
import com.example.stegnoapp.stegno.Tools.Utils;

// The class for text stegno
public class ImageStegno {
    // Log tagging
    private static final String Tag = ImageStegno.class.getName();

    private String message;
    private String key;
    private String encryptedMessage;
    private Bitmap image;
    private Bitmap encodedImage;
    private byte[] encryptZip;
    private Boolean encrypted;
    private Boolean decrypted;
    private Boolean wrongKey;

    public ImageStegno() {
        this.encryptZip = new byte[0];
        //
        this.decrypted = false;
        this.encrypted = false;
        this.wrongKey = true;
        //
        this.message = "";
        this.key = "";
        this.encryptedMessage = "";
        //
        this.image = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        this.encodedImage = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);

    }

    public ImageStegno(String key, String message, Bitmap image){
        this.message = message;
        this.key = key;
        this.image = image;

        this.encryptedMessage = encryptMessage(message, key);
        this.encryptZip = message.getBytes();

        this.wrongKey = true;
        this.encrypted = false;
        this.decrypted = false;

        this.encodedImage = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
    }

    public ImageStegno(String key, Bitmap image){
        this.image = image;
        this.key = key;

        this.wrongKey = true;
        this.encrypted = false;
        this.decrypted = false;

        this.message = "";
        this.key = "";
        this.encryptedMessage = "";
        this.encodedImage = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        this.encryptZip = new byte[0];
    }

    private static String encryptMessage(String message, String key){
        Log.d(Tag, "Encrypting Message: " + message);

        String encryptMessage = "";
        if(message != null){
            if(!Utils.stringEmpty(key)){
                try {
                    encryptMessage = CryptAlgo.encryptMessage(message, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                encryptMessage = message;
            }
        }
        Log.d(Tag, "Encrypt Message: " + encryptMessage);
        return encryptMessage;
    }

    public static String decryptMessage(String message, String key){
        Log.d(Tag, "Decrypting Message: " + message);
        String decryptMessage = "";
        if(message != null){
            if(!Utils.stringEmpty(key)){
                try {
                    decryptMessage = CryptAlgo.decryptMessage(message, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                decryptMessage = message;
            }
        }
        Log.d(Tag, "Decrypt Message: " + decryptMessage);
        return decryptMessage;
    }

    private static String keyTo128bit(String key){
        StringBuilder res = new StringBuilder(key);

        if (key.length() <= 16) {
            for (int i = 0; i < (16 - key.length()); i++) {
                res.append("*");
            }
        } else {
            res = new StringBuilder(res.substring(0, 15));
        }

        Log.d(Tag, "Secret Key Length : " + res.toString().getBytes().length);

        return res.toString();
    }
    // Getters & Setters
    public Boolean getDecrypted() {
        return decrypted;
    }
    public void setDecrypted(Boolean decrypted) {
        this.decrypted = decrypted;
    }
    //
    public Boolean getEncrypted() {
        return encrypted;
    }
    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }
    //
    public Boolean getWrongKey() {
        return wrongKey;
    }

    public void setWrongKey(Boolean wrongKey) {
        this.wrongKey = wrongKey;
    }
    /////////////
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    //
    public String getEncryptedMessage() {
        return encryptedMessage;
    }
    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
    //
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    ////////////
    public Bitmap getEncodedImage() {
        return encodedImage;
    }
    public void setEncodedImage(Bitmap encodedImage) {
        this.encodedImage = encodedImage;
    }
    //
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    ////////////
    public byte[] getEncryptZip() {
        return encryptZip;
    }
    public void setEncryptZip(byte[] encryptZip) {
        this.encryptZip = encryptZip;
    }
}
