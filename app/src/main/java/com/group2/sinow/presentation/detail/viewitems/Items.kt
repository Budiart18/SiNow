package com.group2.sinow.presentation.detail.viewitems

import android.view.View
import com.group2.sinow.R
import com.group2.sinow.databinding.ContentChapterVideoBinding
import com.group2.sinow.databinding.ItemHeaderClassMaterialBinding
import com.xwray.groupie.viewbinding.BindableItem

class HeaderItem(private val title: String?, private val onHeaderClick: (item: String) -> Unit) :
    BindableItem<ItemHeaderClassMaterialBinding>() {
    override fun bind(viewBinding: ItemHeaderClassMaterialBinding, position: Int) {
        viewBinding.tvChapterMaterials.text = title
        viewBinding.root.setOnClickListener { onHeaderClick.invoke(title.toString()) }
    }

    override fun getLayout(): Int = R.layout.item_header_class_material

    override fun initializeViewBinding(view: View): ItemHeaderClassMaterialBinding =
        ItemHeaderClassMaterialBinding.bind(view)
}

class DataItem(
    private val moduleName: String?,
    private val moduleStatus: String?,
    private val onItemClick: (item: String) -> Unit
) :
    BindableItem<ContentChapterVideoBinding>() {
    override fun bind(viewBinding: ContentChapterVideoBinding, position: Int) {
        viewBinding.tvTitleUnlock.text = moduleName
        if (moduleStatus == "terbuka") {
            viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_play)
        } else if (moduleStatus == "terkunci"){
            viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_lock_video)
        } else {
            viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_checked)
        }
        viewBinding.tvNumber.text = position.toString()
        viewBinding.root.setOnClickListener { onItemClick.invoke(moduleName.toString()) }
    }

    override fun getLayout(): Int = R.layout.content_chapter_video

    override fun initializeViewBinding(view: View): ContentChapterVideoBinding =
        ContentChapterVideoBinding.bind(view)
}