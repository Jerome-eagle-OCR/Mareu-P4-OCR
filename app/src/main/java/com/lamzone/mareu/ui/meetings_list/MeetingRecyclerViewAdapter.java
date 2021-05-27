package com.lamzone.mareu.ui.meetings_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MeetingRecyclerViewAdapter extends RecyclerView.Adapter<MeetingRecyclerViewAdapter.MeetingViewHolder> {

    private List<Meeting> mMeetings;

    private MeetingRoomRepository repository = DI.getMeetingRoomRepository();

    public MeetingRecyclerViewAdapter(List<Meeting> items) {
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
        Meeting currentMeeting = mMeetings.get(position);
        MeetingRoom currentMeetingRoom = repository.getMeetingRoomById(currentMeeting.getMeetingRoomId());
        holder.mMeetingRoomName.setText(currentMeetingRoom.getMeetingRoomName());
        holder.mMeetingRoomSymbol.setImageResource(currentMeetingRoom.getMeetingRoomSymbol());
        holder.mMeetingSubject.setText(currentMeeting.getMeetingSubject());
        holder.mMeetingParticipants.setText(currentMeeting.getMeetingParticipants().toString().replaceAll("\\[|\\]", ""));
        holder.mMeetingDate.setText(Utils.formatDate(currentMeeting.getMeetingStartTime()));

        holder.mMeetingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.cancelMeeting(currentMeeting);
                refreshList(repository.getMeetings());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public void refreshList(List<Meeting> meetings) {
        mMeetings = meetings;
        Collections.sort(mMeetings, new Utils.SortByStartTime());
        notifyDataSetChanged();
    }
}