package jp.kyamlab.securitysample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        button_write_encrypted_shared_preferences.setOnClickListener {
            writeEncryptedSharedPreferences()
        }

        button_read_encrypted_shared_preferences.setOnClickListener {
            textview_second.text = readEncryptedSharedPreferences()
        }
    }

    private fun writeEncryptedSharedPreferences(){
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences =  EncryptedSharedPreferences
            .create(
                "my_secret",
                masterKeyAlias,
                requireContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        sharedPreferences.edit()
            .putString(SHARED_PREF_TAG, "Hello world!")
            .apply()
    }

    private fun readEncryptedSharedPreferences(): String{
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences =  EncryptedSharedPreferences
            .create(
                "my_secret",
                masterKeyAlias,
                requireContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        val message = sharedPreferences.getString(SHARED_PREF_TAG, null)
        return message ?: "message not found"
    }

    companion object{
        private const val SHARED_PREF_TAG = "SECRET_MESSAGE"
    }
}
