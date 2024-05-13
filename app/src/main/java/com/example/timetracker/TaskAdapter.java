package com.example.timetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskData> taskList;

    public TaskAdapter(List<TaskData> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_activity, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskData taskData = taskList.get(position);
        holder.bind(taskData);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addTask(TaskData taskData) {
        taskList.add(taskData);
        notifyItemInserted(taskList.size() - 1);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTaskName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
           // textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
        }

        public void bind(TaskData taskData) {
            textViewTaskName.setText(taskData.getTaskName());
        }
    }
}
