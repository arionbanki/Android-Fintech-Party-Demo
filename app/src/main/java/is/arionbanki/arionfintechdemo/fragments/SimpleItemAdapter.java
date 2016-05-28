package is.arionbanki.arionfintechdemo.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.models.SimpleItem;

public class SimpleItemAdapter extends RecyclerView.Adapter<SimpleItemAdapter.ViewHolder> {

    private final List<SimpleItem> mValues;

    public SimpleItemAdapter(List<SimpleItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_simpleitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.item1View.setText(mValues.get(position).item1);
        holder.item2View.setText(mValues.get(position).item2);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView item1View;
        public final TextView item2View;
        public SimpleItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            item1View = (TextView) view.findViewById(R.id.item1);
            item2View = (TextView) view.findViewById(R.id.item2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + item2View.getText() + "'";
        }
    }
}
