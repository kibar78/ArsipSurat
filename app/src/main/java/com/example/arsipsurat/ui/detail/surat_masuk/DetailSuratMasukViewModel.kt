package com.example.arsipsurat.ui.detail.surat_masuk

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arsipsurat.data.model.DetailSuratMasukResponse
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.model.SuratMasukResponse
import com.example.arsipsurat.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSuratMasukViewModel: ViewModel() {
    private val _detailSuratMasuk = MutableLiveData<DetailSuratMasukResponse>()
    val detailSuratMasuk : LiveData<DetailSuratMasukResponse> = _detailSuratMasuk

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailSuratMasukViewModel"
        private const val SURAT_MASUK = ""
    }

    init {
        getDetailSuratMasuk(SURAT_MASUK)
    }

    fun getDetailSuratMasuk(suratMasuk: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailSuratMasuk(suratMasuk)
        client.enqueue(object : Callback<DetailSuratMasukResponse>{
            override fun onResponse(
                call: Call<DetailSuratMasukResponse>,
                response: Response<DetailSuratMasukResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detailSuratMasuk.value = response.body()
                }else{
                    Log.e(TAG,"onFailure :${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailSuratMasukResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}