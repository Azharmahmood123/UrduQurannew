package com.packageapp.tajweedquran;

import java.util.Arrays;
import java.util.List;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubListFragment extends Fragment {

	ListView subListView;
	public List<String> expSubList4;
	public List<String> getExpSubList5;

	int itemPos, listPos;
	private boolean isNotification=false;

	SubListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sublist_fragment, container,
				false);

		subListView = (ListView) rootView.findViewById(R.id.listView1);
		expSubList4 = Arrays.asList(getResources().getStringArray(
				R.array.sublist_4));
		getExpSubList5 = Arrays.asList(getResources().getStringArray(
				R.array.sublist_5));

		Bundle arg = this.getArguments();
		itemPos = arg.getInt("mPosition");

		if (itemPos == 0) {
			adapter = new SubListAdapter(getActivity(), expSubList4);
			TajweedActivity.toolbarTitle.setText("Components of Tajweed");
		} else {
			adapter = new SubListAdapter(getActivity(), getExpSubList5);
			TajweedActivity.toolbarTitle.setText("Rules of Tajweed");
		}
		subListView.setAdapter(adapter);

		subListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				listPos= position;
				FragmentManager fragmentManager3 = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction ftx3 = fragmentManager3.beginTransaction();
				ftx3.replace(R.id.mainFrame, detailFragment());
				ftx3.addToBackStack(null);
				ftx3.commit();
			}
		});

		if(arg.containsKey("notificationPos")&&!isNotification) // go to the page for data
		{
			isNotification=true;

			int tempId=arg.getInt("notificationPos",2);
			if(tempId>1&&tempId<12)
			{
				tempId-=2;

			}
			else if(tempId>11)
			{
				tempId-=12;
			}
			listPos= tempId;
			FragmentManager fragmentManager3 = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction ftx3 = fragmentManager3.beginTransaction();
			ftx3.replace(R.id.mainFrame, detailFragment());
			ftx3.addToBackStack(null);
			ftx3.commit();
		}

		return rootView;
	}


	private DetailPageActivity detailFragment()
	{
		Bundle arg = new Bundle();
		arg.putInt("detailID", itemPos);
		arg.putInt("itemPosition", listPos);
		DetailPageActivity fragment = new DetailPageActivity();
		fragment.setArguments(arg);
		return fragment;
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TajweedActivity.toolbarTitle.setText("Tajweed Quran");
	}

	private class SubListAdapter extends BaseAdapter {
		public List<String> SubList;
		Context context;
		LayoutInflater inflater;

		public SubListAdapter(Context contxt, List<String> subList) {
			// TODO Auto-generated constructor stub
			context = contxt;
			SubList = subList;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return SubList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return SubList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sublistrow, null);
				viewHolder = new ViewHolder();

				viewHolder.listText = (TextView) convertView
						.findViewById(R.id.subListtext);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.listText.setTypeface(((GlobalClass) context.getApplicationContext()).robotoLight);
			viewHolder.listText.setText(SubList.get(position));
			return convertView;
		}

		private class ViewHolder {
			TextView listText;
		}
	}

}
