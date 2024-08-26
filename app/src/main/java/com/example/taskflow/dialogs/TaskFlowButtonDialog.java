package com.example.taskflow.dialogs;

import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.taskflow.MainActivity;
import com.example.taskflow.R;
import com.example.taskflow.RetrofitClient;
import com.example.taskflow.enums.SortedTaskEnum;

public class TaskFlowButtonDialog {
    private final MainActivity mainActivity;
    private final SortedTaskEnum sortedTask;

    private static final String LOGOUT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJtYWlsIjoiZHVtNHVrOTJAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6ItCU0LzQuNGC0YDQuNC5INCW0LjRgNGO0YLQuNC9IiwiaWF0IjoxNzIwODgzMTQ5LCJleHAiOjE3MjEwNjMxNDl9.XCJdIK0CY2ilAy0bBDPMCxpwJhRZPbfF_r4cWpQe4i4";

    public TaskFlowButtonDialog(MainActivity mainActivity, SortedTaskEnum sortedTask) {
        this.mainActivity = mainActivity;
        this.sortedTask = sortedTask;
    }

    public void show() {
        ImageView image = mainActivity.findViewById(R.id.taskFlowImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mainActivity, view);
                popupMenu.getMenuInflater().inflate(R.menu.task_flow_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {

                    if (item.getItemId() == R.id.action_logout) {
                        RetrofitClient.updateToken(LOGOUT_TOKEN);
                        mainActivity.initApi();
                        mainActivity.getAllMyTasks(sortedTask);
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }

        });
    }

}
