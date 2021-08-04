package com.example.stegnoapp.stegno.CryptoTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stegnoapp.stegno.CallBacks.CryptCallback;
import com.example.stegnoapp.stegno.Tools.CryptAlgo;
import com.example.stegnoapp.stegno.Tools.Utils;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to decode secret message in image.
 * All the tasks will run in background.
 */
public class Decoding extends AsyncTask<ImageStegno, Void, ImageStegno> {

    //Tag for Log
    private final static String TAG = Decoding.class.getName();

    private final ImageStegno result;
    //Callback interface for AsyncTask
    private final CryptCallback textDecodingCallback;
    private ProgressDialog progressDialog;

    public Decoding(Activity activity, CryptCallback textDecodingCallback) {
        super();
        this.progressDialog = new ProgressDialog(activity);
        this.textDecodingCallback = textDecodingCallback;
        //making result object
        this.result = new ImageStegno();
    }

    //setting progress dialog if wanted
    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    //pre execution of method
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //setting parameters of progress dialog
        if (progressDialog != null) {
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.setTitle("Decoding Message");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

    @Override
    protected void onPostExecute(ImageStegno ImageStegno) {
        super.onPostExecute(ImageStegno);

        //dismiss progress dialog
        if (progressDialog != null)
            progressDialog.dismiss();

        //sending result to callback
        textDecodingCallback.onEncoded(result);
    }

    @Override
    protected ImageStegno doInBackground(ImageStegno... imageSteganographies) {

        //If it is not already decoded
        if (imageSteganographies.length > 0) {

            ImageStegno ImageStegno = imageSteganographies[0];

            //getting bitmap image from file
            Bitmap bitmap = ImageStegno.getImage();

            //return null if bitmap is null
//            if (bitmap == null)
//                return null;

            //splitting images
            List<Bitmap> srcEncodedList = Utils.splitImage(bitmap);

            //decoding encrypted zipped message
            String decoded_message = EnDeCode.decodeMessage(srcEncodedList);

            Log.d(TAG, "Decoded_Message : " + decoded_message);

            //text decoded = true
            if (!Utils.stringEmpty(decoded_message)) {
                result.setDecrypted(true);
            }

            //decrypting the encoded message
            String decrypted_message = ImageStegno.decryptMessage(decoded_message, ImageStegno.getKey());
            Log.d(TAG, "Decrypted message : " + decrypted_message);

            //If decrypted_message is null it means that the secret key is wrong otherwise secret key is right.
            if (!Utils.stringEmpty(decrypted_message)) {

                //secret key provided is right
                result.setWrongKey(false);

                // Set Results

                result.setMessage(decrypted_message);


                //free memory
                for (Bitmap bitm : srcEncodedList)
                    bitm.recycle();

                //Java Garbage Collector
                System.gc();
            }
        }

        return result;
    }
}