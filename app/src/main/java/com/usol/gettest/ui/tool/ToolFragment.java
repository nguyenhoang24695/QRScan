package com.usol.gettest.ui.tool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.usol.gettest.MainActivity;
import com.usol.gettest.R;
import com.usol.gettest.ScanBarcodeActivity;

import java.util.Collection;
import java.util.Objects;

public class ToolFragment extends Fragment {

    private ToolViewModel toolViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolViewModel =
                ViewModelProviders.of(this).get(ToolViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tool, container, false);

        Button button = (Button)root.findViewById(R.id.scan_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
//                startActivityForResult(intent,1);

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator .setPrompt("Đang đọc QR code - laptrinhvb.net");
                integrator.setCameraId(0);
                // beep khi scan qr thành công
                integrator.setBeepEnabled(true);
                integrator.initiateScan();

            }

        });

        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        TextView tv = (TextView) getActivity().findViewById(R.id.scan_result);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultcode, intent);

        tv.setText(result.getContents());
    }
}