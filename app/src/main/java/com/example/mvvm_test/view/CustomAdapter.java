package com.example.mvvm_test.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm_test.R;
import com.example.mvvm_test.db.Numbers;

public class CustomAdapter extends ListAdapter<Numbers, CustomAdapter.NumbersViewHolder> {

    public CustomAdapter(@NonNull DiffUtil.ItemCallback<Numbers> diffCallback) {
        super(diffCallback);
    }

    @Override
    public NumbersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cusrom_row, parent, false);
        return new NumbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumbersViewHolder holder, int position) {
        Numbers numbers = getItem(position);

        holder.mTV_recycl.setText(numbers.getFacts());
        holder.mTV_recycl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFragment fragment = MyFragment.newInstance(numbers.getNumber(),
                        numbers.getFacts());
                FragmentManager fragmentManager = ((MainActivity) holder.itemView.getContext())
                        .getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frameLayoutHolder, fragment, "tag");
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });
    }

    static class NumbersDiff extends DiffUtil.ItemCallback<Numbers> {

        @Override
        public boolean areItemsTheSame(@NonNull Numbers oldItem, @NonNull Numbers newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Numbers oldItem, @NonNull Numbers newItem) {
            return oldItem.getFacts().equals(newItem.getFacts());
        }
    }

    static class NumbersViewHolder extends RecyclerView.ViewHolder {
        private TextView mTV_recycl;

        public NumbersViewHolder(@NonNull View itemView) {
            super(itemView);
            mTV_recycl = itemView.findViewById(R.id.mTV_recycl);
        }
    }
}

