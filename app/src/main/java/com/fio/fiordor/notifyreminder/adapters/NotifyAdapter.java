package com.fio.fiordor.notifyreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fio.fiordor.notifyreminder.R;
import com.fio.fiordor.notifyreminder.pojo.Notify;

import java.util.List;
import java.util.Locale;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {

    private List<Notify> notifies;
    private OnItemClickListener onItemClick;
    private OnItemLongClickListener onItemLongClick;

    public NotifyAdapter(List<Notify> notifies, OnItemClickListener onItemClick, OnItemLongClickListener onItemLongClick) {
        this.notifies = notifies;
        this.onItemClick = onItemClick;
        this.onItemLongClick = onItemLongClick;
    }

    public void addNotify(Notify notify) {
        notifies.add(notify);
        notifyDataSetChanged();
    }

    public void updateNotifies(List<Notify> notifies) {
        this.notifies = notifies;
        notifyDataSetChanged();
    }

    public Notify getNotify(int position) {
        return notifies.get(position);
    }

    public void remove(Notify notify) {
        notifies.remove(notify);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notify, parent, false);
        NotifyAdapter.ViewHolder holder = new ViewHolder(view, onItemClick, onItemLongClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notify notify = notifies.get(position);
        holder.setTitle(notify.getTitle());
        holder.setDate(notify.getDayOfMonth(), notify.getMonth(), notify.getYear());
        holder.setTime(notify.getHour(), notify.getMinute());
        holder.setText(notify.getText());
    }

    @Override
    public int getItemCount() {
        return notifies.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView day;
        private TextView month;
        private TextView year;
        private TextView time;
        private TextView text;

        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.tvRowTitle);
            day = itemView.findViewById(R.id.tvRowDay);
            month = itemView.findViewById(R.id.tvRowMonth);
            year = itemView.findViewById(R.id.tvRowYear);
            time = itemView.findViewById(R.id.tvRowTime);
            text = itemView.findViewById(R.id.tvRowText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClickListener(getAdapterPosition());
                    return true;
                }
            });
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setDate(int day, int month, int year) {
            this.day.setText((day < 10) ? "0" + day : String.valueOf(day));
            this.month.setText((month < 10) ? "0" + month : String.valueOf(month));
            this.year.setText(String.valueOf(year));
        }

        public void setTime(int hour, int minute) {
            this.time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        }

        public void setText(String text) {
            this.text.setText(text);
        }
    }
}
