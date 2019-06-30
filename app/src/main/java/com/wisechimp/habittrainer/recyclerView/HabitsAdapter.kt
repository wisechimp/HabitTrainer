package com.wisechimp.habittrainer.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wisechimp.habittrainer.R
import kotlinx.android.synthetic.main.single_card.view.*

class HabitsAdapter(val habits: List<Habit>) : RecyclerView.Adapter<HabitViewHolder>() {

    // Populates the View on the fly
    override fun onBindViewHolder(holder: HabitViewHolder, index: Int) {
        val habit = habits[index]
        val rvCard = holder.card
        rvCard.text_view_title.text = habit.title
        rvCard.text_view_description.text = habit.description
        rvCard.image_view_icon.setImageResource(habit.image)
    }

    //Creates a new View Holder item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return HabitViewHolder(view)
    }

    override fun getItemCount() = habits.size
}
