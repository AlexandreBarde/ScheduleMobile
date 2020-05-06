package fr.ynov.schedule.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.ynov.schedule.R;

public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.AlarmClockViewHolder> {

    private ArrayList<AlarmClock> alarmClockList;
    private  OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AlarmClockViewHolder extends RecyclerView.ViewHolder {
        public TextView hourAlarmClock;
        public Switch onOffAlarmClock;
        public ImageButton onDelete;
        public TextView dayAlaramClock;
        public ImageView  delete_alarm;

        public AlarmClockViewHolder(@NonNull View alarmClockView, final OnItemClickListener listener) {
            super(alarmClockView);
            hourAlarmClock = alarmClockView.findViewById(R.id.alarmClock);
            onOffAlarmClock = alarmClockView.findViewById(R.id.onOff);
            onDelete = alarmClockView.findViewById(R.id.deleteAlarm);
            dayAlaramClock = alarmClockView.findViewById(R.id.jourClock);
            delete_alarm = alarmClockView.findViewById(R.id.delete_alarm);
            delete_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }

                }
            });
        }
    }
    public AlarmClockAdapter(ArrayList<AlarmClock> c_alarmClockList) {
        alarmClockList = c_alarmClockList;
    }
    @NonNull
    @Override
    public AlarmClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_parents_show_alarm_clock, parent, false);
        AlarmClockViewHolder acvh = new AlarmClockViewHolder(view,mListener);
        return acvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmClockViewHolder holder, int position) {
        AlarmClock currentAlarmClock = alarmClockList.get(position);
        holder.hourAlarmClock.setText(currentAlarmClock.getHourAlarmClock());
        holder.onOffAlarmClock.setChecked(currentAlarmClock.getActivation());
        holder.dayAlaramClock.setText(currentAlarmClock.getDay());
    }


    @Override
    public int getItemCount() {
        return alarmClockList.size();
    }
}
