					/* In the name of GOD, the Most Gracious, the Most Merciful */
/*
 *	Date : 27th of May 2011
 *	Description : Read from relevant XML file and return data
 *
 * 	Author	: Jazarine Jamal
 *  E-Mail 	: jazarinester@gmail.com
 *  Web		: http://www.jazarine.org
 * */
package com.QuranReading.adapter;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;

import com.QuranReading.model.JuzModel;

					public class XMLParser
{
	public static ArrayList<String> getTranslatedAyaList(Context context,XmlPullParser xpp,int position, String x)
	{
		ArrayList<String> ayahList = new ArrayList<String>();
		try 
		{
			boolean suraFound=false;
			boolean suraFoundandFinishedGet = false;
			
			ayahList.add(x);
			while ((xpp.getEventType()!=XmlPullParser.END_DOCUMENT))
			{
				if (xpp.getEventType()==XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("sura"))
					{
						if(xpp.getAttributeValue(0).equals(position+""))
						{
							xpp.nextTag();
							suraFound = true;
							suraFoundandFinishedGet=false;
							continue;
						}
						else
						{
							if (suraFound)
							{
								suraFoundandFinishedGet = true;
							}
						}
					}
					if(suraFoundandFinishedGet)
					{
						break;
					}
					if (xpp.getName().equals("aya") && suraFound)
					{
						String translatedAya=xpp.getAttributeValue(1).toString();
						translatedAya.trim();
						ayahList.add(translatedAya);
						
					}
				}
				xpp.next();
			}
		}
		catch(Exception ex)
		{
			return ayahList;
		}
		return ayahList;
	}
	//Tafseer uthmani  Tarjuma parsing
	public static ArrayList<String> getAyaTafseerTranslationList(Context context,XmlPullParser xpp,int position, String x)
	{
		ArrayList<String> tafseerTranslationList = new ArrayList<String>();
		try
		{
			boolean suraFound=false;
			boolean suraFoundandFinishedGet = false;

			tafseerTranslationList.add(x);
			while ((xpp.getEventType()!=XmlPullParser.END_DOCUMENT))
			{
				if (xpp.getEventType()==XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("sura"))
					{
						if(xpp.getAttributeValue(0).equals(position+""))
						{
							xpp.nextTag();
							suraFound = true;
							suraFoundandFinishedGet=false;
							continue;
						}
						else
						{
							if (suraFound)
							{
								suraFoundandFinishedGet = true;
							}
						}
					}
					if(suraFoundandFinishedGet)
					{
						break;
					}
					if (xpp.getName().equals("aya") && suraFound)
					{
						String tafseerText=xpp.getAttributeValue(1).toString();
						tafseerText.trim();
						tafseerTranslationList.add(tafseerText);

					}
				}
				xpp.next();
			}
		}
		catch(Exception ex)
		{
			return tafseerTranslationList;
		}
		return tafseerTranslationList;
	}

	//Tafseer uthmani  tafseer parsing
	public static ArrayList<String> getAyaTafseerList(Context context,XmlPullParser xpp,int position, String x)
	{
		ArrayList<String> tafseerList = new ArrayList<String>();
		try
		{
			boolean suraFound=false;
			boolean suraFoundandFinishedGet = false;

			tafseerList.add(x);
			while ((xpp.getEventType()!=XmlPullParser.END_DOCUMENT))
			{
				if (xpp.getEventType()==XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("sura"))
					{
						if(xpp.getAttributeValue(0).equals(position+""))
						{
							xpp.nextTag();
							suraFound = true;
							suraFoundandFinishedGet=false;
							continue;
						}
						else
						{
							if (suraFound)
							{
								suraFoundandFinishedGet = true;
							}
						}
					}
					if(suraFoundandFinishedGet)
					{
						break;
					}
					if (xpp.getName().equals("aya") && suraFound)
					{
						String tafseerText=xpp.getAttributeValue(2).toString();
						tafseerText.trim();
						tafseerList.add(tafseerText);

					}
				}
				xpp.next();
			}
		}
		catch(Exception ex)
		{
			return tafseerList;
		}
		return tafseerList;
	}


	
	public static ArrayList<String> getTranslatedAyaListArabic(Context context,XmlPullParser xpp,int position, String x)
	{
		ArrayList<String> ayahList = new ArrayList<String>();
		try 
		{
			boolean suraFound=false;
			boolean suraFoundandFinishedGet = false;
//			ayahList.add(x);
			while ((xpp.getEventType()!=XmlPullParser.END_DOCUMENT))
			{
				if (xpp.getEventType()==XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("sura"))
					{
						if(xpp.getAttributeValue(0).equals(position+""))
						{
							xpp.nextTag();
							suraFound = true;
							suraFoundandFinishedGet=false;
							continue;
						}
						else
						{
							if (suraFound)
							{
								suraFoundandFinishedGet = true;
							}
						}
					}
					if(suraFoundandFinishedGet)
					{
						break;
					}
					if (xpp.getName().equals("surah") && suraFound)
					{
						String translatedAya=xpp.getAttributeValue(2).toString();
						translatedAya.trim();
						ayahList.add(translatedAya);
						
					}
				}
				xpp.next();
			}
		}
		catch(Exception ex)
		{
			return ayahList;
		}
		return ayahList;
	}

	//Juz related juz,surah and aya attribute list
	public static ArrayList<JuzModel> getJuzData(Context context, XmlPullParser xpp)
	{
		ArrayList<JuzModel> juzDataList = new ArrayList<JuzModel>();
		try
		{
			boolean suraFound=false;
			boolean suraFoundandFinishedGet = false;
//			ayahList.add(x);
			while ((xpp.getEventType()!=XmlPullParser.END_DOCUMENT))
			{
				if (xpp.getEventType()==XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("juzs"))
					{
						if(xpp.getAttributeValue(0).equals("parts"))
						{
							xpp.nextTag();
							suraFound = true;
							suraFoundandFinishedGet=false;
							continue;
						}
						else
						{
							if (suraFound)
							{
								suraFoundandFinishedGet = true;
							}
						}
					}
					if(suraFoundandFinishedGet)
					{
						break;
					}
					if (xpp.getName().equals("juz") && suraFound)
					{
						String JuzNo=xpp.getAttributeValue(0).toString();
						String SurahNo=xpp.getAttributeValue(1).toString();
						String AyahNo=xpp.getAttributeValue(2).toString();
						JuzNo.trim();
						SurahNo.trim();
						AyahNo.trim();
						JuzModel juzModel=new JuzModel(Integer.parseInt(JuzNo),Integer.parseInt(SurahNo),Integer.parseInt(AyahNo));
						juzDataList.add(juzModel);

					}
				}
				xpp.next();
			}
		}
		catch(Exception ex)
		{
			return juzDataList;
		}
		return juzDataList;
	}
}