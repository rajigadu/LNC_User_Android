package com.latenightchauffeurs.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.latenightchauffeurs.R;

public class ProminentDisclosureFragment extends DialogFragment {
    
    private Callback callback;
    private Button btnAccept,btnDeny;

    interface Callback {
        void permissionStatus(Boolean status, DialogFragment dialogFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        callback = (Callback) getTargetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_prominent_disclosure,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAccept = view.findViewById(R.id.btnAccept);
        btnDeny = view.findViewById(R.id.btnDeny);

        btnAccept.setOnClickListener(view1 -> {
            callback.permissionStatus(true, this);
        });
        btnDeny.setOnClickListener(view1 -> {
            callback.permissionStatus(false, this);
            dismissAllowingStateLoss();
        });
    }
}
