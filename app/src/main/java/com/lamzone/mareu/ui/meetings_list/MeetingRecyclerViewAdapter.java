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

import java.util.List;

public class MeetingRecyclerViewAdapter extends RecyclerView.Adapter<MeetingRecyclerViewAdapter.MeetingViewHolder> {

    private List<Meeting> mMeetings;
    private final MeetingRoomRepository repository = DI.getMeetingRoomRepository();
    private Utils.FilterType mFilterType = Utils.FilterType.NONE;
    private long mFilterValue;


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

        holder.mMeetingDelete.setOnClickListener(v -> {
            repository.cancelMeeting(currentMeeting);
            notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            if (!mFilterType.equals(Utils.FilterType.NONE)) {
                mMeetings.remove(currentMeeting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public void refreshList(Utils.FilterType filterType, long filterValue) {
        mFilterType = filterType;
        mFilterValue = filterValue;
        switch (filterType) {
            case NONE:
                mMeetings = repository.getMeetings();
                break;
            case BY_DATE:
                mMeetings = repository.getMeetingsForGivenDate(filterValue);
                break;
            case BY_MEETING_ROOM:
                mMeetings = repository.getMeetingsForGivenMeetingRoom(filterValue);
                break;
        }
        notifyDataSetChanged();
    }

    public void refreshList() {
        this.refreshList(mFilterType, mFilterValue);
    }
}