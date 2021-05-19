package com.lamzone.mareu.ui.meetings_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

public class MeetingRecyclerViewAdapter extends RecyclerView.Adapter<MeetingRecyclerViewAdapter.MeetingViewHolder> {

    private final ArrayList<MeetingItem> mMeetings;

    public MeetingRecyclerViewAdapter(ArrayList<MeetingItem> items) {
        mMeetings = items;
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mMeetingRoomSymbol;
        private final TextView mMeetingRoomName;
        private final TextView mMeetingSubject;
        private final TextView mMeetingParticipants;
        private final TextView mMeetingDate;
        private final ImageButton mMeetingDelete;

        public MeetingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mMeetingRoomSymbol = itemView.findViewById(R.id.meeting_room_symbol);
            mMeetingRoomName = itemView.findViewById(R.id.meeting_room_name);
            mMeetingSubject = itemView.findViewById(R.id.meeting_subject);
            mMeetingParticipants = itemView.findViewById(R.id.meeting_participants);
            mMeetingDate = itemView.findViewById(R.id.meeting_date);
            mMeetingDelete = itemView.findViewById(R.id.meeting_delete);

            mMeetingParticipants.setSelected(true);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item, parent, false);
        return new MeetingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MeetingRecyclerViewAdapter.MeetingViewHolder holder, int position) {
        MeetingItem currentMeetingItem = mMeetings.get(position);

        holder.mMeetingRoomName.setText(currentMeetingItem.getMeetingRoomName());
        holder.mMeetingRoomSymbol.setImageDrawable(getDrawable(holder.mMeetingRoomSymbol.getContext(), currentMeetingItem.getMeetingRoomSymbol()));
        holder.mMeetingSubject.setText(currentMeetingItem.getMeetingSubject());
        holder.mMeetingParticipants.setText(currentMeetingItem.getMeetingParticipants());
        holder.mMeetingDate.setText(currentMeetingItem.getMeetingDate());

        holder.mMeetingDelete.setOnClickListener(v -> Toast.makeText(v.getContext(), "To be implemented !", Toast.LENGTH_LONG).show());
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }
}
