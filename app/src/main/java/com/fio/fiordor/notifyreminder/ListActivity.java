package com.fio.fiordor.notifyreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fio.fiordor.notifyreminder.customnotify.NotifyAdapter;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.do_you_want_delete_notify);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Notify notify = adapter.getNotify(position);

                DatabaseAccess databaseAccess = new DatabaseAccess((ListActivity) getParent());
                databaseAccess.deleteNotify(notify);

                adapter.remove(notify);

                Toast.makeText(getBaseContext(), "borrado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(android.R.string.no, null);

        builder.show();
    }
}