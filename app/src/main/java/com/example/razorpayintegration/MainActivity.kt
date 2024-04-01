package com.example.razorpayintegration

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.razorpayintegration.util.Constants.Companion.RAZORPAY_API
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var amountEdt:EditText
    private lateinit var payNowBtn:Button
    private lateinit var paymentStatus:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        amountEdt=findViewById(R.id.amoutnEdt)
        payNowBtn=findViewById(R.id.payNowBtn)
        paymentStatus=findViewById(R.id.paymentStatus)

        payNowBtn.setOnClickListener{
            if(amountEdt.text.isNotEmpty() && amountEdt.text!=null){
                val amount:Int= amountEdt.text.toString().toInt()
                payAmount(amount)
            }
            else{
                Toast.makeText(this, "Please enter the amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun payAmount(amount: Int) {
        val checkout=Checkout()
        checkout.setKeyID(RAZORPAY_API)
        checkout.setImage(R.drawable.youtube)

        try {
            val options=JSONObject()
            options.put("name", "Youtube Premium")
            options.put("Description", "Please Pay the total amount for the Youtube Premium")
            options.put("Currency", "INR")
            options.put("amount", amount*100)
            options.put("theme", R.color.black)


            val retryObj= JSONObject()

            retryObj.put("enable", true)
            retryObj.put("max", 4)
            retryObj.put("retry", retryObj)

           checkout.open(this@MainActivity, options)
        }
        catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        paymentStatus.text= p0
        Toast.makeText(this, "Payment is success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment failed:$p1", Toast.LENGTH_SHORT).show()
    }
}