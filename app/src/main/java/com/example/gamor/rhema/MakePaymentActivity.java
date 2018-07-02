package com.example.gamor.rhema;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamor.rhema.data.model.PostPayBill;
import com.example.gamor.rhema.data.model.PostPayBillMobileMoney;
import com.example.gamor.rhema.remote.APIServicePayBill;
import com.example.gamor.rhema.remote.APIServicePayBillMobileMoney;
import com.example.gamor.rhema.remote.ApiUtils;
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

import static android.content.ContentValues.TAG;

@SuppressLint("HandlerLeak")
public class MakePaymentActivity extends AppCompatActivity implements View.OnClickListener,
        SwipeListener {

    TextView namePlaceView,locationPlaceView,numberPlaceView,idPlaceView,textViewBillNo,textViewDescription,textViewOriginalAcnt,textViewBalance;
    EditText payAmount;
    Button btnPay;
    String selectedtem, paytype;
    private APIServicePayBill apiServicePayBill;
    private APIServicePayBillMobileMoney apiServicePayBillMobileMoney;
    private AlertDialog progressDialog;
    private SharedPreferences sp;
    private CustomProgressDialogOne customProgressDialogOne;

    private MposHandler handler;
    private Settings setting;
    private static Context context;
    protected boolean ioutP = false;

    private final static String PRIMARY_CALLBACK_URL = "http://102.176.96.33:8080/api/PaymentWebhook";
    private final static String SECONDARY_CALLBACK_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_payment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
        final String agentName =  sp.getString("FullName","");

        //start the printer
        sparkPrinter();

        //Custom Progress Diaglog
        customProgressDialogOne = new CustomProgressDialogOne();

//        progressDialog = new ProgressDialog(MakePaymentActivity.this);
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setTitle("Initialising Customer Payment"); // Setting Title
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.setCancelable(false);

        View dialogView = LayoutInflater.from(MakePaymentActivity.this).inflate(R.layout.custom_progress_dialog_one, null);
        progressDialog = new android.app.AlertDialog.Builder(MakePaymentActivity.this).create();
        progressDialog.setTitle("Initialising Customer Payment"); // Setting Title
        progressDialog.setCancelable(false);
        progressDialog.setView(dialogView);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();
//            AlertDialog b = progressDialog.create();



        namePlaceView = (TextView) findViewById(R.id.namePlaceViewPayment);
        locationPlaceView = (TextView) findViewById(R.id.locationPlaceViewPayment);
        numberPlaceView = (TextView) findViewById(R.id.numberPlaceViewPayment);
        idPlaceView = (TextView) findViewById(R.id.idPlaceViewPayment);

        textViewBillNo = (TextView) findViewById(R.id.textViewBillNoPayment);
        textViewDescription = (TextView) findViewById(R.id.textViewDescriptionPayment);
        textViewOriginalAcnt = (TextView) findViewById(R.id.textViewOriginalAcntPayment);
        textViewBalance = (TextView) findViewById(R.id.textViewBalancePayment);


        //Get posted variables
        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("name").trim();
        final String location = extras.getString("location").trim();
        final String number = extras.getString("number").trim();
        final String id = extras.getString("id").trim();
        final String billNumber = extras.getString("billNumber").trim();
        final String description = extras.getString("description").trim();
        final String account = extras.getString("account").trim();
        final String balance = extras.getString("balance").trim();

        final String email = "";
        final String clientRef = "";
        final String token = "";


        //seting posted variables into activity
        namePlaceView.setText(name);
        locationPlaceView.setText(location);
        numberPlaceView.setText(number);
        idPlaceView.setText(id);
        textViewBillNo.setText(billNumber);
        textViewDescription.setText(description);
        textViewOriginalAcnt.setText(account);
        textViewBalance.setText(balance);

        btnPay = (Button) findViewById(R.id.btnPayBill);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinnerPay);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Cash");
        categories.add("Tigo Cash");
        categories.add("Vodafone Cash");
        categories.add("MTN Mobile Money");
        categories.add("Airtel Money");
        categories.add("Visa Credit/Debit Card");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // On selecting a spinner item
                selectedtem = adapterView.getItemAtPosition(i).toString();

                switch (selectedtem){
                    case "Cash":
                        paytype = "cash";
                        break;
                    case "Tigo Cash":
                        paytype = "tigo-gh";
                        break;
                    case "Vodafone Cash":
                        paytype = "vodafone-gh";
                        break;
                    case "MTN Mobile Money":
                        paytype = "mtn-gh";
                        break;
                    case "Airtel Money":
                        paytype = "airtel-gh";
                        break;
                    case "Visa Credit/Debit Card":
                        paytype = "visa";
                        break;
                }

                // Showing selected spinner item
