package com.creitive.templateapplication.network;

import android.content.Context;
import android.widget.Toast;

import com.creitive.templateapplication.R;

import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.HttpException;


/**
 * Class used to handle error messages
 */

public class GenericErrorHandler {

    /**Toasts according error message for user to see**/
    public static void handleError(Throwable error,Context context){
        if (error instanceof HttpException) {
            HttpException e = ((HttpException) error);

            String errorMsgJSON = readErrorMessageJSON(e.response().errorBody().charStream());
            String errorMsg = readErrorMessage(errorMsgJSON);

            if(errorMsg.isEmpty()){
                Toast.makeText(context, R.string.error_reading_error, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
        else if(error instanceof UnknownHostException){
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
        else{ //We are here probably because of GSON parsing
            error.printStackTrace();
            Toast.makeText(context, R.string.error_cannot_parse, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will handle errors like this "error":"first errror"
     *
     * What this function does is it reads all the text between 3rd and 4th quote sign ( " )
     *
     * @param errorMsgJSON
     * @return Error message that can be displayed to user, and "" if something went wrong
     */
    private static String readErrorMessage(String errorMsgJSON) {
        Pattern pattern = Pattern.compile("\"error\":[^\"]*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(errorMsgJSON);
        if(matcher.find()){
            return matcher.group(1);
        }
        return "";
    }

    /**Reads text from Reader and builds a string **/
    private static String readErrorMessageJSON(Reader reader) {
        StringBuilder errorMsgBuilder = new StringBuilder(); //Builder used for better string performance
        try {
            int data = reader.read(); //Read data
            while (data != -1) { //Check if EOF
                char dataChar = (char) data; //Cast to char
                errorMsgBuilder.append(dataChar); //Append
                data = reader.read(); //Loop again
            }
        }
        catch (IOException e1){ //If there has been an Error reading, return empty error msg
            e1.printStackTrace();
            return "";
        }
        return errorMsgBuilder.toString(); //Everything ok
    }
}
