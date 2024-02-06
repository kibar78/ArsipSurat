package com.example.arsipsurat.ui.detail.surat_masuk.image

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){
    var image: Parcelable? = null
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = DetailSuratMasukFragment()
            1 -> fragment = SuratFragment()
            2 -> fragment = LampiranFragment()
        }
        return fragment as Fragment
    }
}