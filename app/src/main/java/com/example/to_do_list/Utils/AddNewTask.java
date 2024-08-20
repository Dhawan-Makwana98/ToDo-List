package com.example.to_do_list.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.to_do_list.Model.ToDoModel;
import com.example.to_do_list.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveBtn;
    private DataBaseHelper myDB;
    private boolean isUpdate;
    private Bundle bundle;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_newtask, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        mEditText = view.findViewById(R.id.edt_task);
        mSaveBtn = view.findViewById(R.id.btn_save);

        // Initialize database helper
        myDB = new DataBaseHelper(requireActivity());

        // Check if updating an existing task
        bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task", "");
            mEditText.setText(task);
            updateSaveButtonState(!task.isEmpty());
        }

        // Add text watcher to enable/disable the save button based on input
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Save button click listener
        mSaveBtn.setOnClickListener(v -> {
            String text = mEditText.getText().toString().trim();

            if (text.isEmpty()) {
                mEditText.setError("Task cannot be empty");
                return;
            }

            if (isUpdate && bundle != null) {
                // Update existing task
                int id = bundle.getInt("id", -1);
                if (id != -1) {
                    myDB.updateTask(id, text);
                    Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Task updated: " + text);
                }
            } else {
                // Insert new task
                ToDoModel item = new ToDoModel();
                item.setTask(text);
                item.setStatus(0); // Assuming 0 is the default status
                myDB.insertTask(item);
                Log.d(TAG, "Task added: " + text);
            }

            dismiss(); // Close the dialog
        });
    }


    private void updateSaveButtonState(boolean isEnabled) {
        // Enable or disable the save button and set its color
        mSaveBtn.setEnabled(isEnabled);
        int color = isEnabled ? ContextCompat.getColor(requireContext(), R.color.appColor) : Color.GRAY;
        mSaveBtn.setTextColor(Color.WHITE); // Text color is set to white
        mSaveBtn.setBackgroundColor(color);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Notify the parent activity to refresh the task list
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
