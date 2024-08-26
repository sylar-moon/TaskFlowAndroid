package com.example.taskflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskflow.R;
import com.example.taskflow.RetrofitClient;
import com.example.taskflow.dto.SubtaskDto;
import com.example.taskflow.api.SubtaskApi;
import com.example.taskflow.models.SubtaskModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubtaskListViewAdapter extends ArrayAdapter<SubtaskModel> {
    private Context context;
    private List<SubtaskModel> subtasks;
    private SubtaskApi subtaskApi;

    public SubtaskListViewAdapter(@NonNull Context context, List<SubtaskModel> subtasks) {
        super(context, 0,subtasks);
        this.context=context;
        this.subtasks=subtasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_task_view, parent, false);
        }
        subtaskApi =  RetrofitClient.getRetrofitInstance().create(SubtaskApi.class);

        SubtaskModel subtask = subtasks.get(position);

        TextView nameSubtask = convertView.findViewById(R.id.subTaskName);
        RadioButton statusRadioButton = convertView.findViewById(R.id.statusRadioButton);

        System.out.println("is complete = "+subtask.isComplete());
        System.out.println("name = "+subtask.getName());
        System.out.println("id = "+subtask.getId());


        statusRadioButton.setOnCheckedChangeListener(null);

        statusRadioButton.setChecked(subtask.isComplete());




        statusRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusRadioButton.setChecked(!statusRadioButton.isChecked());
                subtask.setComplete(!subtask.isComplete());

                Call<SubtaskDto> call = subtaskApi.updateTaskState(subtask.getId());
                call.enqueue(new Callback<SubtaskDto>() {
                    @Override
                    public void onResponse(Call<SubtaskDto> call, Response<SubtaskDto> response) {
                        System.out.println("new subtask status: "+response.body().getStatus());
                    }

                    @Override
                    public void onFailure(Call<SubtaskDto> call, Throwable t) {
                        System.out.println(t.fillInStackTrace().getMessage());
                        System.out.println(call.request());
                        Toast.makeText(context, "Запрост на изменение статуса не выполнен" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                notifyDataSetChanged();
            }
        });


        nameSubtask.setText(subtask.getName());

        return convertView;

    }
}
