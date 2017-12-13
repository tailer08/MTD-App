package me.thesis.mtd_app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.thesis.mtd_app.service.MTDService;

public class Splash extends AppCompatActivity {

    private boolean isBound=false;

    private ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {}

            @Override
            public void onServiceDisconnected(ComponentName componentName) {}};

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(new Intent(Splash.this,MainActivity.class));
            finish();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MTDService.DISPLAY_HOME);
        registerReceiver(receiver, filter);

        Intent intent = new Intent(this, MTDService.class);
        intent.setAction(MTDService.ACTION_INIT_DB);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isBound) {
            bindService(new Intent(this, MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        }
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            unbindService(mConnection);
        }

        super.onDestroy();
    }
}