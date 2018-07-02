package com.example.gamor.rhema;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamor.rhema.data.model.GetBills;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BillRecycleAdaptor extends RecyclerView.Adapter<BillRecycleAdaptor.MyviewHolder> {

    private Context context;
    private List<GetBills> getBillsList;

    public BillRecycleAdaptor(Context context, List<GetBills> getBillsList) {
        this.context = context;
        this.getBillsList = getBillsList;
    }

    @Override
    public BillRecycleAdaptor.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bill_recycle_adaptor,parent,false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillRecycleAdaptor.MyviewHolder holder, int position) {

        String oriAmount = getBillsList.get(position).getOriginalAmount();
        String oriBalance = getBillsList.get(position).getBalance();

//        DecimalFormat df = new DecimalFormat(".##");
        Double accountFormatted = round(Double.parseDouble(oriAmount),2);
        Double balanceFormatted = round(Double.parseDouble(oriBalance),2);

        holder.textViewBillNo.setText(getBillsList.get(position).getBillNumber());
        holder.textViewDescription.setText(getBillsList.get(position).getDescription());
        holder.textViewOriginalAcnt.setText(accountFormatted.toString());
        holder.textViewBalance.setText(balanceFormatted.toString());

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int getItemCount() {
        return getBillsList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView textViewBillNo,textViewDescription,textViewOriginalAcnt,textViewBalance;
        public MyviewHolder(View itemView) {
            super(itemView);

            textViewBillNo = (TextView) itemView.findViewById(R.id.textViewBillNo);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewOriginalAcnt = (TextView) itemView.findViewById(R.id.textViewOriginalAcnt);
            textViewBalance = (TextView) itemView.findViewById(R.id.textViewBalance);


        }
    }
}
