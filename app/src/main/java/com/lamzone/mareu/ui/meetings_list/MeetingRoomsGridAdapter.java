package com.lamzone.mareu.ui.meetings_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

public class MeetingRoomsGridAdapter extends BaseAdapter {

    private final List<MeetingRoom> mMeetingRooms;
    private final Context mContext;

    public MeetingRoomsGridAdapter(List<MeetingRoom> meetingRooms, Context context) {
        mMeetingRooms = meetingRooms;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mMeetingRooms.size();
    }

    @Override
    public MeetingRoom getItem(int position) {
        return mMeetingRooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;
        if (imageView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(316, 348));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        imageView.setImageResource(mMeetingRooms.get(position).getMeetingRoomSymbol());
        return imageView;
    }
}
