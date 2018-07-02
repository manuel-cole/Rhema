package com.example.gamor.rhema;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamor.rhema.data.model.GetBills;
import com.example.gamor.rhema.remote.APIBillGetService;
import com.example.gamor.rhema.remote.ApiUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBillsActivity extends AppCompatActivity {

    APIBillGetService userService;
    List<GetBills> billsList;
    RecyclerView recyclerView;
    BillRecycleAdaptor billRecycleAdaptor;
    TextView namePlaceView,locationPlaceView,numberPlaceView,idPlaceView;
    private ProgressDialog progressDialog;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView,mBillTextView,mProgressLoaderText;
    private LinearLayout mShowBillData;
    private CardView mAndroidCardViewBillDetails;
    private ConstraintLayout mBillListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bills);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loader);
        mProgressLoaderText = (TextView) findViewById(R.id.pb_loaderText);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mShowBillData = (LinearLayout) findViewById(R.id.showBillsActivity);
        mBillTextView = (TextView) findViewById(R.id.textViewBill);
        mAndroidCardViewBillDetails = (CardView) findViewById(R.id.android_card_view_example);
        mBillListLayout = (ConstraintLayout) findViewById(R.id.billListLayout);

        BackgroundOperation bg = new BackgroundOperation();
        bg.execute();

    }

    class BackgroundOperation extends AsyncTask<String, Void, String> {

        Bundle extras = getIntent().getExtras();

        String CustID = extras.getString("Cust_Account");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = new ProgressDialog(ShowBillsActivity.this);
//            progressDialog.setMessage("Loading..."); // Setting Message
//            progressDialog.setTitle("Getting Customer Biils"); // Setting Title
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressLoaderText.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            userService = ApiUtils.getUserServiceBills();

            billsList = new ArrayList<>();
            Call<List<GetBills>> call = userService.gettingBiils(CustID);
//            Call<List<GetBills>> call = userService.gettingBiils1();

            call.enqueue(new Callback<List<GetBills>>() {
                @Override
                public void onResponse(Call<List<GetBills>> call, Response<List<GetBills>> response) {
                    if (response.isSuccessful()) {
                        billsList = response.body();

                        Log.d("OneFragment", billsList.toString());

                        if (billsList.get(0).getRespCode().equals("0")){

                            namePlaceView = (TextView) findViewById(R.id.namePlaceView);
                            locationPlaceView = (TextView) findViewById(R.id.locationPlaceView);
                            numberPlaceView = (TextView) findViewById(R.id.numberPlaceView);
                            idPlaceView = (TextView) findViewById(R.id.idPlaceView);

                            namePlaceView.setText(billsList.get(0).getCustName());
                            locationPlaceView.setText(billsList.get(0).getLocation());
                            numberPlaceView.setText(billsList.get(0).getPhone());
                            idPlaceView.setText(billsList.get(0).getCustAccount());

                            recyclerView = (RecyclerView) findViewById(R.id.billList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);

                            billRecycleAdaptor = new BillRecycleAdaptor(getApplicationContext(),billsList);
                            recyclerView.setAdapter(billRecycleAdaptor);

                            if (mProgressBar!=null && mProgressBar.isShown()){
                                mProgressBar.setVisibility(View.GONE);
                                mProgressLoaderText.setVisibility(View.GONE);
//                                Toast.makeText(ShowBillsActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
                            }
                            showBillDetails();

                            if ( progressDialog!=null && progressDialog.isShowing() ){
                                progressDialog.dismiss();
                            }

                            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                                            recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override public void onItemClick(View view, int position) {
                                            // do whatever
                                            Toast.makeText(ShowBillsActivity.this, billsList.get(position).getBillNumber(), Toast.LENGTH_SHORT).show();

                                            String name = billsList.get(0).getCustName();
                                            String location = billsList.get(0).getLocation();
                                            String number = billsList.get(0).getPhone();
                                            String id = billsList.get(0).getCustAccount();

                                            //Chosen Bill
                                            String billNumber = billsList.get(position).getBillNumber();
                                            String description = billsList.get(position).getDescription();
                                            String account = billsList.get(position).getOriginalAmount();
                                            String balance = billsList.get(position).getBalance();

                                            DecimalFormat df = new DecimalFormat(".###");
                                            Double accountFormatted = Double.parseDouble(account);
                                            Double balanceFormatted = Double.parseDouble(balance);

                                            //Start payment activity
                                            Intent intent = new Intent(ShowBillsActivity.this,MakePaymentActivity.class);
                                            intent.putExtra("name",name);
                                            intent.putExtra("location",location);
                                            intent.putExtra("number",number);
                                            intent.putExtra("id",id);
                                            intent.putExtra("billNumber",billNumber);
                                            intent.putExtra("description",description);
                                            intent.putExtra("account",df.format(accountFormatted));
                                            intent.putExtra("balance",df.format(balanceFormatted));

                                            startActivity(intent);

                                        }

                                        @Override public void onLongItemClick(View view, int position) {
                                            // do whatever
                                        }
                                    })
                            );

                        }else if (billsList.get(0).getRespCode().equals("1000")){
                            if ( progressDialog!=null && progressDialog.isShowing() ){
                                progressDialog.dismiss();
                            }

                            AlertDialog.Builder registrationAlert = new AlertDialog.Builder(ShowBillsActivity.this);
                            registrationAlert.setTitle(Html.fromHtml("<font color='#125688'>CUSTOMER SEARCH</font>"));
                            registrationAlert.setIcon(R.drawable.ic_alert_registration);
                            registrationAlert.setMessage("Customer with ID: "+CustID+" have no biils");
                            registrationAlert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getContext(), "clicked ok", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ShowBillsActivity.this,HomeActivity.class);
                                    intent.putExtra("detectorShowbills",true);
                                    startActivity(intent);

                                }
                            });
                            registrationAlert.setCancelable(false);

                            AlertDialog alertDialog = registrationAlert.create();
                            alertDialog.show();
