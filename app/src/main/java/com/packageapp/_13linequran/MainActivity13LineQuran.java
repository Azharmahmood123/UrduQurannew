package com.packageapp._13linequran;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.packageapp._13linequran.adapters.PagerAdapter_13Line;
import com.packageapp._13linequran.network.DownloadDialog13Line;
import com.packageapp._13linequran.network.DownloadUtils13Line;
import com.packageapp.tajweedquran.ConstantsTajweedQuran;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.packageapp.tajweedquran.networkdownloading.DownloadDialogTajweed;
import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;

public class MainActivity13LineQuran extends AppCompatActivity  {
    public static ArrayList<Integer> imagesQuran = new ArrayList<>();
    private PagerAdapter_13Line viewPager;
    Toolbar toolbar13Line;
    List<String> listJuzz = new ArrayList<>();
    List<String> listSurahNames=new ArrayList<>();
    int[] juzPosition = {4, 31, 59, 87, 115, 143, 171, 199, 227, 255, 283, 311, 339, 367, 395, 423, 451, 479, 507, 535, 561, 589, 615, 643, 669, 699, 729, 759, 789, 821};
    int[] surahPosition={4,5,70,108,149,179,211,248,262,290,310,330,348,357,366,374,395,410,427,437,451,464,479,489,503,513,527,539,554,564,573,579,583,597,605,613,620,630,637,
            649,661,670,679,688,693,699,706,712,718,723,727,731,734,738,742,747,752,759,763,768,772,775,777,779,782,785,789,792,796,799,802,805,808,810,813,815,818,821,822,824,826,
            827,828,830,831,832,833,834,835,837,838,839,840,840,841,841,842,842,843,844,845,845,846,846,846,847,847,848,848,848,849,849,849,850};
    private RelativeLayout headerLayout, footerLayout;
    public static ArrayList<String> listimagesQuran=new ArrayList<>();
    public static int pagesSize=50;
   ViewPager vPager;
    Spinner spinnerSurah,spinner;
    private ProgressDialog progressDialog;
    private boolean isFirstTimeJuzz=true;
    private boolean isFirstTimeSurah=true;
    private boolean isSuraClick=false;
    private boolean isJuzClick=false;
    private int currentPosition=0;
    private boolean isScroll=false;
    int tempSave=-1;
    int tempJuzRealPosition=-1;
    private boolean juzClickEvent=false;
    private boolean surahClickEvent=false;
    private int pos1=-1,tempPos1=-1;
    private int pos2=-1,tempPos2=-1;
    private int counter=0;
    private int newRealPos;
    private boolean surahEventOccur=false;
    private boolean juzEventOccur=false;
    int[] specialCases={847,846,845,844,843,840,839,838};
    int specialCaseSurahName[]={113,113,113,113,113,113,113,113};
            //{110,106,104,101,99,95,89,87};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity13_line_quran);
        toolbar13Line = (Toolbar) findViewById(R.id.toolbar_13Line);
        setSupportActionBar(toolbar13Line);

