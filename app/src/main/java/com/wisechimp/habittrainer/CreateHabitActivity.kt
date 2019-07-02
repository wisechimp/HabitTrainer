package com.wisechimp.habittrainer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.wisechimp.habittrainer.dataBase.HabitDatabaseTable
import com.wisechimp.habittrainer.recyclerView.Habit
import kotlinx.android.synthetic.main.activity_create_habit.*
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {

    private val TAG = CreateHabitActivity::class.java.simpleName
    private val CHOOSE_IMAGE_REQUEST = 1
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habit)
    }

    fun chooseImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val imageChooser = Intent.createChooser(intent, "Choose image for habit")
        startActivity(imageChooser)
        Log.d(TAG, "Intent to choose image sent.")
        startActivityForResult(imageChooser, CHOOSE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null) {
            Log.d(TAG, "An image was chosen by the user")
            val chosenImageBitmap = tryReadBitmap(data.data)
            chosenImageBitmap?.let{
                this.imageBitmap = chosenImageBitmap
                new_habit_image.setImageBitmap(chosenImageBitmap)
                Log.d(TAG, "Read image bitmap and updated image view")
            }
        }
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(contentResolver, data)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun saveHabit(v: View) {
        if (new_habit_title.viewIsBlank() || new_habit_description.viewIsBlank()) {
            Log.d(TAG, "No habit stored: Title or Description missing")
            displayErrorMessage("Your habit needs a title and description")
            return
        } else if (imageBitmap == null) {
            Log.d(TAG, "No habit stored: Image missing")
            displayErrorMessage("Your habit needs a motivating image")
            return
        }

        val title = new_habit_title.text.toString()
        val description = new_habit_description.text.toString()
        //We can use !! because we know that it should not be null
        val habit = Habit(title, description, imageBitmap!!)

        val id = HabitDatabaseTable(this).store(habit)
        if (id == -1L) {
            displayErrorMessage("Habit could not be stored, let's not make a habit of this...")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayErrorMessage(errorMessage: String) {
        new_habit_error.text = errorMessage
        new_habit_error.visibility = View.VISIBLE
    }

    private fun EditText.viewIsBlank()  = this.text.toString().isBlank()
}
