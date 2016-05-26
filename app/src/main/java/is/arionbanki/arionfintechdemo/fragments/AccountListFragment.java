package is.arionbanki.arionfintechdemo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.adapters.AccountsAdapter;
import is.arionbanki.arionfintechdemo.controllers.AccountController;
import is.arionbanki.arionfintechdemo.controllers.BaseController;
import is.arionbanki.arionfintechdemo.models.Account;


public class AccountListFragment extends BaseFragment implements Handler.Callback {

    public static final String TAG = AccountListFragment.class.getSimpleName();
    private List<Account> mItems;
    private AccountsAdapter mAdapter;
    private View mEmptyView;
    private ContentLoadingProgressBar bar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AccountController mController;

    public AccountListFragment() {
    }

    @SuppressWarnings("unused")
    public static AccountListFragment newInstance() {
        AccountListFragment fragment = new AccountListFragment();
        Bundle args = new Bundle();
        args.putString("title", FinTechApplication.getContext().getString(R.string.accounts_caption));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);

        mItems = new ArrayList<>();

        mEmptyView = view.findViewById(R.id.empty_recycler_view_text);


        mController = new AccountController(getFinTechApplication(), mItems);
        mController.addHandler(new Handler(this));
        mAdapter = new AccountsAdapter(mItems);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        setRefreshing(true);
        mController.handleMessage(AccountController.MESSAGE_GET_ACCOUNTS, null);
    }

    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (!isAdded()) return false;
        Log.d(TAG, "handleMessage() called with: " + "message = [" + message + "]");
        setRefreshing(false);

        switch (message.what) {
            case BaseController.MESSAGE_ERROR:
                handleException(message);
                return true;
            case AccountController.MESSAGE_GET_ACCOUNTS_COMPLETED:
                mAdapter.notifyDataSetChanged();
                setEmptyViewVisibility();
                return true;
            default:
                Log.e(TAG, "handleMessage: UNHANDLED message: " + message);

        }
        return false;
    }

    private void setEmptyViewVisibility() {
        mEmptyView.setVisibility(mItems.size() > 0 ? View.GONE : View.VISIBLE);
    }

}
