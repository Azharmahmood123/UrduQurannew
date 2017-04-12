package com.QuranReading.urduquran;


import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;

public class ResetDialog extends AppCompatActivity {
	GlobalClass mGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.reset_dialog);
		mGlobal=(GlobalClass)getApplicationContext();
		showReset();

	}
	 private void showReset()
	 {
		/* TextView title = new TextView(this);
		 title.setText(R.string.reset);
		 title.setPadding(10, 10, 10, 10);
		 title.setGravity(Gravity.LEFT);
         title.setTextColor(getResources().getColor(R.color.black));
		 title.setTypeface(mGlobal.faceHeading);
		 title.setTextSize(30);
		 TextView msg = new TextView(this);
		 msg.setText(R.string.txt_reset);
		 msg.setPadding(10, 10, 10, 10);
		 msg.setGravity(Gravity.LEFT);
		 msg.setTextColor(getResources().getColor(R.color.black));
		 msg.setTypeface(mGlobal.faceHeading);
		 msg.setTextSize(26);
*/




		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setCustomTitle(title);
		// builder.setView(msg);
		 builder.setTitle(R.string.reset);
		 builder.setMessage(R.string.txt_reset);

		 builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		             // Write your code here to execute after dialog closed
		            	Intent end_actvty = new Intent();
		         		end_actvty.putExtra("RESET", true);
		         		setResult(RESULT_OK, end_actvty);
		         		finish();
		         		
		             }
		     });
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
    	 finish();
     }});
			builder.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
					// TODO Auto-generated method stub
					
				}
			});
			AlertDialog dialog = builder.show();
			/*TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
			dialog.show();*/
		/* TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
		 messageView.setGravity(Gravity.LEFT);*/

 // Setting Icon to Dialog
 //alertDialog.setIcon(R.drawable.tick);

 // Setting OK Button

	 }
	

	
}

