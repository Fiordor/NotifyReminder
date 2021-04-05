package com.fio.fiordor.notifyreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fio.fiordor.notifyreminder.adapters.NotifyAdapter;
import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.threads.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements NotifyAdapter.OnItemClickListener, NotifyAdapter.OnItemLongClickListener {

    private NotifyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvNotifyList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        adapter = new NotifyAdapter(new ArrayList<Notify>(), this::onItemClickListener, this::onItemLongClickListener);
        recyclerView.setAdapter(adapter);

        DatabaseAccess access = new DatabaseAccess(this);
        access.loadAllNotifies();

    }

    public void createNewReminder(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public void updateList(List<Notify> notifyList) {
        adapter.updateNotifies(notifyList);
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }
}