package com.zidan.laznasapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import android.util.Log
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.LocalDataHandler
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BillInfoModel
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.UserAddress
import com.midtrans.sdk.corekit.models.UserDetail
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class LoginActivity : AppCompatActivity(), TransactionFinishedCallback {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFire: FirebaseFirestore
    private lateinit var preferences: MainPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = MainPreferences(this)
        val isFirst = preferences.getFirst()
        val userName = preferences.getName()
        val userEmail = preferences.getEmail()

        if (!isFirst){ startActivity<MainActivity>("name" to userName, "email" to userEmail) }

        mAuth = FirebaseAuth.getInstance()
        mFire = FirebaseFirestore.getInstance()

        tv_forgot.setOnClickListener {

            val email = edt_email.text.toString().trim()
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("yes", "Email sent.")
                    }
                }
        }

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-muyh_hSRaz5d2mGo") // client_key is mandatory
            .setContext(ctx) // context is mandatory
            .setTransactionFinishedCallback {
                // Handle finished transaction here.
            } // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("http://salamcorner.id/midtrans/checkout.php/") //set merchant url (required) BASE_URL
            .enableLog(true) // enable sdk log (optional)
            .buildSDK()

        var userDetail: UserDetail? = LocalDataHandler.readObject("user_details", UserDetail::class.java)
        if (userDetail == null) {
            userDetail = UserDetail()
            userDetail.userFullName = "B. Erfransyah Levi Darmawan"
            userDetail.email = "franslevi008@gmail.com"
            userDetail.phoneNumber = "08123456789"

            val userAddresses = arrayListOf<UserAddress>()
            val userAddress = UserAddress()
            userAddress.address = "Jl Slipi No 15"
            userAddress.city = "Jakarta"
            userAddress.addressType = com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH
            userAddress.zipcode = "40184"
            userAddress.country = "IDN"
            userAddresses.add(userAddress)
            userDetail.userAddresses = userAddresses
            LocalDataHandler.saveObject("user_details", userDetail)
        }

        val transactionRequest = TransactionRequest(System.currentTimeMillis().toString() + "", 50000.toDouble())
        val itemDetails1 = ItemDetails("1", 10000.toDouble(), 2, "Sandal")
        val itemDetails2 = ItemDetails("2", 30000.toDouble(), 1, "Gelas")

        // Create array list and add above item details in it and then set it to transaction request.
        val itemDetailsList = arrayListOf<ItemDetails>()
        itemDetailsList.add(itemDetails1)
        itemDetailsList.add(itemDetails2)

        // Set item details into the transaction request.
        transactionRequest.itemDetails = itemDetailsList

        val billInfoModel = BillInfoModel("demo_label", "demo_value")
        transactionRequest.billInfoModel = billInfoModel

        MidtransSDK.getInstance().transactionRequest = transactionRequest

        btn_register.setOnClickListener {
            MidtransSDK.getInstance().startPaymentUiFlow(this@LoginActivity)
        }

        btn_login.setOnClickListener { setLogin() }
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> toast(
                    "Transaction Finished. ID: " + result.response.transactionId
                ).show()
                TransactionResult.STATUS_PENDING -> toast(
                    "Transaction Pending. ID: " + result.response.transactionId
                ).show()
                TransactionResult.STATUS_FAILED -> toast(
                    "Transaction Failed. ID: " + result.response.transactionId + ". Message: " + result.response.statusMessage
                ).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            toast("Transaction Canceled").show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, ignoreCase = true)) {
                toast("Transaction Invalid").show()
            } else {
                toast("Transaction Finished with failure.").show()
            }
        }
    }

    private fun setLogin() {
        val email =  edt_email.text.toString().trim()
        val password = edt_password.text.toString().trim()

        val isEmail = isEmailValid(email)

        if (isEmail) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mFire.collection("User")
                            .whereEqualTo("email", email)
                            .get().addOnCompleteListener { query ->
                                if (query.isSuccessful) {
                                    for (document in query.result!!) {
                                        val name = document.data["name"].toString()
                                        if (cb_remember_me.isChecked) {
                                            preferences.setFirst(false, name, email)
                                        }
                                        startActivity<MainActivity>("name" to name, "email" to email)
                                    }
                                }
                            }
                    }
                    else toast(task.exception.toString()).show()
                }
        } else {
            edt_email.error = "Email Invalid"
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
