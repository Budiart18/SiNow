package com.group2.sinow.presentation.detail.viewitems

import android.view.View
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemHeaderClassMaterialBinding
import com.group2.sinow.databinding.ItemListVideoChapterBinding
import com.xwray.groupie.viewbinding.BindableItem

class HeaderItemVideoChapter(
    private val title: String?,
    private val duration: Int?,
    private val onHeaderClick: (item: String) -> Unit
) :
    BindableItem<ItemHeaderClassMaterialBinding>() {
    override fun bind(viewBinding: ItemHeaderClassMaterialBinding, position: Int) {
        viewBinding.tvChapterMaterials.text = title
        viewBinding.tvDuration.text = String.format("%d Menit", duration)
        viewBinding.root.setOnClickListener { onHeaderClick.invoke(title.toString()) }

    }

    override fun getLayout(): Int = R.layout.item_header_class_material

    override fun initializeViewBinding(view: View): ItemHeaderClassMaterialBinding =
        ItemHeaderClassMaterialBinding.bind(view)
}

class DataItemVideoChapter(
    private val moduleName: String?,
    private val moduleStatus: String?,
    private val moduleNumber: Int?,
    private val onItemClick: (item: String) -> Unit
) :
    BindableItem<ItemListVideoChapterBinding>() {
    override fun bind(viewBinding: ItemListVideoChapterBinding, position: Int) {
        viewBinding.tvTitleUnlock.text = moduleName
        when (moduleStatus) {
            "terbuka" -> {
                viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_play)
            }

            "terkunci" -> {
                viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_lock_video)
            }

            else -> {
                viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_checked)
            }
        }
        viewBinding.tvNumber.text = moduleNumber.toString()
        viewBinding.root.setOnClickListener { onItemClick.invoke(moduleName.toString()) }
    }

    override fun getLayout(): Int = R.layout.item_list_video_chapter

    override fun initializeViewBinding(view: View): ItemListVideoChapterBinding =
        ItemListVideoChapterBinding.bind(view)
}