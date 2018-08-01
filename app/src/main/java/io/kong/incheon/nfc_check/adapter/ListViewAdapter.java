package io.kong.incheon.nfc_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.ListViewBtnItem;

public class ListViewAdapter extends ArrayAdapter implements View.OnClickListener {

    public interface ListBtnClickListener {
        void onListBtnClick(int position);
    }

    int resourceId;
    private ListBtnClickListener listBtnClickListener;

    public ListViewAdapter(Context context, int resource, ArrayList<ListViewBtnItem> list, ListBtnClickListener clickListener) {
        super(context, resource, list);

        this.resourceId = resource;
        this.listBtnClickListener = clickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        final TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        final TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);

        final ListViewBtnItem listViewItem = (ListViewBtnItem) getItem(position);

        txtTitle.setText(listViewItem.getTextTitle());
        txtDate.setText(listViewItem.getTxtDate());

        Button btnSubject = (Button) convertView.findViewById(R.id.btnSubject);
        btnSubject.setTag(position);
        btnSubject.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)view.getTag());
        }
    }
}
