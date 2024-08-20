package com.example.to_do_list.TD_List_Page;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do_list.Model.ToDoModel;
import com.example.to_do_list.R;
import com.example.to_do_list.Utils.AddNewTask;
import com.example.to_do_list.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private static final String TAG = "ToDoAdapter";

    private List<ToDoModel> mList;
    private TD_List_Activity activity;
    private DataBaseHelper mydb;

    public ToDoAdapter(DataBaseHelper mydb, TD_List_Activity activity) {
        this.activity = activity;
        this.mydb = mydb;
        this.mList = new ArrayList<>();  // Initialize mList to avoid NullPointerException
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);

        // Set the task text
        holder.mTaskText.setText(item.getTask());

        // Set initial state of CheckBox and strike-through based on item status
        boolean isChecked = toBoolean(item.getStatus());
        holder.mCheckBox.setOnCheckedChangeListener(null);  // Remove previous listener to prevent recycling issues
        holder.mCheckBox.setChecked(isChecked);
        setStrikeThrough(holder.mTaskText, isChecked);

        // Set CheckBox change listener
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isCheckedNow) -> {
            if (isCheckedNow) {
                mydb.updateStatus(item.getId(), 1);
                holder.mImageView.setVisibility(View.VISIBLE);
                Toast.makeText(activity, "Task completed: " + item.getTask(), Toast.LENGTH_SHORT).show();
            } else {
                mydb.updateStatus(item.getId(), 0);
                holder.mImageView.setVisibility(View.GONE);
            }

            // Update the strike-through state
            setStrikeThrough(holder.mTaskText, isCheckedNow);

            // Log the current task list after an update
            logTaskList();
        });
    }



    private void setStrikeThrough(TextView taskText, boolean isChecked) {
        if (isChecked) {
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        mydb.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
        Log.d(TAG, "Task deleted: " + item.getTask());
        Toast.makeText(activity, "Task Deleted", Toast.LENGTH_SHORT).show();
        logTaskList();  // Log the current task list after a deletion

        if (mList.isEmpty()) {
        }
        // Notify the activity to check if the recycler view is empty
        if (activity != null) {
            activity.checkIfRecyclerViewIsEmpty();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        TextView mTaskText;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.taskCheckBox);
            mTaskText = itemView.findViewById(R.id.taskText);
            mImageView = itemView.findViewById(R.id.doneImg);
        }
    }

    private void logTaskList() {
        if (mList != null) {
            for (ToDoModel task : mList) {
                Log.d(TAG, "Task: " + task.getTask() + ", Status: " + task.getStatus());
            }
        } else {
            Log.d(TAG, "Task list is null");
        }
    }
}
