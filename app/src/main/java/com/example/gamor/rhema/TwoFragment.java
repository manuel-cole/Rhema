package com.example.gamor.rhema;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gamor.rhema.data.model.PostRegistration;
import com.example.gamor.rhema.remote.APIServiceRegister;
import com.example.gamor.rhema.remote.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class TwoFragment extends Fragment {

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private EditText inputSurname,inputFirstname,inputOthername,inputAddress,inputBirthday,
            inputLicenseNo,inputIssueDate,inputExpiryDate,inputRenewalDate,inputLicenseClass,
            inputEmail,inputRegistrationCenter,inputNationality,inputPhone,inputCustomerPicture;

    private Button btnRegister;
    private APIServiceRegister apiServiceRegister;
//    private TextView mResponseTv;
    private String myBirthDate,myIssueDate,myExpiryDate,myRenewalDate;
    android.app.AlertDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_progress_dialog_one, null);
        progressDialog = new android.app.AlertDialog.Builder(getContext()).create();
        progressDialog.setTitle("Registering User.."); // Setting Title
        progressDialog.setCancelable(false);
        progressDialog.setView(dialogView);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();
//            AlertDialog b = progressDialog.create();


        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar1 = Calendar.getInstance();
        final Calendar myCalendar2 = Calendar.getInstance();
        final Calendar myCalendar3 = Calendar.getInstance();

        final EditText editText = (EditText) view.findViewById(R.id.inputBirthday);
        final EditText editText1 = (EditText) view.findViewById(R.id.inputIssueDate);
        final EditText editText2 = (EditText) view.findViewById(R.id.inputExpiryDate);
        final EditText editText3 = (EditText) view.findViewById(R.id.inputRenewalDate);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
//                updateLabel();

                String myFormat = "yyyy-MM-dd"; //In which you need put here

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editText.setText(sdf.format(myCalendar.getTime()));

                myBirthDate = sdf.format(myCalendar.getTime());
            }
        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar1.set(Calendar.YEAR, i);
                myCalendar1.set(Calendar.MONTH, i1);
                myCalendar1.set(Calendar.DAY_OF_MONTH, i2);
//                updateLabel();

                String myFormat1 = "yyyy-MM-dd"; //In which you need put here

                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

                editText1.setText(sdf1.format(myCalendar1.getTime()));

                myIssueDate = sdf1.format(myCalendar1.getTime());
            }
        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar2.set(Calendar.YEAR, i);
                myCalendar2.set(Calendar.MONTH, i1);
                myCalendar2.set(Calendar.DAY_OF_MONTH, i2);
//                updateLabel();

                String myFormat2 = "yyyy-MM-dd"; //In which you need put here

                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);

                editText2.setText(sdf2.format(myCalendar2.getTime()));

                myExpiryDate = sdf2.format(myCalendar2.getTime());
            }
        };
        final DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar3.set(Calendar.YEAR, i);
                myCalendar3.set(Calendar.MONTH, i1);
                myCalendar3.set(Calendar.DAY_OF_MONTH, i2);