//                            Toast.makeText(getContext(), "No bill", Toast.LENGTH_SHORT).show();
                        }else {
                            if ( progressDialog!=null && progressDialog.isShowing() ){
                                progressDialog.dismiss();
                            }
                            AlertDialog.Builder registrationAlert = new AlertDialog.Builder(ShowBillsActivity.this);
                            registrationAlert.setTitle(Html.fromHtml("<font color='#125688'>CUSTOMER SEARCH</font>"));
                            registrationAlert.setIcon(R.drawable.ic_alert_registration);
                            registrationAlert.setMessage("Customer with ID: "+CustID+" is not registered");
                            registrationAlert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getContext(), "clicked ok", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ShowBillsActivity.this,HomeActivity.class);
                                    intent.putExtra("detectorShowbills",true);
                                    startActivity(intent);
                                }
                            });
                            registrationAlert.setCancelable(false);

                            AlertDialog alertDialog = registrationAlert.create();
                            alertDialog.show();
                        }

                    }else {
                        Toast.makeText(ShowBillsActivity.this, "Error: "+ response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GetBills>> call, Throwable t) {
                    Log.d("OneFragment",billsList.toString());
//                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    if (mProgressBar!=null && mProgressBar.isShown()){
                       mProgressBar.setVisibility(View.GONE);
                       mProgressLoaderText.setVisibility(View.GONE);
                    }
                    showErrorMrssage();
                }
            });



            return billsList.toString();
        }

        @Override
        protected void onPostExecute(String result) {
//        mProgressBar.setVisibility(View.GONE);
        if (null == result){
            Toast.makeText(ShowBillsActivity.this, "New Error", Toast.LENGTH_SHORT).show();
//            showErrorMrssage();
        }else {
//            showBillDetails();
        }
//        Toast.makeText(getApplicationContext(), billsList.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if ( progressDialog!=null && progressDialog.isShowing() ){
                progressDialog.dismiss();
            }
//            mProgressBar.setVisibility(View.GONE);
//            Toast.makeText(ShowBillsActivity.this, "New Error", Toast.LENGTH_SHORT).show();

            if (null == values){
                Toast.makeText(ShowBillsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                showErrorMrssage();
            }else {
//                showBillDetails();
            }

        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            SharedPreferences preferences =getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(ShowBillsActivity.this,MainActivity.class));
            Toast.makeText(this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showBillDetails(){
//        mProgressBar.setVisibility(View.GONE);

        mShowBillData.setVisibility(View.VISIBLE);
        mBillListLayout.setVisibility(View.VISIBLE);

        mAndroidCardViewBillDetails.setVisibility(View.VISIBLE);

        mBillTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMrssage(){
//        mShowBillData.setVisibility(View.GONE);
//        mProgressBar.setVisibility(View.GONE);

        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private class CustID {

    }
}
