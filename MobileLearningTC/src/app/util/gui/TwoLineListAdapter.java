package app.util.gui;

import app.mobile.learningtc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwoLineListAdapter extends ArrayAdapter<TwoLineListItem> {
	public TwoLineListAdapter(Context context) {
		super(context, 0);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_twoline, null);

		TextView title = (TextView) convertView.findViewById(R.id.list_twoline_title);
		title.setText(getItem(position).title);
		TextView content = (TextView) convertView.findViewById(R.id.list_twoline_content);
		content.setText(getItem(position).content);

		return convertView;
	}
}
