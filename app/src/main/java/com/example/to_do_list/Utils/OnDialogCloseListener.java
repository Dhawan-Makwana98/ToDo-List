package com.example.to_do_list.Utils;

import android.content.DialogInterface;

public interface OnDialogCloseListener {
    void onDialogClose(DialogInterface dialogInterface);

    void onBackPressedDispatcher();
}