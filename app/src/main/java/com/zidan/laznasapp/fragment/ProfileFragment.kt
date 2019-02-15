package com.zidan.laznasapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.zidan.laznasapp.MainPreferences
import com.zidan.laznasapp.R
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.ctx
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.LocalDataHandler
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.models.*
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var preferences: MainPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        preferences = MainPreferences(ctx)

        val userName = arguments?.getString("name")
        val userEmail = arguments?.getString("email")

        tv_username.text = userName
        tv_email.text = userEmail

        mAuth.signOut()
        Log.d("yes", "Log Out")
        preferences.setFirst(true,"", "")
        activity?.finish()
    }
}
