package is.arionbanki.arionfintechdemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.arionbanki.arionfintechdemo.AppData;
import is.arionbanki.arionfintechdemo.DividerItemDecoration;
import is.arionbanki.arionfintechdemo.R;
import is.arionbanki.arionfintechdemo.activities.MainActivity;
import is.arionbanki.arionfintechdemo.models.SimpleItem;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @SuppressWarnings("unused")
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simpleitem_list, container, false);

        List<SimpleItem> items = loadClaims();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new SimpleItemAdapter(items));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        Button logoutButton = (Button) view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.logout();
            }
        });

        return view;
    }

    private List<SimpleItem> loadClaims() {
        List<SimpleItem> items = new ArrayList<>();
        Map<String, String> claims = AppData.getInstance().getAll("jwt");
        for (String key : claims.keySet()) {
            items.add(new SimpleItem(key.replace("jwt.", ""), claims.get(key)));
        }
        return items;
    }
}
