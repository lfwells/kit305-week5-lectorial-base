package au.edu.utas.lfwells.week5lectorial

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class ImageUtils {

    //you will use this function in the lectorial
    fun drawPixelAsImageView(parent: ViewGroup, x : Int, y : Int, pixel : Int, size: Int) : ImageView
    {
        //create an image view
        val imageView = ImageView(parent.context)
        //make it the colour of the pixel
        imageView.setBackgroundColor(pixel)
        //set the size
        imageView.layoutParams = ViewGroup.LayoutParams(size, size)
        //set the position
        imageView.x = x.toFloat() * size
        imageView.y = y.toFloat() * size
        //add it to the parent view
        parent.addView(imageView)

        //return the imageView
        return imageView
    }


    //you won't need the rest of the things in this file, but I'm leaving it here for the curious

    //this function also won't work for you, because you don't have the file, don't have the permissions, etc
    fun loadAndSetUpImage(db: FirebaseFirestore, context:Context)
    {
        val pathString = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val pixels = getPixelArrayFromImage(
            context,
            File(pathString, "rick.png")?.toUri()!!
        )
        Log.d("IMAGE", pixels?.size.toString())

        val imageCollection = db.collection("my_image")
        uploadPixelsAsDocuments(pixels!!, imageCollection)
    }

    fun getPixelArrayFromImage(context: Context, imageUri: Uri): IntArray? {
        try {
            // Open the input stream from the image URI
            val inputStream = context.contentResolver.openInputStream(imageUri)
            // Decode the input stream into a bitmap
            val bitmap = BitmapFactory.decodeStream(inputStream)
            // Get the width and height of the bitmap
            val width = bitmap.width
            val height = bitmap.height
            // Create an int array to store the pixel data
            val pixels = IntArray(width * height)
            // Get the pixel data from the bitmap
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            // Return the pixel array
            return pixels
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
        }
        // Return null if an exception occurred
        return null
    }

    //this function won't work for you, because I have disabled writes to my database
    //also this is NOT NOT NOT the way to upload an image to firebase, this is a terrible way to do it
    fun uploadPixelsAsDocuments(pixels:IntArray, imageCollection: CollectionReference)
    {
        val width = 552
        val height = 304
        val scale = 8

        val batchList = mutableListOf<List<Map<String, Int>>>();

        var currentBatch = mutableListOf<Map<String, Int>>();

        //draw each pixel as an imageView, scaled down by a factor of scale
        for (y in 0 until height / scale) {
            for (x in 0 until width / scale) {
                val pixel = pixels!![y * scale * width + x * scale]
                /*imageUtils.drawPixelAsImageView(
                    findViewById<ConstraintLayout>(R.id.root),
                    x,
                    y,
                    pixel,
                    scale
                )*/

                currentBatch.add(mapOf("x" to x, "y" to y, "pixel" to pixel))
                if (currentBatch.size >= 500)
                {
                    batchList.add(currentBatch)
                    currentBatch = mutableListOf<Map<String, Int>>()
                }

            }
        }

        //commit all batches
        var i = 0
        for (batch in batchList)
        {
            Log.d("FIREBASE", "batch $i")
            i++
            imageCollection.firestore.runBatch {
                for (data in batch)
                {
                    imageCollection.add(data)
                }
            }
        }

    }





}