package au.edu.utas.lfwells.week5lectorial

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //root ui object, going to use for displaying image views
        val root = findViewById<ConstraintLayout>(R.id.root)

        //get the image utils object which can draw image views for us
        val imageUtils = ImageUtils()

        //get db connection
        val db = Firebase.firestore
        Log.d("FIREBASE", "Firebase connected: ${Firebase.app}")

        //we will write code in here
    }
}