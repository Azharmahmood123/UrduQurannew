package com.packageapp.quranvocabulary.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.packageapp._13linequran.MainActivity13LineQuran;
import com.packageapp._13linequran.network.DownloadUtils13Line;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;
import com.packageapp.quranvocabulary.models.CategoryDetail;
import com.packageapp.quranvocabulary.networkhelpers.DownloadDialogQuranVocabulary;
import com.packageapp.quranvocabulary.networkhelpers.DownloadUtilsQuranVocabulary;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cyber on 1/20/2016.
 */
public class DetailAdapter extends ArrayAdapter implements MediaPlayer.OnCompletionListener {

    private  boolean isAudioExist=false;
    private ArrayList<CategoryDetail> arrayList;
    private Context context;
    private SettingsSharedPref sharedPref;
    private ArrayList<Integer> clickedItem;
    private static boolean isPlaying = false;
    private static MediaPlayer mediaPlayer;
    private ImageView speakerImage;
    private int catId;
    private TextView[] textView;
    private int indexPos=-1;
    private int textLength;
    private int listPosition;
    int positionChange=-1;
    int currentPlayPos=-1;
    int temporaryPos=-1;
    private long stopClick=0;

    public DetailAdapter(Context context, int resource, ArrayList<CategoryDetail> list, ArrayList<Integer> clickedItem) {
        super(context, resource);
        this.context = context;
        this.arrayList = list;
        this.clickedItem = clickedItem;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_layout,parent,false);

