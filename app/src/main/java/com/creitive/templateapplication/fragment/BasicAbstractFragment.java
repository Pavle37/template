package com.creitive.templateapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.creitive.templateapplication.R;
import com.creitive.templateapplication.activity.BasicAbstractActivity;
import com.creitive.templateapplication.util.Utils;


/**
 * Children of this class must implement methods
 * <code>setLogoId</code>
 * <p>
 * <code>getTitle</code>
 * <p>
 * <code>setBackButtonId</code>
 * <p>
 * which are used to configure Activity's action bar
 */

public abstract class BasicAbstractFragment extends Fragment {

    public static final int FLAG_NO_DRAWABLE = 0;
    protected static final String FLAG_NO_TITLE = "";

    protected static final String ARGS_KEY = "com.creitive.cvvault.fragment.basic.args";

    protected ProgressDialog mProgressDialog;

    public BasicAbstractActivity mActivity;

    /**Interface used to indicate which fragment is used as last fragment in the Activity**/
    public interface Home{}

    /**Activities implemnting this interface know how to set their back button, logo or text and hamburger icon**/
    public interface ToolbarSetter {
        void setToolbar(BasicAbstractFragment currentFragment, String fragmentTitle, int backButton);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BasicAbstractActivity) context;
    }

    public boolean isProgressShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates new Fragment with FLAG as arg
     **/
    public static Bundle getArgs(int flag) {
        Bundle args = new Bundle();
        args.putInt(ARGS_KEY, flag);

        return args;
    }

    /**
     * Creates new Fragment with FLAG as arg
     **/
    public static Bundle getArgs(String flag) {
        Bundle args = new Bundle();
        args.putString(ARGS_KEY, flag);

        return args;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setToolbar(this, setTitle(), setBackButtonId());
    }

    /**
     * Sets left drawable of the {@link android.support.v7.app.ActionBar} to  id returned
     **/
    protected int setBackButtonId() {
        return R.drawable.ic_back;
    }

    /**
     * Sets the Title of the {@link android.support.v7.app.ActionBar} corresponding to the caller Activity
     **/
    protected abstract String setTitle();

    /**
     * Method that handles back presses
     **/
    public void backPressed() {
        mActivity.getSupportFragmentManager().popBackStack();
    }



    /**
     * Used with predefined title and text for progress
     **/
    public void showProgress() {
        if (!isProgressShowing())
            showProgress(getString(R.string.progress_title), getString(R.string.progress_content));
    }

    /**
     * Hides the progress dialog
     */
    public void hideProgress(){
        if(isProgressShowing())
            mProgressDialog.dismiss();
    }

    /**
     * Displays progress dialog or {@link Toast} if no Internet connection
     **/
    public void showProgress(String progressTitle, String progressText) {
        mProgressDialog = ProgressDialog.show(getContext(), progressTitle, progressText, true);
    }
    /**
     *  Displays progress dialog only if the Internet connection is on, Toasts otherwise
     *
     *  @param progressTitle - Title of the progress dialog
     *  @param progressText - Text in the content section of the progress dialog
     */
    public void showProgressConsideringConnection(String progressTitle, String progressText){
        if (!Utils.General.hasInternetConnection(getContext())) {
            Toast.makeText(mActivity, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(getContext(), progressTitle, progressText, true);
    }
    /**
     *  Displays progress dialog only if the Internet connection is on, Toasts otherwise
     */
    public void showProgressConsideringConnection(){
        if (!isProgressShowing())
            showProgressConsideringConnection(getString(R.string.progress_title), getString(R.string.progress_content));
    }


}
