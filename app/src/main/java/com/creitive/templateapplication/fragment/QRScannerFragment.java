package com.creitive.templateapplication.fragment;


import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Fragment that captures QRCode and the result is handled in <code>handleResult</code> method
 */
public class QRScannerFragment extends BasicAbstractFragment implements MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;


    public QRScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle state) {
        mScannerView = new ZXingScannerView(getActivity());

        //Set the scanner to QR Code
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);

        return mScannerView;
    }

    @Override
    protected String setTitle() {
        return FLAG_NO_TITLE; //We want the logo shown, so no title is displayed
    }
    @Override
    protected int setBackButtonId() {
        return FLAG_NO_DRAWABLE; //Nothing to go back to, this is first fragment and always will be
    }

    /**
     * Result of the scanning ( parsed QRCode data )
     *
     * @param result data that's parsed
     */

    @Override
    public void handleResult(Result result) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
            Toast.makeText(mActivity, "A234", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showMessageDialog("Contents = " + result.getText() + ", Format = " + result.getBarcodeFormat().toString());
    }

    /**
     * Displays a message in a message info fragment
     * @param message text to be displayed
     */
    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Need to stop the camera for other apps to use it
        mScannerView.stopCamera();
        closeMessageDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set handler to get the handleResult call
        mScannerView.setResultHandler(this);
        //Start scanning
        int cameraId = -1;
        mScannerView.startCamera(cameraId);
    }

    @Override
    public void backPressed() {
        mActivity.finish(); //Cannot go back to previous fragment, because there is none
    }
}
