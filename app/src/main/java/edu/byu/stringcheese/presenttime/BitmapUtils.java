package edu.byu.stringcheese.presenttime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.Firebase;

import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by longl on 3/29/2016.
 */
public class BitmapUtils {
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap decodeSampledBitmapFromString(String encodedImage,
                                                       int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
    }

    public static Bitmap decodeStringToBitmap(String encodedImage)
    {
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return decodedImage;
    }

    public static String encodeBitmapToString(Bitmap image)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 1, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        image.recycle();
        try {
            byteStream.close();
        } catch (IOException e) {
            //already closed
        }
        return imageFile;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream inputStream,
                                                       int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static Bitmap decodeStream(InputStream inputStream)
    {
        return BitmapFactory.decodeStream(inputStream);
    }
    public static File saveBitmapToFile(Bitmap bitmap, String filename)
    {
        FileOutputStream out = null;
        File file = new File(Environment.getExternalStorageDirectory().toString(),filename+".png");
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void sendImageToFirebase(Context context, Uri imageData, String user, Firebase ref)
    {
        String image = encodeBitmapToString(decodeSampledBitmapFromFile(new File(getRealPathFromURI(context,imageData)),60,60));
        HashMap<String, Object> map = new HashMap<>();
        map.put(user,image);
        ref.getParent().child("images").updateChildren(map);
    }


    public static String getDefaultImage(Context context)
    {
        File defaultAvatar = new File(Environment.getExternalStorageDirectory().toString(),"default.png");
        if(!defaultAvatar.exists()) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.balloon);
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(defaultAvatar));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return defaultAvatar.getAbsolutePath();
    }
    public static String getRealPathFromURI(Context context, Uri data)
    {
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            realPath = getRealPathFromURI_BelowAPI11(context, data);

            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            realPath = getRealPathFromURI_API11to18(context, data);

            // SDK > 19 (Android 4.4)
        else
            realPath = getRealPathFromURI_API19(context, data);
        return realPath;
    }
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String encodeResourceToString(Resources resources, int res, int width, int height) {
        return BitmapUtils.encodeBitmapToString(BitmapUtils.decodeSampledBitmapFromResource(resources,res, width, height));
    }

    public static Bitmap decodeSampledBitmapFromWebImage(Uri url, int width, int height)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url.toString());
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, width, height);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                return bitmap;
            }
        }
        catch(Exception e)
        {
            Log.e("BitmapUtils","Failed to download image",e);
        }

        return null;
    }

    public static String decodeImageFromWebToString(Uri photoUrl, int width, int height) {
        return BitmapUtils.encodeBitmapToString(decodeSampledBitmapFromWebImage(photoUrl,width,height));
    }
}
