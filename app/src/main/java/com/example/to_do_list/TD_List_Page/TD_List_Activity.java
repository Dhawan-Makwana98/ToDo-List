package com.example.to_do_list.TD_List_Page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.to_do_list.Model.ToDoModel;
import com.example.to_do_list.R;
import com.example.to_do_list.RecyclerViewTouchHelper;
import com.example.to_do_list.Utils.AddNewTask;
import com.example.to_do_list.Utils.DataBaseHelper;
import com.example.to_do_list.Utils.OnDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TD_List_Activity extends AppCompatActivity implements OnDialogCloseListener {

    private static final String TAG = "TD_List_Activity";

    private RecyclerView mrecyclerView;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;
    private DataBaseHelper myDB;
    private ImageView nodatafound;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_list);

        // Set the status bar color to match the app's theme
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        // Initialize views
        mrecyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.floatingBTN);
        nodatafound = findViewById(R.id.nodatafound);
        ImageView logout = findViewById(R.id.logout);

        // Initialize the list and database helper
        mList = new ArrayList<>();
        myDB = new DataBaseHelper(TD_List_Activity.this);
        adapter = new ToDoAdapter(myDB, TD_List_Activity.this);

        // Set up RecyclerView
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(adapter);

        // Load tasks from database and update the adapter
        mList = myDB.getAllTask();
        Collections.reverse(mList);
        adapter.setTask(mList);
        logTaskList();
        checkIfRecyclerViewIsEmpty();

        String guestValue = getIntent().getStringExtra("guest");
        Log.d("dota", "guest--" + guestValue);

        // Set up FloatingActionButton click listener
        fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));

        // Set up ItemTouchHelper for swipe-to-delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mrecyclerView);

        logout.setOnClickListener(v -> {
           /* FirebaseAuth.getInstance().signOut();
            Toast.makeText(TD_List_Activity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TD_List_Activity.this, MainActivity.class));
            finish();*/

            showExitConfirmationDialog();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        // Refresh the task list and update the adapter when a dialog is closed
        mList = myDB.getAllTask();
        Collections.reverse(mList);
        adapter.setTask(mList);
        adapter.notifyDataSetChanged();
        logTaskList();
        checkIfRecyclerViewIsEmpty();
    }

    private void logTaskList() {
        Log.d(TAG, "Current task list: " + mList.toString());
    }

    void checkIfRecyclerViewIsEmpty() {
        // Show or hide the no-data-found view based on the list content
        if (mList.isEmpty()) {
            nodatafound.setVisibility(View.VISIBLE);
            nodatafound.setImageResource(R.drawable.nodatafound1);
            mrecyclerView.setVisibility(View.GONE);
        } else {
            nodatafound.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.VISIBLE);
            nodatafound.setImageResource(R.drawable.to_do_bgimg);
        }
    }


    @Override
    public void onBackPressedDispatcher() {
        showExitConfirmationDialog();
    }


    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }
}
