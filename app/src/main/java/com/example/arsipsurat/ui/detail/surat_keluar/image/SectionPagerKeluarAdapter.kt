package com.example.arsipsurat.ui.detail.surat_keluar.image

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerKeluarAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity){
    var imageKeluar : Parcelable? = null
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = SuratKeluarFragment()
            1 -> fragment = LampiranKeluarFragment()
        }
        return fragment as Fragment
    }
}