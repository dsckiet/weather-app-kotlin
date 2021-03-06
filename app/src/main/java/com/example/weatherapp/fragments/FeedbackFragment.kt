package com.example.weatherapp.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.dataClass.Feedback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_feedback.*
import java.util.regex.Pattern


class FeedbackFragment : Fragment() {
    private lateinit var ratingBar : RatingBar
    private lateinit var database: DatabaseReference
    private var validate: Boolean = true
    private val NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        ratingBar = view.findViewById(R.id.ratingBar)
        ratingBar.rating = 3f
        ratingBar.stepSize = .5f
//        nav_view.visibility = View.GONE

        backbtn1.setOnClickListener {
            findNavController().navigate(R.id.action_feedbackFragment_to_homeFragment)
        }

        database = FirebaseDatabase.getInstance().reference

        send.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val msg = messageInput.text.toString()
            if (name.isEmpty()) {
                nameInput.error = "Please enter a name"
                nameInput.requestFocus()
                validate = false
            }else {
                if (TextUtils.isEmpty(email)){
                    emailInput.error = "Email is Mandatory"
                    emailInput.requestFocus()
                    validate = false
                }
                else{
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        validate = false
                        emailInput.error = "Please enter valid email"
                        emailInput.requestFocus()
                    }
                }
            }
            if (validate) {
                database.child(name).setValue(Feedback(email, msg , ratingBar.rating.toString() ))

                        Toast.makeText(context, "Response Submitted", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_feedbackFragment_to_homeFragment)


            }

        }


    }
    fun onSuccess() {

        findNavController().popBackStack()
    }


}