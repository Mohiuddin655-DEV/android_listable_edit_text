package com.bmry.listableedittext;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmry.listableedittext.databinding.LetListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ListableEditTextAdapter extends RecyclerView.Adapter<ListableEditTextAdapter.ListableEditTextHolder> {

    private List<String> items;
    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> items) {
        this.items = items;
    }

    public void addItem(String item) {
        if (items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(int position) {
        if (getItemCount() > position) {
            this.items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public String getItemAt(int position) {
        if (items != null && items.size() > position) {
            return items.get(position);
        } else {
            return null;
        }
    }

    public List<String> getItems() {
        if (items != null) {
            return items;
        } else {
            return new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ListableEditTextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListableEditTextHolder(LetListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListableEditTextHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.onListener(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items != null && items.size() > 0) {
            return items.size();
        } else {
            return 0;
        }
    }

    public interface OnClickListener {
        void onClick(Object data, int position, boolean longClick);
    }

    public class ListableEditTextHolder extends RecyclerView.ViewHolder {

        private final LetListItemBinding binding;

        public ListableEditTextHolder(@NonNull LetListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(String item) {
            if (item != null) {
                binding.tvText.setText(item);
                hideDivider(getAdapterPosition());
            }
        }

        private void hideDivider(int position) {
            if (getItemCount() == position + 1) {
                binding.divider.setVisibility(View.GONE);
            } else {
                binding.divider.setVisibility(View.VISIBLE);
            }
        }

        private void onListener(String item) {
            if (item != null && listener != null) {
                binding.actionClear.setOnClickListener(v ->
                        listener.onClick(item, getAdapterPosition(), false));
            }
        }
    }
}
