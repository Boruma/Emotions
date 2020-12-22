package com.example.Emotions;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Emotions.models.User;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;


public class QR_Scanner extends Fragment {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    public QR_Scanner() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r__scanner, container, false);

        txtBarcodeValue = view.findViewById(R.id.txtBarcodeValue);
        surfaceView = view.findViewById(R.id.surfaceView);
        btnAction = view.findViewById(R.id.btn_Scan_qr);

        ImageView imageView = view.findViewById(R.id.imageView7);
        imageView.setVisibility(View.VISIBLE);
        final EditText qrinput = view.findViewById(R.id.input_qr_noscan);
        Button useWhenNoQR = view.findViewById(R.id.btn_usewithout_scan);

        ActionBarDrawerToggle mDrawerToggle;
        DrawerLayout mDrawer;
        MaterialToolbar mToolbar = view.findViewById(R.id.my_toolbar);
        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout_nav);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, mToolbar, R.string.app_name, R.string.app_name);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = view.findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_settings);
                        break;
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_profileFragment);
                        break;
                    case R.id.nav_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_mapFragment);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_leaderboardFragment);
                        break;
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_roadmap);
                        break;
                }
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_leaderboard:
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_leaderboardFragment);
                        break;
                    case R.id.action_scanner:

                        break;
                    case R.id.action_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_mapFragment);
                        break;
                }
                return true;
            }
        });

        useWhenNoQR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String QRInput = qrinput.getText().toString();
                if (QRInput.length() == 0) {
                    qrinput.requestFocus();
                    qrinput.setError(getString(R.string.errorEmpty));
                } else {
                    QR_ScannerDirections.ActionQRScannerToQuestionFragment action = QR_ScannerDirections.actionQRScannerToQuestionFragment(String.valueOf(qrinput.getText()));
                    action.setPointIDS(Integer.parseInt(String.valueOf(qrinput.getText())));
                    Navigation.findNavController(getView()).navigate(action);
                }

            }
        });
        //Add UserData to DrawerHeader
        navigationView = view.findViewById(R.id.left_drawer);
        View headerView = navigationView.getHeaderView(0);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        if (user != null) {
            //Get User from Storage
            Gson gson = new Gson();
            User u1 = gson.fromJson(user, User.class);
            TextView navUsername = (TextView) headerView.findViewById(R.id.username_header);
            navUsername.setText(u1.getName());
            TextView navMail = (TextView) headerView.findViewById(R.id.mail_header);
            navMail.setText(u1.getEmail());
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseDetectorsAndSources();
    }


    private void initialiseDetectorsAndSources() {

        //Create BarcodeDetector
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //Create Camera with autodocus
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        //add the camera to the surfaceview if Permission is granted
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            //Process the QR Code
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            btnAction.setText("Weiter");
                            btnAction.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    //Add the Value of the QR Code to the Navigation as a Parameter
                                    intentData = barcodes.valueAt(0).displayValue;
                                    QR_ScannerDirections.ActionQRScannerToQuestionFragment action = QR_ScannerDirections.actionQRScannerToQuestionFragment(String.valueOf(intentData));
                                    action.setPointIDS(Integer.valueOf(intentData));
                                    Navigation.findNavController(getView()).navigate(action);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
