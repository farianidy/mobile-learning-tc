package app.util.gui;

import app.mobile.learningtc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconListAdapter extends ArrayAdapter<IconListItem> {
	public IconListAdapter(Context context) {
		super(context, 0);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_icon, null);

		ImageView icon = (ImageView) convertView.findViewById(R.id.ivRowIcon);
		icon.setImageResource(getItem(position).iconRes);
		TextView title = (TextView) convertView.findViewById(R.id.tvRowTitle);
		title.setText(getItem(position).tag);

		return convertView;
	}
}