        listSurahNames=Arrays.asList(getResources().getStringArray(R.array.surah_names));
         spinner = (Spinner) findViewById(R.id.spinnerJuzz);
      spinnerSurah = (Spinner) findViewById(R.id.spinnerSurah);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        footerLayout = (RelativeLayout) findViewById(R.id.footerLayout);
        listJuzz = Arrays.asList(getResources().getStringArray(R.array.juzz_names));
        ArrayAdapter<String> dataAdapterSurah = new ArrayAdapter<String>(this, R.layout.spinner_surah_items, listSurahNames);
        dataAdapterSurah.setDropDownViewResource(R.layout.spinner_dropdown_surah);
        spinnerSurah.setAdapter(dataAdapterSurah);
        spinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Toast.makeText(MainActivity13LineQuran.this, "Surah Item selected is " + i, Toast.LENGTH_LONG).show();
                if(!isFirstTimeSurah)
                {
                    isJuzClick=false;
                    isSuraClick=true;
                /*    if(pos1!=i && i!=tempPos1-1)
                    {
                        pos1=i;
                        tempPos1=pos1;
                    }
                    else
                    {

                        pos1=-1;
                    }
                    if(pos1!=0&&pos1!=-1)
                    {
                        surahClickEvent=true;
                    }
                  *//*  if(pos1==-1||pos1==0)
                    {
                        pos1=i;
                    }
                    else if(pos1!=i)
                    {
                        if(pos1!=0)
                        {
                            surahClickEvent=true;
                            pos1=-1;
                        }


                    }*//*
                  *//*  pos1=i;
                  //  setjuzName(i);
                    if(pos1!=0&&pos1!=-1)
                    {
                        surahClickEvent=true;
                    }*//*
                *//*    if(surahClickEvent) {
                        surahClickEvent=false;
                      //  pos1=-1;
                        vPager.setCurrentItem(850 - surahPosition[i]);

                    }*//*
                  *//*  if(spinnerSurah.getTag().equals("wrong selection"))
                    {

                    }
                    else
                    {
                        vPager.setCurrentItem(850 - surahPosition[i]);
                    }*/
                    if(pagesSize==50)
                    {
                        if (surahEventOccur &&i<2) {
                            surahEventOccur = false;
                            vPager.setCurrentItem(50 - surahPosition[i]);
                        }
                        else if(i>1)
                        {
                            startActivity(new Intent(MainActivity13LineQuran.this, DownloadDialog13Line.class));
                        }

                    }

                    else {
                        if (surahEventOccur) {
                            surahEventOccur = false;
                            vPager.setCurrentItem(850 - surahPosition[i]);
                        }
                    }


                }

