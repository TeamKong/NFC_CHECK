package io.kong.incheon.nfc_check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;

public class SlidingMenu {

    Context context;
    private DrawerLayout drawerLayout;
    private View drawerView;

    public SlidingMenu(final Context context, View view) {

        this.context = context;
        this.drawerView = view;

        drawerLayout = (DrawerLayout) ((Activity)context).findViewById(R.id.drawer_layout);


        Button btnNfcCheck = (Button) ((Activity) context).findViewById(R.id.NfcCheck_menu);
        btnNfcCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NfcActivity.class);
                context.startActivity(intent);
                ((Activity) context).getApplication().fileList();
            }
        });


    }

}
