package com.alaturing.incidentify.main.incident.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil3.load
import com.alaturing.incidentify.databinding.IncidentItemBinding
import com.alaturing.incidentify.main.incident.model.Incident

/**
 * Adaptarador que implementa [ListAdapter] para incidencias
 */
class IncidentsAdapter constructor(
    private val toMaps: (Int)->Unit
): ListAdapter<Incident, IncidentsAdapter.IncidentViewHolder>(
    IncidentDiffCallback
) {

    inner class IncidentViewHolder(
        private val binding:IncidentItemBinding
    ):ViewHolder(binding.root) {

        fun bind(i:Incident) {
            binding.showMapBtn.setOnClickListener {
                toMaps(i.id)
            }
            binding.incidentDescription.text = i.description
            binding.incidentStatus.isChecked = i.solved
             if (i.photoUri!=null) {
                binding.evidenceImage.load(i.photoUri)
            }
            else {
                // TODO poner un placeholder
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val binding = IncidentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  IncidentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) = holder.bind(getItem(position))



    object IncidentDiffCallback: DiffUtil.ItemCallback<Incident>() {
        override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
            return oldItem.description == newItem.description &&
                    oldItem.solved == newItem.solved
        }

    }
}