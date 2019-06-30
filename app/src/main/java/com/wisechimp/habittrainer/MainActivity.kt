package com.wisechimp.habittrainer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisechimp.habittrainer.recyclerView.HabitsAdapter
import com.wisechimp.habittrainer.recyclerView.getSampleHabits
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = HabitsAdapter(getSampleHabits())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_habit) {
            moveToNewActivity(CreateHabitActivity::class.java)
        }
        return true
    }

    private fun moveToNewActivity(newClass: Class<*>) {
        val intent = Intent(this, newClass)
        startActivity(intent)
    }
}
