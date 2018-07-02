package com.example.gamor.rhema;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamor.rhema.data.model.GetBills;
import com.example.gamor.rhema.model.ResGetBiils;
import com.example.gamor.rhema.remote.APIBillGetService;
import com.example.gamor.rhema.remote.ApiUtils;
import com.example.gamor.rhema.remote.UserService;
import com.imagpay.Settings;
import com.imagpay.SwipeEvent;
import com.imagpay.SwipeListener;
import com.imagpay.enums.CardDetected;
import com.imagpay.enums.EmvStatus;
import com.imagpay.enums.PosModel;
import com.imagpay.enums.PrintStatus;
import com.imagpay.mpos.MposHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("HandlerLeak")
public class OneFragment extends Fragment implements View.OnClickListener,
        SwipeListener {

    SharedPreferences sp;

    private MposHandler handler;
    private Settings setting;
    private static Context context;
    protected boolean ioutP = false;
    APIBillGetService userService;
    List<GetBills> billsList;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_one, container, false);


        TextView textView;
//         textView = (TextView) view.findViewById(R.id.textView1);

//        textView.setText("Testing Printing");

        final EditText getBills = (EditText) view.findViewById(R.id.customerId);
        Button btnGetBills = (Button) view.findViewById(R.id.btnGetBills);


        btnGetBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerId = getBills.getText().toString();
//                Toast.makeText(getContext(), customerId, Toast.LENGTH_SHORT).show();
                gettBiils(customerId);
            }
        });

        context = getContext();

        Button bt_print = (Button) view.findViewById(R.id.bt_print);
        bt_print.setOnClickListener(this);

        // Init SDK,call singleton function,so that you can keeping on the
        // connect in the whole life cycle
        // handler = MposHandler.getInstance(this);
        handler = MposHandler.getInstance(getContext(), PosModel.Z91);

        setting = Settings.getInstance(handler);
        // power on the device when you need to read card or print
        setting.mPosPowerOn();
        try {
            // for Z90,delay 1S and then connect
            // Thread.sleep(1000);
            // connect device via serial port
            if (!handler.isConnected()) {
                sendMessage("Connect Res:" + handler.connect());
            } else {
                handler.close();
                sendMessage("ReConnect Res:" + handler.connect());
            }
        } catch (Exception e) {
            sendMessage(e.getMessage());

        }
        // add linstener for connection
        handler.addSwipeListener(this);
        // add linstener for read IC chip card
        // handler.addEMVListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_print:
                printtest();
                break;
        }
    }

    private void gettBiils(String customerId) {

        if (validateCustomerId(customerId)){

            Intent intent = new Intent(getContext(),ShowBillsActivity.class);
            intent.putExtra("Cust_Account",customerId);
            startActivity(intent);
        }

    }

    private boolean validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().length() == 0){

            Toast.makeText(getContext(), "Customer ID is required ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void showToast(String mesg) {
        Message mssg = new Message();
        mssg.what = 10;
        mssg.obj = "" + mesg;
        handleros.sendMessage(mssg);
    }

    Handler handleros = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        }
    };

    private void printtest() {
        new Thread(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                // if you need to print a big bmp,advice you to convert it to
                // printing data at first
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                InputStream is = getResources().openRawResource(R.drawable.logo_large);
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
                List<byte[]> data = setting.mPosPrnConvertBmp(bitmap);

                // call mPosEnterPrint open print
                boolean bb = setting.mPosEnterPrint();
                sendMessage("mPosEnterPrint:" + bb);
                if (bb) {
                    ioutP = false;
                    // show print receipts
                    setting.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    setting.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT);
                    setting.mPosPrnStr("RHEMA SYSTEMS");
                    setting.mPosPrintLn();
                    setting.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
                    setting.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
                    setting.mPosPrnStr("The cardholder stub   \nPlease properly keep");
                    setting.mPosPrnStr("--------------------------");
                    setting.mPosPrnStr("Merchant Name:Gamor Emmanuel");
                    setting.mPosPrnStr("Merchant No.:846584000103052");
                    setting.mPosPrnStr("Terminal No.:12345678");
                    setting.mPosPrnStr("categories: Cash");
                    setting.mPosPrnStr("Period of Validity:2016/07");
                    setting.mPosPrnStr("Batch no.:000101");
                    setting.mPosPrnStr("Card Number:");
                    setting.mPosPrnStr("622202400******0269");
                    setting.mPosPrnStr("Trade Type:consumption");
                    setting.mPosPrnStr("Serial No.:000024  \nAuthenticode:096706");
                    setting.mPosPrnStr("Date/Time:2016/09/01 11:27:12");
                    setting.mPosPrnStr("Ref.No.:123456789012345");
                    setting.mPosPrnStr("Amount:$ 1200.00");
                    setting.mPosPrnStr("--------------------------");
                    setting.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_CENTER);
                    // 1.print image with bitmap
//                     setting.mPosPrnImg(bitmap);
                    // 2.print image with printing data
                    setting.mPosPrnImg(data);
                    setting.mPosPrintLn();
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = null;
                } else {
//                    Toast.makeText(getContext(), "Enter print error!", Toast.LENGTH_SHORT).show();
                    showToast("" + getResources().getText(R.string.check_paper));
                }
            }
        }).start();
    }


    protected void sendMessage(String string) {
        Log.i("xtztt", "==>:" + string);
    }

    @Override
    public void onDestroy() {
        // power off the device when you do not need to read card or print for a
        // long time
        setting.mPosPowerOff();
        // ondestroy the sdk when you exit the app
        handler.onDestroy();
        setting.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDisconnected(SwipeEvent swipeEvent) {

    }

    @Override
    public void onConnected(SwipeEvent swipeEvent) {

    }

    @Override
    public void onStarted(SwipeEvent swipeEvent) {

    }

    @Override
    public void onStopped(SwipeEvent swipeEvent) {

    }

    @Override
    public void onReadData(SwipeEvent swipeEvent) {

    }

    @Override
    public void onParseData(SwipeEvent swipeEvent) {

        Log.i("xtztt", "" + swipeEvent.getValue());
        // 45584954205052494e54 打印缺纸
        if ((!ioutP) && "4e4f205041504552".equals(swipeEvent.getValue())) {
            ioutP = true;
            showToast(""
                    + OneFragment.this.getResources().getText(
                    R.string.out_of_paper));
        }
    }

    @Override
    public void onPermission(SwipeEvent swipeEvent) {

    }

    @Override
    public void onCardDetect(CardDetected cardDetected) {

    }

    @Override
    public void onPrintStatus(PrintStatus printStatus) {

        if (printStatus.equals(PrintStatus.IMAGES)) {
            // settings.mPosExitPrint();
            sendMessage("images print finish!");
        } else if (printStatus.equals(PrintStatus.EXIT)) {
            sendMessage("device exit print!");
        }
    }

    @Override
    public void onEmvStatus(EmvStatus emvStatus) {

    }
}
