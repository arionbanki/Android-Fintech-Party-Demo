package is.arionbanki.arionfintechdemo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.arionbanki.arionfintechdemo.FinTechApplication;
import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.adapters.NationalRegistryAdapter;
import is.arionbanki.arionfintechdemo.controllers.BaseController;
import is.arionbanki.arionfintechdemo.controllers.NationalRegistryController;
import is.arionbanki.arionfintechdemo.interfaces.SearchListener;
import is.arionbanki.arionfintechdemo.models.NationalRegistryParty;


public class NationalRegistryFragment extends BaseFragment implements Handler.Callback, SearchListener {

    public static final String TAG = NationalRegistryFragment.class.getSimpleName();
    private List<NationalRegistryParty> mItems;
    private NationalRegistryAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    private NationalRegistryController mController;

    public NationalRegistryFragment() {
    }

    @SuppressWarnings("unused")
    public static NationalRegistryFragment newInstance() {
        NationalRegistryFragment fragment = new NationalRegistryFragment();
        Bundle args = new Bundle();
        args.putString("title", FinTechApplication.getContext().getString(R.string.national_registry_caption));
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
        View view = inflater.inflate(R.layout.fragment_national_registry_list, container, false);

        mItems = new ArrayList<>();

        mEmptyView = (TextView) view.findViewById(R.id.empty_recycler_view_text);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        mController = new NationalRegistryController(getFinTechApplication(), mItems);
        mController.addHandler(new Handler(this));
        mAdapter = new NationalRegistryAdapter(mItems);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);

        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(R.string.national_registry_empty_text);
        return view;
    }

    private void search(String queryString) {
        if (TextUtils.isEmpty(queryString)) {
            setRefreshing(false);
        } else {
            setRefreshing(true);
            mController.handleMessage(NationalRegistryController.MESSAGE_SEARCH_REGISTRY, queryString);
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
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
            case NationalRegistryController.MESSAGE_SEARCH_REGISTRY_COMPLETED:
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

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }
}
