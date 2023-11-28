package com.example.todolistfirestore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfirestore.databinding.ItemContactBinding

typealias OnClickContact = (Contacts) -> Unit
class ContactsAdapter (
    private var listContacts: List<Contacts>,
    private val onClickContact: OnClickContact):
    RecyclerView.Adapter<ContactsAdapter.ItemContactViewHolder>(){

    private var onClickDeleteListener: ((Contacts) -> Unit)? = null

    fun setOnClickDeleteListener(listener: (Contacts) -> Unit) {
        onClickDeleteListener = listener
    }

        inner class ItemContactViewHolder(private val binding: ItemContactBinding):
                RecyclerView.ViewHolder(binding.root){

                    fun bind(note: Contacts){
                        with(binding){
                            textName.text = note.name
                            textNumber.text = note.number

                            itemView.setOnClickListener{
                                onClickContact(note)
                            }

                            btnDelete.setOnClickListener{
                                onClickDeleteListener?.invoke(note)
                            }
                        }
                    }
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ItemContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: ItemContactViewHolder, position: Int) {
        holder.bind(listContacts[position])
    }
}