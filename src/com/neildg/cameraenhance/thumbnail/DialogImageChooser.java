/**
 * 
 */
package com.neildg.cameraenhance.thumbnail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * A custom dialog fragment to show to the user to let them choose what image to show,
 * the original image or the processed image.
 * @author NeilDG
 *
 */
public class DialogImageChooser extends DialogFragment {

	 // Use this instance of the interface to deliver action events
    private NoticeDialogListener mListener;
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositionClick(DialogImageChooser dialog, int position);
    }
    
	 @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose type of image")
               .setItems(new CharSequence[] {"Original", "Processed"}, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int position) {
        	   DialogImageChooser.this.mListener.onDialogPositionClick(DialogImageChooser.this, position);
           }
        });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
	 
	 	

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	 
}
