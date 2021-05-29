package com.example.pets.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;

public class PetCursor extends CursorAdapter {

    public PetCursor(Context context, Cursor cursor)
    {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
       return LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView petType=(TextView) view.findViewById(R.id.name);
        TextView sum=(TextView) view.findViewById(R.id.summary);

      int columenameINDEX=cursor.getColumnIndex(petContract.Shelter.COLUMN_PET_NAME);
      int columebreedINDEX=cursor.getColumnIndex(petContract.Shelter.COLUMN_PET_BREED);

      String name=cursor.getString(columenameINDEX);
      String breed=cursor.getString(columebreedINDEX);

      petType.setText(name);
      sum.setText(breed);
    }


}
