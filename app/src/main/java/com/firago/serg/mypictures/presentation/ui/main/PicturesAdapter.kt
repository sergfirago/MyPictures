package com.firago.serg.mypictures.presentation.ui.main

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firago.serg.mypictures.R
import com.firago.serg.mypictures.domain.PictureLink
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*

class PictureLinksDiffCallback(val oldList: List<PictureLink>?, val newList: List<PictureLink>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem?.id == newItem?.id
    }

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList?.get(oldItemPosition) == newList?.get(newItemPosition)
    }

}

private const val LOG_TAG = "PicturesAdapter"

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {
    private var onClick: ((PictureLink) -> Unit) = {}
    fun setOnItemClickListener(f: (PictureLink) -> Unit) {
        onClick = f
    }


    var data: List<PictureLink>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_picture, parent, false)
        )
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(data!![position], onClick)
    }

    fun swapData(data: List<PictureLink>?, detectMoves: Boolean = true) {

        val oldData = this.data
        this.data = data

        val diffCallback = PictureLinksDiffCallback(oldData, data)

        val calculateDiff = DiffUtil.calculateDiff(diffCallback, detectMoves)

        if (detectMoves) {
            calculateDiff.dispatchUpdatesTo(this)
        } else {
            notifyDataSetChanged()
        }
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PictureLink, onClick: ((PictureLink) -> Unit)) = with(itemView) {
            showProgress()
            Picasso.get()
                    .load(item.linkToPreview)
                    .placeholder(R.drawable.ic_download_black_300dp)
                    .resize(300, 300)
                    .centerCrop()
                    .into(itemView.imageView, object : Callback {
                        override fun onSuccess() {
                            Log.d(LOG_TAG, "success")
                            showImage()
                        }

                        override fun onError(e: Exception?) {
                            Log.d(LOG_TAG, "error")
                            showImage()
                        }

                    })

            itemView.imageView.setOnClickListener {
                onClick(item)
                Log.d(LOG_TAG, "Click ${item.linkToFile}")
            }
        }

        private fun showProgress() {
            Log.d(LOG_TAG, "show progress")
            itemView.progressBar3.visibility = View.VISIBLE
            itemView.imageView.visibility = View.INVISIBLE
        }

        private fun showImage() {
            itemView.progressBar3.visibility = View.INVISIBLE
            itemView.imageView.visibility = View.VISIBLE
        }
    }
}