package fr.ynov.schedule.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.ynov.schedule.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>  {

    private ArrayList<Task> mTask_list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TaskAdapter(ArrayList<Task> task_list){
        this.mTask_list = task_list;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_template_task,parent, false);
        TaskViewHolder evh = new TaskViewHolder(v,mListener);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task current_task = mTask_list.get(position);
        holder.name_task.setText(current_task.getName());
        holder.description_task.setText(current_task.getDescription());
        holder.date_task.setText(current_task.dateFormat());
        holder.image_status_task.setImageResource(current_task.getImage_status());
        holder.durée_task.setText(current_task.heureFormat());
    }

    @Override
    public int getItemCount() {
        return mTask_list.size();
    }

    public static  class TaskViewHolder extends RecyclerView.ViewHolder {

        public  TextView name_task;
        public  TextView description_task;
        public  TextView date_task;
        public ImageView image_status_task;
        public ImageView image_delete_task;
        public  TextView durée_task;

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name_task = itemView.findViewById(R.id.nameText);
            description_task = itemView.findViewById(R.id.descriptionText);
            date_task = itemView.findViewById(R.id.dateText);
            image_status_task = itemView.findViewById(R.id.statusImage);
            image_delete_task = itemView.findViewById(R.id.delete_task);
            durée_task = itemView.findViewById(R.id.durée);
            image_delete_task.setOnClickListener(new View.OnClickListener() {
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






}
