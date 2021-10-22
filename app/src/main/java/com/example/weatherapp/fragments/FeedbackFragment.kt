package com.example.weatherapp.fragments
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFeedbackBinding
import com.example.weatherapp.dataclass.Feedback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_feedback.*


class FeedbackFragment : Fragment() {
    private lateinit var ratingBar : RatingBar
    private lateinit var database: DatabaseReference

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
        ratingBar.rating = 2.5f
        ratingBar.stepSize = .5f

//        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//            Toast.makeText(context, "Rating = $rating", Toast.LENGTH_SHORT).show()
//        }


        //Setting firebase database
        database = FirebaseDatabase.getInstance().reference
        send.setOnClickListener {

            var name = name.text.toString()
            var email = email.text.toString()
            var msg = message.text.toString()

            database.child(name.toString()).setValue(Feedback(email,msg))
            Toast.makeText(context, "Response Submitted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_feedbackFragment_to_homeFragment)

        }

    }


}