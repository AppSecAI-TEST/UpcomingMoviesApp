package com.example.upcomingmoviesapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.upcomingmoviesapp.R;

import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String checkString(String myString) {

        String data = myString.trim();
        if (data.length() == 0 || data.equalsIgnoreCase("") ||
                data.equalsIgnoreCase(null) ||
                data.equalsIgnoreCase("null")) {
            data = "N/A";
        } else {
            data = myString.trim();
        }
        return data;
    }

    public static void hideSoftKeyboard(Activity con) {
        /*InputMethodManager imm = (InputMethodManager) con.getSystemService(con.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(edt_username.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(con.getCurrentFocus().getWindowToken(), 0);*/

        // // TODO: 23/01/2017 : new logic
        // Check if no view has focus:
        View view = con.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static boolean isEmailValid(String email) {
        String regExpn =
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean validateString(String userName) {
        boolean isEmpty = true;
        if (userName.length() > 0 && !userName.equalsIgnoreCase("")) {
            isEmpty = false;
        } else {
            isEmpty = true;
        }
        return isEmpty;
    }

    public static boolean isValidMobile(String phone2) {
        boolean check = false;
        CharSequence text = "";

        if (!Pattern.matches("[a-zA-Z]+", text)) {
            if (phone2.length() > 0 && !phone2.equalsIgnoreCase("")) {
                check = false;
                //  txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static void setupUI(final Activity activityContext, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard(activityContext);
                    return false;
                }
            });
        }
    }

    public static void setAdapter(Context mContext, Spinner spinner_policy_no, ArrayList<String> policyList) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, policyList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy_no.setAdapter(dataAdapter);
    }

    public static void setAdapterClaimIntimation(Context mContext, Spinner spinner_policy_no, ArrayList<String> policyList) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item2, policyList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy_no.setAdapter(dataAdapter);
    }

    public static void setTitle(TextView tvHeader, String title) {
        tvHeader.setText(title);
    }

    public static void showInternetDialog(final Context ctx, String msg) {
        final Activity activity = (Activity) ctx;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.app_name);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static boolean isInternetAvailable(Context mContext) {
        if (haveNetworkConnection(mContext)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean haveNetworkConnection(Context contxt) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    return haveConnectedWifi;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    return haveConnectedMobile;
                }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }


    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Deletes the contents of {@code dir}. Throws an IOException if any file
     * could not be deleted, or if {@code dir} is not a readable directory.
     */
    public static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);

            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    public static void ClearCacheIfExist(Context mContext, File directory) {
        File cacheFile = Util.getExternalCacheDir(mContext);
        if (!directory.exists()) {
            try {
                Util.deleteContents(cacheFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static void displayNormalSnackbar(CoordinatorLayout cdSnackbar, int intOfString) {
        Snackbar snackbar = Snackbar.make(cdSnackbar, intOfString, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static String isJsonObjectHasKeyContent(JSONObject jsonDataObject, String key) {
        String name = "";
        try {
            if (jsonDataObject.has(key)) {
                name = jsonDataObject.getString(key);
            } else {
                name = "N/A";
            }
        } catch (Exception e) {
            name = "N/A";
        }
        return name;
    }

    public static void validateStringAndSetTextView(TextView textView, String string) {
        if (string.equalsIgnoreCase("") || string.equalsIgnoreCase("null") || string.equalsIgnoreCase(null)) {
            textView.setText("N/A");
        } else {
            textView.setText(string);
        }
    }

    public static String checkBundleParameter(Bundle bundle, String key) {
        String myKey = "";
        try {
            myKey = bundle.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            myKey = "";
        }
        return myKey;
    }

    public static void displaySnackbar(CoordinatorLayout snackbarLayout, String messageToDisplay) {
        Snackbar snackbar = Snackbar.make(snackbarLayout, messageToDisplay, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static LinearLayoutManager attachRecyclerViewwithLL(Context mContext) {

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        return llm;
    }


    public static boolean isEditTextEmpty(final EditText editText) {
        if (editText.getText().toString().length() == 0) {
            return true;
        } else return false;

    }


    public static boolean validateSpinner(final Spinner spinner, final String stringToCompare) {
        if (spinner.getSelectedItem().toString().equalsIgnoreCase(stringToCompare)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateContactNumber(String stringContact) {

        if (stringContact.length() < 10) {
            return true;
        } else {
            return false;
        }
    }

    public static String makeValidUrlParameter(String string) {

        return string.trim().replaceAll(" ", "%20");
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }


            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
