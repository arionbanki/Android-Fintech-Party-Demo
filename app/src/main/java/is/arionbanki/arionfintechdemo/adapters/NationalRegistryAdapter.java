package is.arionbanki.arionfintechdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.models.NationalRegistryParty;

public class NationalRegistryAdapter extends RecyclerView.Adapter<NationalRegistryAdapter.ViewHolder> {

    private final List<NationalRegistryParty> mValues;

    public NationalRegistryAdapter(List<NationalRegistryParty> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_national_registry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NationalRegistryParty.ViewModel vm = new NationalRegistryParty.ViewModel(mValues.get(position));
        holder.accountItem = vm.model;
        holder.name.setText(vm.getFullName());
        holder.addressLine.setText(vm.getAddressLine());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView addressLine;
        public NationalRegistryParty accountItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.partyName);
            addressLine = (TextView) view.findViewById(R.id.partyAddressLine);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
