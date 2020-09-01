package com.sony.sqlitedb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHandler(
    context: Context,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {

        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1
        val CUSTOMERS_TABLE_NAME = "Customers"
        val COLUMN_CUSTOMER_ID = "customerid"
        val COLUMN_CUSTOMER_NAME = "customername"
        val COLUMN_DOB = "dob"
        val COLUMN_IMAGE = "image"

    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CUSTOMER_TABLE = ("CREATE TABLE $CUSTOMERS_TABLE_NAME(" +
                "$COLUMN_CUSTOMER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CUSTOMER_NAME TEXT," +
                "$COLUMN_DOB TEXT," +
                "$COLUMN_IMAGE TEXT)")
        db?.execSQL(CREATE_CUSTOMER_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun getCustomers(mCtx: Context): ArrayList<Customer> {
        val qry = "Select * from $CUSTOMERS_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)

        val customers = ArrayList<Customer>()


        if (cursor.count == 0)
            Toast.makeText(mCtx, "Records Not Found", Toast.LENGTH_LONG).show()
        else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()) {
                val customer = Customer()

                customer.customerId = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_ID))
                customer.customerName =
                    cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME))
                customer.dob = cursor.getString(cursor.getColumnIndex(COLUMN_DOB))
                customer.Image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))

                customers.add(customer)

                cursor.moveToNext()
            }

            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_LONG)
                .show()
        }
        cursor.close()
        db.close()
        return customers
    }

    fun addCustomer(mCtx: Context, customer: Customer) {
        val values = ContentValues()
        values.put(COLUMN_CUSTOMER_NAME, customer.customerName)
        values.put(COLUMN_DOB, customer.dob)
        values.put(COLUMN_IMAGE, customer.Image)


        val db = this.writableDatabase

        try {
            db.insert(CUSTOMERS_TABLE_NAME, null, values)
            Toast.makeText(mCtx, "Customer Added", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(mCtx, e.message, Toast.LENGTH_LONG).show()
        }
        db.close()
    }

    fun deleteCustomer(customerID: Int): Boolean {

        val qry = "Delete from $CUSTOMERS_TABLE_NAME where $COLUMN_CUSTOMER_ID=$customerID"
        val db = this.writableDatabase
        var result: Boolean = false

        try {
            val cursor = db.execSQL(qry)
            result = true

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }



}