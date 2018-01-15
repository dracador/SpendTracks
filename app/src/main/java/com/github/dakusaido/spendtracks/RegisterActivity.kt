package com.github.dakusaido.spendtracks

import android.os.AsyncTask

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    val Tag: String = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            RegisterRequest().execute()
        }
    }

    private fun makeRequest(username: String, email: String, password: String): String {
        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("password", password)

        val headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json"
        )


        Log.d(Tag, json.toString(4))
        //synchronous call
        val (request, response, result) =
                Fuel.post("http://10.0.2.2:8000/api/users/create")
                        .body(json.toString()).header(headers)
                        .responseString()

        Log.d(Tag, request.toString())
        Log.d(Tag, response.toString())
        Log.d(Tag, result.toString())
        return result.toString()
    }

    inner class RegisterRequest : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val username = usernameField.text
            val password = passwordField.text
            val email = "$username@testing.de"

            return makeRequest(username.toString(), email, password.toString())
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result == "") {
                toast("Network Error")
            } else {
                toast(result.toString())
            }
        }
    }

}