//                Toast.makeText(adapterView.getContext(), "Selected: " + selectedtem, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Sending post registration details to api
        apiServicePayBill = ApiUtils.getUserServicePayBill();

        //sending post to hubtel api
        apiServicePayBillMobileMoney = ApiUtils.getUserServicePayBillMobileMoney();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                progressDialog.show();
                progressDialog.show();
//                customProgressDialogOne.showCustomDialog(MakePaymentActivity.this,"Initialising Customer Payment",true);

                //amount to pay & button
                payAmount = (EditText) findViewById(R.id.editTestAmount);
                String PaymentAmount = payAmount.getText().toString().trim();


                if (!TextUtils.isEmpty(PaymentAmount)) {
                    Float paymentAmount = Float.parseFloat(PaymentAmount);
//                DecimalFormat paymentAmount = DecimalFormat.(PaymentAmount);

                    Log.i(TAG, "PayType " + paytype);

                    if (!TextUtils.isEmpty(paytype) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(billNumber)) {

                        if (paytype.equals("tigo-gh") || paytype.equals("vodafone-gh") || paytype.equals("mtn-gh") || paytype.equals("airtel-gh")) {

                            sentPayBillsDetailsMobileMoney(name, number, email, paytype, paymentAmount, PRIMARY_CALLBACK_URL, SECONDARY_CALLBACK_URL,
                                    description, clientRef, token, id, billNumber,
                                    agentName,name,id,billNumber,description,paymentAmount,balance);
                        } else if (paytype.equals("cash")) {
                            sentPayBillsDetails(id, paymentAmount, paytype, billNumber, agentName, name, id, description, balance);
                        } else if (paytype.equals("visa")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(MakePaymentActivity.this, "Visa Payments not yet implemented. ", Toast.LENGTH_SHORT).show();

                        }

                    }
                }else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(MakePaymentActivity.this, "Payment amount cannot be empty..", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void sentPayBillsDetails(String AccountNumber, final Float Amount, String Paytype, final String BillNumber,
                                     final String agentName, final String custName, final String custID, final String description,
                                     final String balance
                                     ) {

//        Toast.makeText(getApplicationContext(), Amount, Toast.LENGTH_SHORT).show();

        apiServicePayBill.savePostPayBill(AccountNumber,Amount,Paytype,BillNumber).enqueue(new Callback<PostPayBill>() {

            @Override
            public void onResponse(Call<PostPayBill> call, final Response<PostPayBill> response) {
                if (response.isSuccessful()){
                    Log.i(TAG, "post submitted to API => " + response.body().toString());

                    if ( progressDialog!=null && progressDialog.isShowing() ){
                        progressDialog.dismiss();
                    }

                    AlertDialog.Builder registrationAlert = new AlertDialog.Builder(MakePaymentActivity.this);
                    registrationAlert.setTitle(Html.fromHtml("<font color='#125688'>PAYMENT STATUS</font>"));
                    registrationAlert.setIcon(R.drawable.ic_check_box);
                    registrationAlert.setMessage("Payment Number: "+response.body().getPaymentNumber()+
                                                "\nPayment Amount: "+response.body().getPaymentAmount()+
                                                "\nPayment Date: "+response.body().getPaymentDate()+
                                                "\nMessage: "+response.body().getRespmessage());

//                    registrationAlert.setMessage("Payment Successfull");

                    registrationAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getContext(), "clicked ok", Toast.LENGTH_SHORT).show();

                            gotoRegistrationPage(custID);
                        }
                    });
                    registrationAlert.setNegativeButton("PRINT RECEIPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MakePaymentActivity.this, "Printing Issued..", Toast.LENGTH_SHORT).show();

                            printtest(agentName,custName,custID,BillNumber,description,Amount,response.body().getPaymentDate(),balance);
                        }
                    });
                    registrationAlert.setCancelable(false);

                    AlertDialog alertDialog = registrationAlert.create();
                    alertDialog.show();
                }else {
                    Log.e(TAG, "Error ==> " + response.errorBody().source());
                    Toast.makeText(MakePaymentActivity.this, response.errorBody().source().toString(), Toast.LENGTH_SHORT).show();
                    if ( progressDialog!=null && progressDialog.isShowing() ){
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<PostPayBill> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API => "+t.getCause());

                Toast.makeText(MakePaymentActivity.this, "Payment Request Failed, Please Try Again", Toast.LENGTH_SHORT).show();

                if ( progressDialog!=null && progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void sentPayBillsDetailsMobileMoney(String CustomerName, final String CustomerMsisdn, final String CustomerEmail, final String Channel,
                                                final Float Amount, final String PrimaryCallbackUrl, final String SecondaryCallbackUrl, final String Description,
                                                final String ClientReference, final String Token, final String AccountNumber, final String BillNumber,
                                                final String agentName,final String name,final String id,final String billNumber,final String description,final Float paymentAmount,final String balance){

            apiServicePayBillMobileMoney.savePostPayBillMobileMoney(CustomerName,CustomerMsisdn,CustomerEmail,Channel,Amount,PrimaryCallbackUrl,
                    SecondaryCallbackUrl,Description,ClientReference,Token,AccountNumber,BillNumber).enqueue(new Callback<PostPayBillMobileMoney>() {
                @Override
                public void onResponse(Call<PostPayBillMobileMoney> call, Response<PostPayBillMobileMoney> response) {
                    if (response.isSuccessful()){
                        Log.i(TAG, "post submitted to mobile money API." + response.body().toString());

                        if ( progressDialog!=null && progressDialog.isShowing() ){
                            progressDialog.dismiss();
                        }
                        customProgressDialogOne.dismissCustomDialog(MakePaymentActivity.this);

                        if (response.body().getRespCode().equals("0001")){

                            Intent intent = new Intent(MakePaymentActivity.this,ConfirmBillPayment.class);
                            intent.putExtra("TransactionId",response.body().getTransactionId());
                            intent.putExtra("ClientReference",response.body().getClientReference());
                            intent.putExtra("Description",response.body().getDescription());
                            intent.putExtra("Amount",response.body().getAmount());

                            intent.putExtra("Cust_Account",AccountNumber);
                            intent.putExtra("FullName",agentName);
                            intent.putExtra("name",name);
                            intent.putExtra("id",id);
                            intent.putExtra("billNumber",BillNumber);
                            intent.putExtra("description",description);
                            intent.putExtra("balance",balance);

                            startActivity(intent);
                        }else {
                            customProgressDialogOne.dismissCustomDialog(MakePaymentActivity.this);
                            Toast.makeText(MakePaymentActivity.this, "Transaction Failed. Please try Again...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<PostPayBillMobileMoney> call, Throwable t) {

                }

            });
    }

    @Override
    protected void onResume() {
        super.onResume();

        customProgressDialogOne.dismissCustomDialog(MakePaymentActivity.this);

    }

    private void sparkPrinter() {
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


    private void gotoRegistrationPage(String id) {
        Intent i = new Intent(MakePaymentActivity.this,ShowBillsActivity.class);
        //detector
        i.putExtra("detectorAfterPayment",true);

        i.putExtra("Cust_Account",id);
        startActivity(i);
        //finish();
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

    private void printtest(final String agentName, final String custName, final String custID, final String billNumber, final String description, final Float amount, final String date,final String balance) {
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

//    @Override
//    public void onDestroy() {
//        // power off the device when you do not need to read card or print for a
//        // long time
//        setting.mPosPowerOff();
//        // ondestroy the sdk when you exit the app
//        handler.onDestroy();
//        setting.onDestroy();
//        super.onDestroy();
//    }

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
                    + MakePaymentActivity.this.getResources().getText(
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
