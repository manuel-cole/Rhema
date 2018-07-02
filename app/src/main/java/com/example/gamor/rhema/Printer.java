package com.example.gamor.rhema;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.imagpay.Settings;
import com.imagpay.SwipeEvent;
import com.imagpay.SwipeListener;
import com.imagpay.enums.CardDetected;
import com.imagpay.enums.EmvStatus;
import com.imagpay.enums.PosModel;
import com.imagpay.enums.PrintStatus;
import com.imagpay.mpos.MposHandler;

import java.io.InputStream;
import java.util.List;

@SuppressLint("HandlerLeak")
public class Printer extends AppCompatActivity implements SwipeListener {

    private MposHandler handler;
    private Settings setting;
    private static Context context;
    protected boolean ioutP = false;

    public void sparkPrinter() {
        // Init SDK,call singleton function,so that you can keeping on the
        // connect in the whole life cycle
        // handler = MposHandler.getInstance(this);
        handler = MposHandler.getInstance(getApplicationContext(), PosModel.Z91);

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
    }


    public void printtest(final String agentName, final String custName, final String custID, final String billNumber, final String description, final Float amount, final String date,final String balance) {
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
                InputStream is = getResources().openRawResource(R.drawable.logo_small);
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
                    setting.mPosPrnStr("RHEMA PAYMENT");
                    setting.mPosPrintLn();
                    setting.mPosPrintTextSize(Settings.MPOS_PRINT_TEXT_NORMAL);
                    setting.mPosPrintAlign(Settings.MPOS_PRINT_ALIGN_LEFT);
                    setting.mPosPrnStr("Please keep safe");
                    setting.mPosPrnStr("--------------------------");
                    setting.mPosPrnStr("Agent Name: "+agentName);
                    setting.mPosPrnStr("Customer : "+custName);
                    setting.mPosPrnStr("Customer ID.: "+custID);
                    setting.mPosPrnStr("Bill Number: "+billNumber);
                    setting.mPosPrnStr("Description: "+description);
                    setting.mPosPrnStr("Amount: GHS "+amount);
                    setting.mPosPrnStr("Balance: GHS "+balance);
                    setting.mPosPrnStr("Date: "+date);
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
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        }
    };

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
                    + getApplicationContext().getResources().getText(
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
