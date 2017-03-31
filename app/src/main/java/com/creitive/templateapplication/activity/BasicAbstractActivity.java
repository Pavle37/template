package com.creitive.templateapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.creitive.templateapplication.R;
import com.creitive.templateapplication.fragment.BasicAbstractFragment;
import com.creitive.templateapplication.model.Token;
import com.creitive.templateapplication.util.Utils;


/**
 * Children of this Activity will get their {@link android.support.v7.app.ActionBar}
 * set dependent on the {@link BasicAbstractFragment} that is loaded
 * <p>
 * <code>backPressed</code> is  executed when back button on the {@link android.support.v7.app.ActionBar}
 * is pressed
 * <p>
 * <code>initializeToolbar</code> is executed as soon as onCreate is called, and <bold>must</bold>
 * initialize the Toolbar
 */

public abstract class BasicAbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Toolbar mToolbar;

    private BasicAbstractFragment mCurrentFragment;

    private static final String LOCAL_STORE_KEY = "com.creitive.localstore.key";

    /**One of the most important methods. sets the toolbar of the instancing Activity**/
    public void setToolbar(BasicAbstractFragment currentFragment, String fragmentTitle, int backButton) {

        mCurrentFragment = currentFragment; //This is needed for navigation back

        mToolbar.setTitle("");//Clear the title so that IvBack can go to the left

        TextView tvTitle = (TextView) mToolbar.findViewById(R.id.tvToolbarTitle);
        tvTitle.setText(fragmentTitle);

        ImageView ivLogo = (ImageView) mToolbar.findViewById(R.id.ivToolbarLogo);
        if (fragmentTitle.equals("")) {
            imageViewAppear(ivLogo, R.drawable.logo);
        } else {
            ivLogo.setVisibility(View.GONE);
        }

        ImageView ivBack = (ImageView) mToolbar.findViewById(R.id.ivToolbarBack);
        if (backButton != BasicAbstractFragment.FLAG_NO_DRAWABLE) {
            imageViewAppear(ivBack, backButton);
        } else {
            ivBack.setVisibility(View.GONE);
        }

        ivBack.setOnClickListener(view -> mCurrentFragment.backPressed());
    }

    private void imageViewAppear(ImageView iv, int imgResId) {
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(imgResId);
    }


    /**
     * Used to start new Activity with slide in transition
     **/
    public void activityStart(Intent i) {
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        mCurrentFragment.backPressed();
    }

    /**
     * Executed when back button on the {@link android.support.v7.app.ActionBar} is pressed
     */
    public void backPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    /**
     * Loads fragments with animation going forward or backward dependent on loadForward
     **/
    public void loadFragment(BasicAbstractFragment fragmentToLoad, boolean loadForward) {
        int animationInId, animationOutId;
        int animationInIdOut, animationOutIdOut;
        if (loadForward) {
            animationInId = R.anim.right_in;
            animationOutId = R.anim.left_out;

            animationInIdOut = R.anim.in_from_left;
            animationOutIdOut = R.anim.out_to_right;
        } else {
            animationInId = R.anim.bottom_up;
            animationOutId = R.anim.bottom_up_out;

            animationInIdOut = R.anim.top_down_in;
            animationOutIdOut = R.anim.top_down;
        }

        //If we are trying to load the same fragment that is being currently displayed don't let it
        if (mCurrentFragment != null && (mCurrentFragment.getClass().getName().equals(fragmentToLoad.getClass().getName()))) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animationInId, animationOutId, animationInIdOut, animationOutIdOut)
                .replace(R.id.flContent, fragmentToLoad)
                .addToBackStack(fragmentToLoad.getClass().getName())
                .commit();
    }

    /**
     * Loads fragments with animation going forward
     **/
    public void loadFragment(BasicAbstractFragment fragmentToLoad) {
        loadFragment(fragmentToLoad, true);
    }

    /**
     * Saves Login {@link Token} to SharedPrefs
     **/
    public void saveLogIn(Token token) {
        SharedPreferences preferences = getSharedPreferences(LOCAL_STORE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putString(LOCAL_STORE_KEY, Utils.General.serializeToJson(token)).apply();
    }

    /**
     * Loads Login {@link Token} from SharedPrefs
     **/
    public Token loadLogIn() {
        SharedPreferences preferences = getSharedPreferences(LOCAL_STORE_KEY, Context.MODE_PRIVATE);
        return Utils.General.deserializeFromJson(preferences.getString(LOCAL_STORE_KEY, ""), Token.class);
    }

    /**
     * Loads a new Activity
     **/
    public void loadActivity(Class<? extends Activity> activityToLoad) {
        Intent i = new Intent(this, activityToLoad);
        startActivity(i);
        this.finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //First check if we are already on home
            if(mCurrentFragment instanceof BasicAbstractFragment.Home){
                closeDrawer();
                return true; //Don't execute code below
            }
            //Clear all fragments to restart the back stack
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //TODO: loadFragment(new HomeFragment(),false);
            }
        else if (id == R.id.nav_logout) {
            saveLogIn(null); //Discard the saved Token
            //TODO: loadActivity(BeforeLoginActivity.class);
        }

        closeDrawer();
        return true;
    }

    /**
     * Closes the drawer
     **/
    protected abstract void closeDrawer();
}
