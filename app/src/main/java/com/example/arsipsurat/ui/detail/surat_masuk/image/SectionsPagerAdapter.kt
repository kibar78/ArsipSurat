package com.example.arsipsurat.ui.detail.surat_masuk.image

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){
    var image: Parcelable? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = SuratFragment()
            1 -> fragment = LampiranFragment()
        }
        fragment?.arguments = Bundle().apply {
            putParcelable(SuratFragment.IMAGE_SURAT, image)
            putParcelable(LampiranFragment.IMAGE_LAMPIRAN, image)
        }
        return fragment as Fragment
    }
}