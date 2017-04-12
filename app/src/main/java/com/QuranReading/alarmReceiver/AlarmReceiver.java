package com.QuranReading.alarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.QuranReading.adapter.XMLParser;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.NotificationClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.SplashActivity;
import com.packageapp.HomeSplashActivity;

public class AlarmReceiver extends BroadcastReceiver {
	int surahPos;
	int ayaNo;
	Context context;
	SettingsSharedPref settngPref;
	ArrayList<String> surahSizeList = new ArrayList<String>();
	private XmlPullParser xpp;
	private String bismillahText;
	private CharSequence message;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		settngPref = new SettingsSharedPref(context);

		randInt();
		randAyaNo();
		// it is used to handle async task scenerio in surah activity When notification occured and setting menu was opened
		((GlobalClass) context.getApplicationContext()).notificationOcurd = true;
		showNamazNotification(surahPos);
	}

	private void randAyaNo() {
		setNotificationLanguage(settngPref.getTranslationLanguage());
	}

	

	public void randInt() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int min = 1, max = 114;

		if (day == Calendar.FRIDAY) {
			surahPos = 18;
			Log.v("Day", "Friday");
		}
		else 
		{
			surahPos = (int) (min + (Math.random() * max));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public void showNamazNotification(int pos) {
		int notificationID = GlobalClass.notifID;
		String name = getSurahName();

		Intent intnt = new Intent(context, HomeSplashActivity.class);
		intnt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intnt.putExtra("SURAHDAYNO", surahPos);
		intnt.putExtra("AYANO", ayaNo);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, intnt, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		//Notification notif = new Notification(R.drawable.ic_launcher, name, System.currentTimeMillis());
		NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
				notif.setContentTitle(name);
		notif.setContentText(message);
		notif.setTicker(name);
		notif.setWhen(System.currentTimeMillis());
		notif.setContentIntent(pendingIntent);
		notif.setDefaults(Notification.DEFAULT_SOUND);
		notif.setAutoCancel(true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			//notif.setSmallIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_launcher));
			notif.setSmallIcon(R.mipmap.ic_launcher_notification);


		}
		else
		{
			notif.setSmallIcon(R.mipmap.ic_launcher);
		}

		notif.setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_launcher));
		Notification notification=notif.build();
		CharSequence from = name;
		//notif.setLatestEventInfo(context, from, message, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		nm.notify(notificationID, notification);
	}

	public String getSurahName() {
		String name = "";
		String[] namesArray = {};

		int resrcNamesArray;

		resrcNamesArray = context.getResources().getIdentifier("surah_names", "array", context.getPackageName());

		if (resrcNamesArray > 0) {
			namesArray = context.getResources().getStringArray(resrcNamesArray);
			name = "Surah " + namesArray[surahPos - 1];
		}

		return name;
	}
	
	private void setNotificationLanguage(int translationLanguage) {


		switch (translationLanguage)
		{

		case 0:
		{
			xpp = context.getResources().getXml(R.xml.english_translation);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngSaheeh);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 1:
		{
			xpp = context.getResources().getXml(R.xml.urdu_translation_ahmedrazakhan);
			bismillahText = context.getResources().getString(R.string.bismillahTextUrdu);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_urdu) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 2:
		{
			xpp = context.getResources().getXml(R.xml.urdu_translation_maududi);
			bismillahText = context.getResources().getString(R.string.bismillahTextUrdu_mududi);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_urdu) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 3:
		{
			xpp = context.getResources().getXml(R.xml.urdu_translation_jalandhry);
			bismillahText = context.getResources().getString(R.string.bismillahTextUrdu_jalandri);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_urdu) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 4:
		{
			xpp = context.getResources().getXml(R.xml.urdu_translation_junagarhi);
			bismillahText = context.getResources().getString(R.string.bismillahTextUrdu_junagari);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_urdu) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
			

		case 5:
		{
			xpp = context.getResources().getXml(R.xml.english_translation);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngSaheeh);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
	
//////melayu
		case 6:
		{
			xpp = context.getResources().getXml(R.xml.eng_translation_pickthal);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngPickthal);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 7:
		{
			xpp = context.getResources().getXml(R.xml.eng_translation_shakir);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngShakir);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 8:
		{
			xpp = context.getResources().getXml(R.xml.eng_translation_maududi);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngMadudi);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 9:
		{
			xpp = context.getResources().getXml(R.xml.eng_translation_daryabadi);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngDarayabadi);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 10:
		{
			xpp = context.getResources().getXml(R.xml.eng_translation_yusufali);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngYusaf);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		

		case 11:
		{
			xpp = context.getResources().getXml(R.xml.spanish_cortes_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextSpanishCortes);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_spanish) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 12:
		{
			xpp = context.getResources().getXml(R.xml.french_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextFrench);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_french) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 13:
		{
			xpp = context.getResources().getXml(R.xml.chinese_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextChinese);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_chinese) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 14:
		{
			xpp = context.getResources().getXml(R.xml.persian_ghoomshei_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextPersianGhommshei);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_persian) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 15:
		{
			xpp = context.getResources().getXml(R.xml.italian_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextItalian);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_italian) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 16:
		{
			xpp = context.getResources().getXml(R.xml.dutch_trans_keyzer);
			bismillahText = context.getResources().getString(R.string.bismillahTextDutchKeyzer);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_dutch) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;

		case 17:
		{
			xpp = context.getResources().getXml(R.xml.indonesian_bhasha_trans);
			bismillahText = context.getResources().getString(R.string.bismillahTextIndonesianBahasha);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_indonesian) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
			
		case 18:
		{
			xpp = context.getResources().getXml(R.xml.malay_basmeih);
			bismillahText = context.getResources().getString(R.string.bismillahTextMalay);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_malay) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;	
			
		case 19:
		{
			xpp = context.getResources().getXml(R.xml.hindi_suhel_farooq_khan_and_saifur_rahman_nadwi);
			bismillahText = context.getResources().getString(R.string.bismillahTextHindi);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_hindi) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 20:
		{
			xpp = context.getResources().getXml(R.xml.arabic_jalaleen);
			bismillahText = context.getResources().getString(R.string.bismillah);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_arabic) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 21:
		{
			xpp = context.getResources().getXml(R.xml.turkish_diyanet_isleri);
			bismillahText = context.getResources().getString(R.string.bismillahTextTurkish);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_turkish) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 22:
		{
			xpp = context.getResources().getXml(R.xml.bangali_zohurul_hoque);
			bismillahText = context.getResources().getString(R.string.bismillahTextBengali);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_bengali) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 23:
		{
			xpp = context.getResources().getXml(R.xml.russian_abuadel);
			bismillahText = context.getResources().getString(R.string.bismillahTextRussian);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_russian) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 24:
		{
			xpp = context.getResources().getXml(R.xml.japanese);
			bismillahText = context.getResources().getString(R.string.bismillahTextJapan);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_japanese) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 25:
		{
			xpp = context.getResources().getXml(R.xml.pourteges);
			bismillahText = context.getResources().getString(R.string.bismillahTextPortuguese);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_portugese) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
		case 26:
		{
			xpp = context.getResources().getXml(R.xml.thai);
			bismillahText = context.getResources().getString(R.string.bismillahTextThai);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day_thai) + " - " + surahSizeList.get(ayaNo).toString();
		}
			break;
	

		default:
		{
			xpp = context.getResources().getXml(R.xml.english_translation);
			bismillahText = context.getResources().getString(R.string.bismillahTextEngSaheeh);
			surahSizeList = XMLParser.getTranslatedAyaList(context, xpp, surahPos, bismillahText);
			int min = 1, max = surahSizeList.size() - 1;
			// Log.d("Random No", "Generate");
			ayaNo = (int) (min + (Math.random() * max));
			message = context.getResources().getString(R.string.aya_of_day) + " - " + surahSizeList.get(ayaNo).toString();
		}
		}
	}
}