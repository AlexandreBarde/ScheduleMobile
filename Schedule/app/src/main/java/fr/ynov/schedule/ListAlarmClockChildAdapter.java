package fr.ynov.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAlarmClockChildAdapter extends RecyclerView.Adapter<ListAlarmClockChildAdapter.AlarmClockViewHolder> {
    private ArrayList<AlarmClock> alarmClockList;
    public static class AlarmClockViewHolder extends RecyclerView.ViewHolder {
        public TextView hourAlarmClock;
        public Switch onOffAlarmClock;
        public ImageButton onDelete;

        public AlarmClockViewHolder(@NonNull View alarmClockView) {
            super(alarmClockView);
            hourAlarmClock = alarmClockView.findViewById(R.id.alarmClock);
            onOffAlarmClock = alarmClockView.findViewById(R.id.onOff);
            onDelete = alarmClockView.findViewById(R.id.deleteAlarm);
        }
    }
    public ListAlarmClockChildAdapter(ArrayList<AlarmClock> c_alarmClockList) {
        alarmClockList = c_alarmClockList;
    }
    @NonNull
    @Override
    public AlarmClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_template_alarm_clock, parent, false);
        AlarmClockViewHolder acvh = new AlarmClockViewHolder(view);
        return acvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmClockViewHolder holder, int position) {
        AlarmClock currentAlarmClock = alarmClockList.get(position);
        holder.hourAlarmClock.setText(currentAlarmClock.getHourAlarmClock());
    }

    @Override
    public int getItemCount() {
        return alarmClockList.size();
    }
}
