package com.creitive.templateapplication.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Class that encapsulates some UTIL methods
 */
public final class Utils {

    /**Util class used when dealing with View's**/
    public static class View{

        /**
         * @return ArrayAdapter that is set to the spinner with dropdown menu android.R.layout.simple_spinner_dropdown_item;
         *
         * @param spinnerLayoutId what spinner should look like when item is selected
         * @param itemList array of objects that is shown ( method toString is called )
         *
         */
        public static ArrayAdapter configureSpinner(int spinnerLayoutId,Context context,Object[] itemList){
            int dropdownLayoutId = android.R.layout.simple_spinner_dropdown_item;
            return configureSpinner(spinnerLayoutId,dropdownLayoutId,context,itemList);
        }

        /**
         * @return ArrayAdapter that is set to the spinner
         *
         * @param spinnerLayoutId what spinner should look like when item is selected
         * @param dropdownLayoutId what dropdown menu should look like
         * @param itemList array of objects that is shown ( method toString is called )
         *
         */
        public static ArrayAdapter configureSpinner(int spinnerLayoutId,int dropdownLayoutId,Context context,Object[] itemList){
            ArrayAdapter adapter = new ArrayAdapter<>(context, spinnerLayoutId, itemList);
            adapter.setDropDownViewResource(dropdownLayoutId);
            return adapter;
        }

        /**
         * Disables the view completely
         **/
        public static void disableView(android.view.View view) {
            view.setEnabled(false);
            view.setClickable(false);
            view.setFocusable(false);
            view.setLongClickable(false);
        }

        /**
         * Sets html content to a TextView takes care of supporting below and above Nougat
         *
         * @param tv - Target TextView
         * @param html - HTML content that is loaded to that TextView
         */
        public static void setHtmlToView(TextView tv, String html) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
            } else tv.setText(Html.fromHtml(html));
        }

        /**
         * Hides the keyboard
         */
        public static void hideKeyboard(Context context, android.view.View view) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**General things used in utility class**/
    public static class General{
        /**
         * Serialize object with GSON
         **/
        public static String serializeToJson(Object someClass) {
            Gson gson = new Gson();
            return gson.toJson(someClass);
        }

        /**
         * Deserialize object from String
         **/
        public static <T> T deserializeFromJson(String jsonString, Class<T> classPar) {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, classPar);
        }

        /**
         * Checks for Internet Connection
         *
         * @return <code>true</code>if there is Internet Connection, <code>false</code> otherwise
         */
        public static boolean hasInternetConnection(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

        /**
         * Resizes the bitmap
         * @param bitmap bitmap to be resized
         * @param maxWidth maximum width of the provided bitmap
         * @param maxHeight maximum height of the provided bitmap
         * @return Bitmap with new dimensions rezied from the provided one
         */
        public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

            float scale = Math.min(((float) maxHeight / bitmap.getWidth()), ((float) maxWidth / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            return resizedBitmap;
        }

        /**
         * Scales down the bitmap so that the bitmap's longer side is equal to maxSize
         *
         * @param bitmap bitmap to be compressed
         * @param maxSize maximum size of the supplied bitmap
         * @return Compressed bitmap
         */
        public static Bitmap compressBitmap(Bitmap bitmap, int maxSize) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 0) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        /**
         * Gets the color from resources
         * @param context Context from which the resource is pulled
         * @param id id of resource that we wabt ti load
         * @return Color loaded from resource
         */
        public static int getColorWrapper(Context context, int id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.getColor(context, id);
            } else {
                //noinspection deprecation
                return context.getResources().getColor(id);
            }
        }
    }

    /**Class that is used to validate some common strings**/
    public static class Validator{
        /**
         * Validates the email address passed as the first parametar
         *
         * @param email - Email address that needs to be validated
         * @return <code>true</code> if email is valid, <code>false</code> otherwise
         */
        public static boolean isValidEmail(CharSequence email) {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        /**
         * Validates phone number
         *
         * @param phone - Phone number that needs to be validated
         * @return <code>true</code> if phone is valid, <code>false</code> otherwise
         */
        public static boolean isValidPhone(String phone) {
            return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
        }

        /**
         * Validates age of a person. Age must be in [0-150] years and must only contain chars
         *
         * @param ageString - Age that is checked
         * @return <code>true</code> if age is valid, <code>false</code> otherwise
         */
        public static boolean isValidAge(String ageString) {
            try{
                int age = Integer.parseInt(ageString);

                return age >= 0 && age < 150;
            }
            catch (NumberFormatException e){
                return false;
            }
        }

        /**
         * Validates string to check if it is a positive number
         *
         * @param number - string that is being validated
         * @return <code>true</code> if number is positive, <code>false</code> otherwise
         */
        public static boolean isValidPositiveNumber(String number) {
            return number.length() > 0 && number.matches("[0-9]+");
        }
    }

}
