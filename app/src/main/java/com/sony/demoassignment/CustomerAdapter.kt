package com.sony.sqlitedb

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sony.demoassignment.MainActivity
import com.sony.demoassignment.R
import kotlinx.android.synthetic.main.lo_customers.view.*


class CustomerAdapter(mCtx: Context, val customers: ArrayList<Customer>) :
    RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {


    val mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var txtCustomerName = itemView.txtCustomerName
        var txtdob = itemView.txtdob
        var imageView4 = itemView.imageView4

        val btnDelete = itemView.btnDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_customers, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: CustomerAdapter.ViewHolder, position: Int) {

        val customer: Customer = customers[position]
        holder.txtCustomerName.text = customer.customerName
        holder.txtdob.text = customer.dob.toString()

        Glide.with(mCtx)
            .load(customer.Image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageView4)

        holder.btnDelete.setOnClickListener {
            val customerName = customer.customerName
            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are You Sure to Delete : $customerName ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (MainActivity.dbHandler.deleteCustomer(customer.customerId)) {
                        customers.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, customers.size)
                        Toast.makeText(mCtx, "Name $customerName Deleted", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(mCtx, "Error Deleting", Toast.LENGTH_LONG).show()

                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i -> })
                .setIcon(R.drawable.ic_warning_black_24dp)
                .show()
        }

    }

}