        sharedPref = new SettingsSharedPref(context);
        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.detail_txt_arabic_layout2);
        final TextView[] txtView = new TextView[5];
        txtView[0] = (TextView) rootView.findViewById(R.id.detail_txt_arabic1);
        txtView[1] = (TextView) rootView.findViewById(R.id.detail_txt_arabic2);
        txtView[2] = (TextView) rootView.findViewById(R.id.detail_txt_arabic3);
        txtView[3] = (TextView) rootView.findViewById(R.id.detail_txt_arabic4);
        txtView[4] = (TextView) rootView.findViewById(R.id.detail_txt_arabic5);

        final ImageView speakerImg = (ImageView) rootView.findViewById(R.id.detail_speaker);
        TextView txtEng = (TextView) rootView.findViewById(R.id.detail_txt_eng);
        final String split[] = arrayList.get(position).getArabicWord().split(" ");
        catId = arrayList.get(position).getCatId();

        if (split.length > 3) {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }
       /* if(positionChange==position)
        {
            textView=txtView;
        }*/


        speakerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-stopClick<1000)
                {
                    return;
                }
                stopClick=SystemClock.elapsedRealtime();
                temporaryPos=position;

                if ((!isPlaying)) {
                    currentPlayPos=temporaryPos;
                    isAudioExist = isAudioFileExist();
                    if (isAudioExist == true) {
                        isPlaying = true;
//                    playMedia(selectCatAudio(catId)[position]);
                        indexPos = 0;
                        textLength = split.length;
                        listPosition = position;
                        playMedia("a" + catId + "_" + listPosition + "_" + indexPos);
                        txtView[0].setBackgroundColor(context.getResources().getColor(R.color.actionbar_color_tran));
                        speakerImg.setImageResource(R.drawable.speaker_on);
                        speakerImage = speakerImg;
//                    highLight(selectCatTimings(catId)[position], txtView, split.length, 0);
                        textView = txtView;

                        // positionChange=position;

                    } else {
                        pauseMedia();

                        if (textView != null) {
                            textView[0].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                            textView[1].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                            textView[2].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                            textView[3].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                            textView[4].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                            speakerImage.setImageResource(R.drawable.speaker_off);
                        }


                    }
                } else  //block added
                {
                    pauseMedia();
                    if (textView != null) {
                        textView[0].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                        textView[1].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                        textView[2].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                        textView[3].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                        textView[4].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                        speakerImage.setImageResource(R.drawable.speaker_off);
                    }
                    if (currentPlayPos != temporaryPos) {
                        currentPlayPos=temporaryPos;
                        isPlaying = true;
                        isAudioExist = isAudioFileExist();
                        if (isAudioExist == true) {
                            isPlaying = true;
//                    playMedia(selectCatAudio(catId)[position]);
                            indexPos = 0;
                            textLength = split.length;
                            listPosition = position;
                            playMedia("a" + catId + "_" + listPosition + "_" + indexPos);
                            txtView[0].setBackgroundColor(context.getResources().getColor(R.color.actionbar_color_tran));
                            speakerImg.setImageResource(R.drawable.speaker_on);
                            speakerImage = speakerImg;
//                    highLight(selectCatTimings(catId)[position], txtView, split.length, 0);
                            textView = txtView;

                            // positionChange=position;
                        }
                    }
                }
            }
        });

        switch (split.length) {
            case 1:
                txtView[0].setText(split[0]);
                break;
            case 2:
                txtView[0].setText(split[0]);
                txtView[1].setText(split[1]);
                break;
            case 3:
                txtView[0].setText(split[0]);
                txtView[1].setText(split[1]);
                txtView[2].setText(split[2]);
                break;
            case 4:
                txtView[0].setText(split[0]);
                txtView[1].setText(split[1]);
                txtView[2].setText(split[2]);
                txtView[3].setText(split[3]);
                break;
            case 5:
                txtView[0].setText(split[0]);
                txtView[1].setText(split[1]);
                txtView[2].setText(split[2]);
                txtView[3].setText(split[3]);
                txtView[4].setText(split[4]);
                break;
        }

        if (sharedPref.getDetailQuranVocabulary()) {
            txtEng.setVisibility(View.VISIBLE);
            if (sharedPref.getLanguageQuranVocabulary() != 0) {
                txtEng.setText(arrayList.get(position).getEngWord());
            }else {
                txtEng.setText(arrayList.get(position).getUrduWord());
            }
        }else {
            if (clickedItem.contains(position)) {
                txtEng.setVisibility(View.VISIBLE);
                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                    txtEng.setText(arrayList.get(position).getEngWord());
                }else {
                    txtEng.setText(arrayList.get(position).getUrduWord());
                }
            }else {
                txtEng.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    public void playMedia(String audioName) {
        try {


                //  Uri uri = Uri.parse("android.resource://com.quranreading.quranvocabulary/raw/"+audioName);
                File myFile = new File(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath(), audioName+".mp3");
                Uri audioUri = Uri.parse(myFile.getPath());
                mediaPlayer = MediaPlayer.create(context, audioUri);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(this);

        }catch (Exception exp) {

        }
    }
    private boolean isAudioFileExist()
    {
        String audioFile="a1_0_0.mp3";

        try
        {
            if(!DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.exists())
            {
                DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.mkdirs();
            }

            File myFile = new File(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath(), audioFile);

            //  Uri audioUri = Uri.parse(myFile.getPath());

            if(myFile.exists())
            {
                return true;
              //  new MainActivity13LineQuran.loadImagesFiles().execute();
            }
            else
            {
                context.startActivity(new Intent(context, DownloadDialogQuranVocabulary.class));

            }
        }
        catch (Exception e)
        {
            Log.e("File", e.toString());
        }
        return  false;
    }

    public static void pauseMedia() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                isPlaying = false;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public  void pauseMediaForce() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                isPlaying = false;
                textView[indexPos].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                speakerImage.setImageResource(R.drawable.speaker_off);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            textView[indexPos].setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            if (indexPos != (textLength - 1)) {
                indexPos++;
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (isPlaying) {
                    playMedia("a" + catId + "_" + listPosition + "_" + indexPos);
                    textView[indexPos].setBackgroundColor(context.getResources().getColor(R.color.actionbar_color_tran));
                }
            }else {
                isPlaying = false;
                speakerImage.setImageResource(R.drawable.speaker_off);
            }
        }catch (Exception ex){}
    }
}
