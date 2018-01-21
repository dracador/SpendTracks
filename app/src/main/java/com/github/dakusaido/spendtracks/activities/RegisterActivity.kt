package com.github.dakusaido.spendtracks.activities

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.dakusaido.spendtracks.R
import com.github.dakusaido.spendtracks.utilities.REGISTER_URL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject



class RegisterActivity : AppCompatActivity() {

    private val tag: String = "RegisterActivity"
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            RegisterRequest().execute(buildJSON())
        }
    }

    private fun buildJSON(): JSONObject {
        val username = usernameField.text
        val password = passwordField.text
        val email = emailField.text

        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("password", password)

        return json
    }

    inner class RegisterRequest : AsyncTask<JSONObject, Void, Response>() {
        override fun onPreExecute() {
            super.onPreExecute()

            dialog = indeterminateProgressDialog(message="This might take a few seconds", title = "Register")
            dialog!!.setCancelable(false)
            dialog!!.show()
        }

        override fun doInBackground(vararg jsonBody: JSONObject): Response {
            val headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json"
            )

            Log.d(tag, "Starting request...")

            val (_, response, _) =
                Fuel.post(REGISTER_URL)
                    .body(jsonBody.toString())
                    .header(headers)
                    .timeout(5 * 1000) // milliseconds
                    .responseString()

            Thread.sleep(1000)  // simulate slow connection

            return response
        }

        override fun onPostExecute(response: Response) {
            super.onPostExecute(response)

            alertText.text = response.statusCode.toString()
            dialog!!.dismiss()
            TODO("Handle statusCodes")
        }
    }
}
