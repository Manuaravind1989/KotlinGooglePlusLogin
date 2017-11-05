package com.mobiledev.googleplus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

class MainActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private var txt_name: TextView? = null
    private var txt_mail: TextView? = null
    private var signInButton: SignInButton? = null
    private var custom_loginButton: Button? = null
    private var gso: GoogleSignInOptions? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt_name = findViewById<TextView>(R.id.txt_name)
        txt_mail = findViewById<TextView>(R.id.txt_mail)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        custom_loginButton = findViewById<Button>(R.id.custom_loginButton)
        custom_loginButton!!.setOnClickListener() {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton!!.setSize(SignInButton.SIZE_WIDE)
        signInButton!!.setScopes(gso!!.scopeArray)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this@MainActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                .build()

        signInButton!!.setOnClickListener(this)

    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }


    //After the signing we are calling this function
    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val acct = result.signInAccount
            txt_name!!.text = acct!!.displayName
            txt_mail!!.text = acct.email

        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View) {
        if (v === signInButton) {
            signIn()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }
}