package is.arionbanki.arionfintechdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.models.Account;

import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private final List<Account> mValues;

    public AccountsAdapter(List<Account> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Account.ViewModel vm = new Account.ViewModel(mValues.get(position));
        holder.accountItem = vm.model;
        holder.customName.setText(vm.getCustomName());
        holder.accountId.setText(vm.getAccountId());
        holder.balance.setText(vm.getBalanceText());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView customName;
        public final TextView accountId;
        public final TextView balance;
        public Account accountItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            accountId = (TextView) view.findViewById(R.id.accountId);
            customName = (TextView) view.findViewById(R.id.customName);
            balance = (TextView) view.findViewById(R.id.balance);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + accountId.getText() + "'";
        }
    }
}
