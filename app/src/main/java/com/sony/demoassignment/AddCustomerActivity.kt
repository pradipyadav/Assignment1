package com.sony.sqlitedb

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sony.demoassignment.MainActivity
import com.sony.demoassignment.R
import kotlinx.android.synthetic.main.activity_add_customer.*
import java.util.*


class AddCustomerActivity : AppCompatActivity() {

    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
   //Date Variable
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    // Gallery Image Picker Code
    companion object {
        //image pick code

        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //User Upload Image URI
    var inputData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

        selectData.setOnClickListener {
            val dpd = DatePickerDialog(
                this,

                DatePickerDialog.OnDateSetListener {
                        view,
                        year,
                        monthOfYear,
                        dayOfMonth ->
                    var mnt = monthOfYear + 1
                    selectData.setText("" + dayOfMonth + "-" + mnt + "-" + year)

                },
                year,
                month,
                day
            )
            dpd.getDatePicker().setMaxDate(Date().getTime())
            dpd.show()
        }


        uploadI.setOnClickListener {
            pickImageFromGallery();
        }


        btnAdd.setOnClickListener {

            if (edtCustomerName.text.isEmpty()) {
                Toast.makeText(this, "Enter Your Name", Toast.LENGTH_LONG).show()
                edtCustomerName.requestFocus()
            }
            else if (selectData.text.equals("Select Date")) {
                Toast.makeText(this, "Select Date", Toast.LENGTH_LONG).show()
            }
            else if (inputData.isEmpty()) {
                Toast.makeText(this, "Select Your Image", Toast.LENGTH_LONG).show()
            }
            else {
                val customer = Customer()
                customer.customerName = edtCustomerName.text.toString()

                customer.dob = selectData.text.toString()
                customer.Image = inputData

                MainActivity.dbHandler.addCustomer(this, customer)
                clearEdits()
                edtCustomerName.requestFocus()
                finish()
            }

        }


    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data?.data)

            //val iStream: InputStream? = data?.data?.let { contentResolver.openInputStream(it) }
            inputData = data?.data.toString()

            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()

        }
    }


    private fun pickImageFromGallery() {

        //Above M Version
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun clearEdits() {
        edtCustomerName.text.clear()
    }

    override fun onResume() {
        super.onResume()
        getPermission();
    }


    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun getPermission() {
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

}