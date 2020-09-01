package com.sony.demoassignment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sony.sqlitedb.AddCustomerActivity
import com.sony.sqlitedb.Customer
import com.sony.sqlitedb.CustomerAdapter
import com.sony.sqlitedb.DBHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }

    var customerList = ArrayList<Customer>()
    lateinit var adapter: RecyclerView.Adapter<*>
    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)
        viewCustomers()

        fab.setOnClickListener {
            val i = Intent(this, AddCustomerActivity::class.java)
            startActivity(i)

        }

        editSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var filteredList = ArrayList<Customer>()
                    if (!editSearch.text.isEmpty()) {
                        for (i in 0..customerList.size - 1) {
                            if (customerList.get(i).customerName!!.toLowerCase()
                                    .contains(p0.toString().toLowerCase())
                            )
                                filteredList.add(customerList[i])
                        }
                        adapter = CustomerAdapter(this@MainActivity, filteredList)
                        rv.adapter = adapter
                    } else {
                        adapter = CustomerAdapter(this@MainActivity, customerList)
                        rv.adapter = adapter
                    }


                }

            }
        )
    }

    private fun viewCustomers() {

        try {
            customerList = dbHandler.getCustomers(this)
            adapter = CustomerAdapter(this, customerList)
            rv = findViewById(R.id.rv)

            rv.layoutManager =
                LinearLayoutManager(
                    this,
                    LinearLayout.VERTICAL,
                    false
                ) as RecyclerView.LayoutManager

            rv.adapter = adapter
        } catch (E: Exception) {
            E.printStackTrace()
        }
    }

    override fun onResume() {
        viewCustomers()
        super.onResume()
    }
}