package com.example.stegnoapp.stegno.CryptoTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stegnoapp.stegno.CallBacks.CryptCallback;
import com.example.stegnoapp.stegno.Tools.Utils;
import com.example.stegnoapp.ui.gallery.GalleryFragment;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to encode secret message in image.
 * All the tasks will run in background.
 */
public class Encoding extends AsyncTask<ImageStegno, Integer, ImageStegno> {

    //Tag for Log
    private static final String TAG = Encoding.class.getName();

    private final ImageStegno result;
    //Callback interface for AsyncTask
    private final CryptCallback callbackInterface;
    private int maximumProgress;
    // private final ProgressDialog progressDialog;

    public Encoding(GalleryFragment activity, CryptCallback callbackInterface) {
        super();
        // this.progressDialog = new ProgressDialog(activity);
        this.callbackInterface = callbackInterface;
        //making result object
        this.result = new ImageStegno();
    }

    //pre execution of method
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //setting parameters of progress dialog
//        if (progressDialog != null) {
//            progressDialog.setMessage("Loading, Please Wait...");
//            progressDialog.setTitle("Encoding Message");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
    }

    @Override
    protected void onPostExecute(ImageStegno textStegnography) {
        super.onPostExecute(textStegnography);

        //dismiss progress dialog
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }

        //Sending result to callback interface
        callbackInterface.onEncoded(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        //Updating progress dialog
//        if (progressDialog != null) {
//            progressDialog.incrementProgressBy(values[0]);
//        }
    }

    @Override
    protected ImageStegno doInBackground(ImageStegno... imageSteganographies) {

        maximumProgress = 0;

        if (imageSteganographies.length > 0) {

            ImageStegno textStegnography = imageSteganographies[0];

            //getting image bitmap
            Bitmap bitmap = textStegnography.getImage();

            //getting height and width of original image
            int originalHeight = bitmap.getHeight();
            int originalWidth = bitmap.getWidth();

            //splitting bitmap
            List<Bitmap> src_list = Utils.splitImage(bitmap);

            //encoding encrypted compressed message into image

            List<Bitmap> encoded_list = EnDeCode.encodeMessage(src_list, textStegnography.getEncryptedMessage(), new EnDeCode.ProgressHandler() {
                        @Override
                        public void setTotal(int tot) {
                            maximumProgress = tot;
                            // progressDialog.setMax(maximumProgress);
                            Log.d(TAG, "Tatal Length: " + tot);
                        }

                        @Override
                        public void increment(int inc) {
                            publishProgress(inc);
                        }

                        @Override
                        public void finished() {
                            Log.d(TAG, "Finished Encoding!");
                            //progressDialog.setIndeterminate(true);
                        }
                    });

                    //free Memory
            for (Bitmap bitm : src_list)
                bitm.recycle();

            //Java Garbage collector
            System.gc();

            //merging the split encoded image
            Bitmap srcEncoded = Utils.mergeImage(encoded_list, originalHeight, originalWidth);

            //Setting encoded image to result
            result.setEncodedImage(srcEncoded);
            result.setEncrypted(true);
        }

        return result;
    }
}