//                updateLabel();

                String myFormat3 = "yyyy-MM-dd"; //In which you need put here

                SimpleDateFormat sdf3 = new SimpleDateFormat(myFormat3, Locale.US);

                editText3.setText(sdf3.format(myCalendar3.getTime()));

                myRenewalDate = sdf3.format(myCalendar3.getTime());
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editText1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date1,
                        myCalendar1.get(Calendar.YEAR),
                        myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date2,
                        myCalendar2.get(Calendar.YEAR),
                        myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editText3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date3,
                        myCalendar3.get(Calendar.YEAR),
                        myCalendar3.get(Calendar.MONTH),
                        myCalendar3.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //getting inputs
        inputSurname = (EditText) view.findViewById(R.id.inputSurname);
        inputFirstname = (EditText) view.findViewById(R.id.inputFirstname);
        inputOthername = (EditText) view.findViewById(R.id.inputOthername);
        inputAddress = (EditText) view.findViewById(R.id.inputAddress);
        inputLicenseNo = (EditText) view.findViewById(R.id.inputLicenseNo);
        inputLicenseClass = (EditText) view.findViewById(R.id.inputLicenseClass);
        inputEmail = (EditText) view.findViewById(R.id.inputEmail);
        inputRegistrationCenter = (EditText) view.findViewById(R.id.inputRegistrationCenter);
        inputNationality = (EditText) view.findViewById(R.id.inputNationality);
        inputPhone = (EditText) view.findViewById(R.id.inputPhone);
        inputCustomerPicture = (EditText) view.findViewById(R.id.inputCustomerPicture);

        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        // Sending post registration details to api
        apiServiceRegister = ApiUtils.getUserServiceRegister();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                //Cast input to string
                String Surname = inputSurname.getText().toString().trim();
                String Firstname = inputFirstname.getText().toString().trim();
                String OtherName = inputOthername.getText().toString().trim();
                String Address = inputAddress.getText().toString().trim();
                String DateOfBirth = myBirthDate.toString().trim();
                String LicenseNo = inputLicenseNo.getText().toString().trim();
                String DateOfIssue = myIssueDate.toString().trim();
                String DateOfExpiry = myExpiryDate.toString().trim();
                String DateOfRenewal = myRenewalDate.toString().trim();
                String LicenseClass = inputLicenseClass.getText().toString().trim();
                String Email = inputEmail.getText().toString().trim();
                String RegistrationCenter = inputRegistrationCenter.getText().toString().trim();
                String Nationality = inputNationality.getText().toString().trim();
                String Phone = inputPhone.getText().toString().trim();
                String CustomerPicture = inputCustomerPicture.getText().toString().trim();

                if (!TextUtils.isEmpty(Firstname) && !TextUtils.isEmpty(Surname) && !TextUtils.isEmpty(LicenseNo) && !TextUtils.isEmpty(Phone)){

                    sentRegisterDetails(Surname,Firstname,OtherName,Address,DateOfBirth,LicenseNo,
                            DateOfIssue,DateOfExpiry,DateOfRenewal,LicenseClass,Email,RegistrationCenter,Nationality,Phone,CustomerPicture);

                }
            }
        });


        return view;
    }

    private void sentRegisterDetails(String surname, String firstname, String otherName,
                                     String address, String dateOfBirth, String licenseNo,
                                     String dateOfIssue, String dateOfExpiry, String dateOfRenewal,
                                     String licenseClass, String email, String registrationCenter,
                                     String nationality, String phone, String customerPicture) {

        apiServiceRegister.savePostRegister(surname, firstname, otherName, address, dateOfBirth,
                licenseNo, dateOfIssue, dateOfExpiry, dateOfRenewal, licenseClass,
                email, registrationCenter, nationality, phone, customerPicture).enqueue(new Callback<PostRegistration>() {
            @Override
            public void onResponse(Call<PostRegistration> call, Response<PostRegistration> response) {

                if(response.isSuccessful()) {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
//                    showResponse(response.body().toString());
//                    Toast.makeText(getContext(), "post submitted to API."+ response.body().toString(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
//                    Toast.makeText(getContext(),  response.body().getRespMessage(), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder registrationAlert = new AlertDialog.Builder(getContext());
                    registrationAlert.setTitle(Html.fromHtml("<font color='#125688'>REGISTRATION STATUS</font>"));
                    registrationAlert.setIcon(R.drawable.ic_alert_registration);
                    registrationAlert.setMessage("Message: "+response.body().getRespMessage());
                    registrationAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getContext(), "clicked ok", Toast.LENGTH_SHORT).show();

                            gotoRegistrationPage();
                        }
                    });
                    registrationAlert.setCancelable(false);

                    AlertDialog alertDialog = registrationAlert.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(getContext(), response.errorBody().source().toString(), Toast.LENGTH_LONG).show();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostRegistration> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void gotoRegistrationPage() {
            Intent i = new Intent(getContext(),HomeActivity.class);
            //detector
            i.putExtra("detector",true);
            startActivity(i);
            //finish();

    }

//    public void showResponse(String response) {
//        if(mResponseTv.getVisibility() == View.GONE) {
//            mResponseTv.setVisibility(View.VISIBLE);
//        }
//        mResponseTv.setText(response);
//
//    }

    }

