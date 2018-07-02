package com.example.gamor.rhema;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamor.rhema.data.model.PostPayBillMobileMoneyFinalised;
import com.example.gamor.rhema.remote.APIServicePayBillMobileMoneyFinalised;
import com.example.gamor.rhema.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ConfirmBillPayment extends AppCompatActivity {

    Button btnConfrim,btnCancel;
    private APIServicePayBillMobileMoneyFinalised apiServicePayBillMobileMoneyFinalised;
    private AlertDialog progressDialog;
    private String CustID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bill_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View dialogView = LayoutInflater.from(ConfirmBillPayment.this).inflate(R.layout.custom_progress_dialog_one, null);
        progressDialog = new android.app.AlertDialog.Builder(ConfirmBillPayment.this).create();
        progressDialog.setTitle("Initialising Customer Payment"); // Setting Title
        progressDialog.setCancelable(false);
        progressDialog.setView(dialogView);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();

        btnConfrim = (Button) findViewById(R.id.btn_mobile_money_payment_confirm);
        btnCancel = (Button) findViewById(R.id.btn_mobile_money_payment_cancel);

        Bundle extras = getIntent().getExtras();

        final String TransactionId = extras.getString("TransactionId");
        String ClientReference = extras.getString("ClientReference");
        String Description = extras.getString("Description");
        final String Amount = extras.getString("Amount");

        CustID = extras.getString("Cust_Account");
        final String agentName = extras.getString("FullName").trim();
        final String name = extras.getString("name").trim();
        final String id = extras.getString("id").trim();
        final String billNumber = extras.getString("billNumber").trim();
        final String description = extras.getString("description").trim();
        final String balance = extras.getString("balance").trim();

        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText(
                Description+"\n"+
                "Amount: GHC "+Amount);

        btnConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(TransactionId)){
                    progressDialog.show();
                    sendFinalizedPayment(TransactionId,CustID,agentName,name,id,billNumber,description,Amount,balance);
                }
            }
        });

    }

    private void sendFinalizedPayment(String transactionId, final String id,final String agentName, final String custName, final String custID, final String billNumber, final String description, final String amount,final String balance) {
        Log.e(TAG, "Transaction ID ==> "+transactionId);
        apiServicePayBillMobileMoneyFinalised = ApiUtils.getUserServicePayBillMobileMoneyFinalised();

        apiServicePayBillMobileMoneyFinalised.savePostPayBillMobileMoneyFinalised(transactionId).enqueue(new Callback<PostPayBillMobileMoneyFinalised>() {
            @Override
            public void onResponse(Call<PostPayBillMobileMoneyFinalised> call, final Response<PostPayBillMobileMoneyFinalised> response) {
                if (response.isSuccessful()){
                    if ( progressDialog!=null && progressDialog.isShowing() ){
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder registrationAlert = new AlertDialog.Builder(ConfirmBillPayment.this);
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
                        //todo start next page after user clicks on ok
                            gotoShowBillsPage(id);
                        }
                    });
                    registrationAlert.setNegativeButton("PRINT RECEIPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(MakePaymentActivity.this, "Printing "+agentName, Toast.LENGTH_SHORT).show();

                            //TODO call printer method here
                            Printer printer = new Printer();
                            printer.sparkPrinter();
                            printer.printtest(agentName,custName,custID,billNumber,description,Float.valueOf(amount),response.body().getPaymentDate(),balance);
                        }
                    });
                    registrationAlert.setCancelable(false);

                    AlertDialog alertDialog = registrationAlert.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<PostPayBillMobileMoneyFinalised> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API."+t.getCause());

                Toast.makeText(ConfirmBillPayment.this, "Payment Request Failed, Please Try Again", Toast.LENGTH_SHORT).show();

                if ( progressDialog!=null && progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void gotoShowBillsPage(String id) {
        Intent i = new Intent(ConfirmBillPayment.this,ShowBillsActivity.class);
        //detector
        i.putExtra("detectorAfterPayment",true);

        i.putExtra("Cust_Account",id);
        startActivity(i);
        //finish();
    }
}