                isFirstTimeSurah=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSurah.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                surahEventOccur = true;
                return false;
            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listJuzz);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
       // spinner.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(!isFirstTimeJuzz)
               {
                   isSuraClick=false;
                   isJuzClick=true;
                 //  setSurahName(i);
                /*   if(pos2!=i)
                   {
                       pos2=i;
                   }
                   else
                   {
                       pos2=-1;
                   }
                   if(pos2!=0&&pos2!=-1)
                   {
                       juzClickEvent=true;
                   }

                  *//* if(pos2==-1||pos2==0)
                   {
                       pos2=i;
                   }
                   else if(pos2!=i)
                   {
                       if(pos2!=0)
                       {
                           surahClickEvent=true;
                           pos2=-1;
                       }


                   }*//*
                 *//*  pos2=i;
                   if(pos2!=0&&pos2!=-1)
                   {
                       juzClickEvent=true;
                   }*//*
                   if(juzClickEvent){
                       juzClickEvent=false;
                     //  pos2=-1;
                       vPager.setCurrentItem(850-juzPosition[i]);


                   }*/

                    if(pagesSize==50)
                    {
                        if (juzEventOccur&&i<2) {
                            juzEventOccur = false;
                            vPager.setCurrentItem(50-juzPosition[i]);
                        }
                        else if(i>1)
                        {
                            startActivity(new Intent(MainActivity13LineQuran.this, DownloadDialog13Line.class));
                        }
                    }
                   else {


                        if (juzEventOccur) {
                            juzEventOccur = false;
                            vPager.setCurrentItem(850 - juzPosition[i]);
                        }
                    }
                  //
               }
                isFirstTimeJuzz=false;

               // Toast.makeText(MainActivity13LineQuran.this, "Juzz Item selected is " + i, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                juzEventOccur=true;
                return false;
            }
        });

      /*  for (int i = 801; i < 851; i++) {
            int id = getResources().getIdentifier("drawable/" + "img_" + i, null, getPackageName());
            imagesQuran.add(id);
        }*/


      //  Collections.reverse(imagesQuran);
      //  Log.e("IMages", "images" + imagesQuran);
        vPager = (ViewPager) findViewById(R.id.vPager13Line);
        viewPager = new PagerAdapter_13Line(getSupportFragmentManager());
        DownloadImagesFiles();

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;

               if(pagesSize==50)
                {
                    setInitialPage(position);
                }
                else {
                    setJuzAndSurahName(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }



    private void setSurahName(int pos)
    {
        int surahTempPos[]={0,1,1,2,3,3,4,5,6,7,8,10,11,14,16,17,20,22,24,26,28,32,35,38,40,45,50,57,66,77};
        spinnerSurah.setSelection(surahTempPos[pos]);
    }
    private void setjuzName(int pos)
    {

        int juzzTempPos[]={0,0,2,3,5,6,7,8,9,10,10,11,12,12,12,13,14,14,15,15,16,16,17,17,17,18,18,19,19,20,20,20,20,21,21,21,22,22,22,23,23,24,24,24,24,25,25,25,25,25,25,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,28,28,28,28,28,28,28,28,28,28,28,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29};
        spinner.setSelection(juzzTempPos[pos]);
    }
    private void setInitialPage(int pos){
        int realPosition=50-currentPosition;
        for(int i=0;i<2;i++)
        {
            if(realPosition==surahPosition[i])
            {
                tempSave=i;
                spinnerSurah.setSelection(i);
                setjuzName(i);
            }
            else if(tempSave!=-1) {
                if (realPosition==(surahPosition[tempSave]-1)) {

                    if (tempSave != 0) {
                        newRealPos = tempSave;
                        newRealPos -= 1;
                        spinnerSurah.post(new Runnable() {
                            @Override
                            public void run() {
                                spinnerSurah.setSelection(newRealPos,true);
                            }
                        });

                        tempSave = -1;

                    }
                }
            }
        }
        for(int i=0;i<2;i++)
        {

            if(realPosition==juzPosition[i]){
                spinner.setSelection(i);
                setSurahName(i);
                tempJuzRealPosition=i;
              /*  if(juzClickEvent==true)
                {
                    tempJuzRealPosition=-1;

                }*/
                //  tempValue=juzPosition[i];

            }
            else if(tempJuzRealPosition!=-1) {
                if ((realPosition==juzPosition[tempJuzRealPosition]-1)) {
                    if (tempJuzRealPosition != 0) {
                        int newRealPos = tempJuzRealPosition;
                        newRealPos -= 1;
                        spinner.setSelection(newRealPos);
                        // setSurahName(newRealPos);
                        tempJuzRealPosition = -1;
                    }

                }
            }
        }

    }
    private void setJuzAndSurahName(int pos)
    {
        int realPosition=850-currentPosition;

        for(int i=0;i<114;i++)
        {
            if(realPosition==surahPosition[i])
            {
                tempSave=i;
                spinnerSurah.setSelection(i);
                setjuzName(i);



            }

            else if(tempSave!=-1) {

                if (realPosition==(surahPosition[tempSave]-1)) {
                    if (realPosition>837) {
                        //special case
                      /*  for(int k=0;k<8;k++)
                        {

                            if(realPosition==specialCases[k])
                            {
                                spinnerSurah.setSelection(specialCaseSurahName[k]);
                            }

                        }*/

                    } else {

                        if (tempSave != 0) {
                            newRealPos = tempSave;
                            newRealPos -= 1;
                            spinnerSurah.post(new Runnable() {
                                @Override
                                public void run() {
                                    spinnerSurah.setSelection(newRealPos, true);

                                    //  spinner.setTag(0); // tag crashes remove this tag
                                    // isEventOver=true;

                                }
                            });

                            //  setjuzName(newRealPos);
                            tempSave = -1;

/*
                        spinnerSurah.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                            }
                        },1000);*/
                        }
                    }
                }
            }


        }




        for(int i=0;i<30;i++)
        {

            if(realPosition==juzPosition[i]){
                spinner.setSelection(i);
                setSurahName(i);
                tempJuzRealPosition=i;
                if(juzClickEvent==true)
                {
                    tempJuzRealPosition=-1;

                }
              //  tempValue=juzPosition[i];

            }
            else if(tempJuzRealPosition!=-1) {
                if ((realPosition==juzPosition[tempJuzRealPosition]-1)) {
                    if (tempJuzRealPosition != 0) {
                        int newRealPos = tempJuzRealPosition;
                        newRealPos -= 1;
                        spinner.setSelection(newRealPos);
                       // setSurahName(newRealPos);
                        tempJuzRealPosition = -1;
                    }

                }
            }
        }
    }

    public void viewsClickEvent(View v) {
        switch (v.getId() ){

            case R.id.imgQuranPage:
                hideAnimation();
                break;
            case R.id.tvGoTo:
                gotoDialogue();
                break;
            case R.id.btn_next:
                vPager.setCurrentItem(currentPosition-1);
                break;
            case R.id.btn_previous:
                vPager.setCurrentItem(currentPosition+1);
                break;
            default:
                break;


        }

    }
    private void hideAnimation()
    {
        Animation animFadein, animFadeout;
        // load the animation
        animFadein = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);

        // load the animation
        animFadeout = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);

        if (GlobalClass.isLayoutVisible) {
            headerLayout.startAnimation(animFadeout);
            footerLayout.startAnimation(animFadeout);
            GlobalClass.isLayoutVisible = false;
            headerLayout.setVisibility(View.GONE);
            footerLayout.setVisibility(View.GONE);


        } else {

            headerLayout.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);
            headerLayout.startAnimation(animFadein);
            footerLayout.startAnimation(animFadein);
            GlobalClass.isLayoutVisible = true;
        }
    }
    private void gotoDialogue()
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
       // ew AlertDialog.Builder(mContext, R.style.MyCustomDialogTheme);
       // if you want to change the theme of the dialog.

        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Enter Between 1-851");
        alert.setTitle("Enter Page Number");

        alert.setView(edittext);

        alert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
               // Editable YouEditTextValue = edittext.getText();
                //OR
                String editTextvalue = edittext.getText().toString();
                int gotoPosition;
                if(editTextvalue.isEmpty()||editTextvalue.equals("0"))
                {
                    showToastMessage();
                }
                else
                {
                    gotoPosition=Integer.parseInt(editTextvalue);
                    if(gotoPosition>850)
                    {
                        showToastMessage();

                    }
                    else
                    {
                        int tempValue=gotoPosition;
                        tempValue=850-tempValue;
                        vPager.setCurrentItem(tempValue);
                    }

                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });

        alert.show();
    }
    private void showToastMessage()
    {
        Toast toast = Toast.makeText(getBaseContext(), "Enter Number between 1 -850 ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void DownloadImagesFiles()
    {
        String audioFile="img_1.jpg";

        try
        {
            if(!DownloadUtils13Line.rootPath13Line.exists())
            {
                DownloadUtils13Line.rootPath13Line.mkdirs();
            }

            File myFile = new File(DownloadUtils13Line.rootPath13Line.getAbsolutePath(), audioFile);

          //  Uri audioUri = Uri.parse(myFile.getPath());

            if(myFile.exists())
            {
                pagesSize=850;
                listimagesQuran=new ArrayList<>();
                new  loadImagesFiles().execute();
            }
            else
            {
                listimagesQuran=new ArrayList<>();
               // showDownloadDialog();

                /* for (int i = 801; i < 851; i++) {
                    int id = getResources().getIdentifier("drawable/" + "img_" + i, null, getPackageName());
                     listimagesQuran.add(String.valueOf(id));
                  }*/
                for (int i = 0; i < 50; i++) {


                    int id = getResources().getIdentifier("drawable/" + "img_" + i+1, null, getPackageName());
                    listimagesQuran.add(String.valueOf(id));
                }
                pagesSize=50;
                vPager.setAdapter(viewPager);
                vPager.setCurrentItem(49);
                startActivity(new Intent(MainActivity13LineQuran.this, DownloadDialog13Line.class));


            }
        }
        catch (Exception e)
        {
            Log.e("File", e.toString());
        }
    }
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("Download 13 Line Quran Files");

        builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                startActivity(new Intent(MainActivity13LineQuran.this, DownloadDialog13Line.class));
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
    private class  loadImagesFiles extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            String imgFileName;

            for(int i=1;i<801;i++)
            {
                imgFileName="img_"+i+".jpg";
                File myFile = new File(DownloadUtils13Line.rootPath13Line.getAbsolutePath(), imgFileName);

              //  Uri imguri = Uri.parse(myFile.getPath());
                imgFileName=myFile.toString();
              //  int id = getResources().getIdentifier(imgFileName, null, getPackageName());
                listimagesQuran.add(imgFileName);
            }
            for(int i=801;i<851;i++)
            {
                int id = getResources().getIdentifier("drawable/" + "img_" + i, null, getPackageName());
                listimagesQuran.add(String.valueOf(id));
            }


           // Collections.reverse(listimagesQuran);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity13LineQuran.this, "Wait", "Loading Files...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            vPager.setAdapter(viewPager);
            vPager.setCurrentItem(850);
            progressDialog.dismiss();
        }
    }


}
