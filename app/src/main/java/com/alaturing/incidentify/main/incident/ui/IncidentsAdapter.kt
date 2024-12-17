package com.alaturing.incidentify.main.incident.ui

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
class IncidentsAdapter: ListAdapter<Incident, IncidentsAdapter.IncidentViewHolder>(IncidentDiffCallback) {

    inner class IncidentViewHolder(
        private val binding:IncidentItemBinding
    ):ViewHolder(binding.root) {
        fun bind(i:Incident) {
            binding.incidentDescription.text = i.description
            binding.incidentStatus.isChecked = i.solved
             if (i.smallPhotoUrl!=null) {
                binding.evidenceImage.load(i.smallPhotoUrl)
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
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
            return oldItem.description == newItem.description &&
                    oldItem.solved == newItem.solved
        }

    }
}