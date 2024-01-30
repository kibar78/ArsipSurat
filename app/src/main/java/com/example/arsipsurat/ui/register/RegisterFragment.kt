package com.example.arsipsurat.ui.register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arsipsurat.data.ViewModelFactory
import com.example.arsipsurat.data.model.user.UserItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.FragmentRegisterBinding
import com.example.arsipsurat.ui.register.create.BuatAkunActivity
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val registerAdapter = RegisterAdapter()

    private val viewModelRegister by viewModels<RegisterViewModel>(){
        ViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelRegister.uiStateUser.observe(viewLifecycleOwner){uiStateUser->
            when(uiStateUser){
                is Result.Loading->{
                    showLoading(true)
                }
                is Result.Success->{
                    setUser(uiStateUser.data)
                    showLoading(false)
                }
                is Result.Error->{
                    Toast.makeText(requireActivity(), uiStateUser.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

        viewModelRegister.deleteDataSuccess.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                if (it){
                    val mySnackbar = Snackbar.make(binding.root, "Akun telah dihapus", Snackbar.LENGTH_SHORT)
                    mySnackbar.show()
                }
                else{
                    val mySnackbar = Snackbar.make(binding.root, "Akun gagal dihapus", Snackbar.LENGTH_SHORT)
                    mySnackbar.show()
                }
            }
        }

        binding.fabDaftarAkun.setOnClickListener{
            val intent = Intent(requireActivity(), BuatAkunActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        viewModelRegister.getUser(RegisterViewModel.ID)
        super.onResume()

    }
    private fun setUser(dataUser: List<UserItem?>){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        registerAdapter.listUser = dataUser
        binding.rvUser.adapter = registerAdapter
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.setHasFixedSize(true)

        registerAdapter.onLongClick = {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Peringatan")
                setMessage("Apa anda yakin untuk menghapus data ini?\nLevel : ${it.level}")
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Ya") { _, _ ->
                    binding.rvUser.visibility = View.INVISIBLE
                    showLoading(true)
                    viewModelRegister.delete(it)
                }
            }
            alertDialog.show()
        }
        registerAdapter.notifyDataSetChanged()
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.rvUser.visibility = View.INVISIBLE
        }
        else {
            binding.pbLoading.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


