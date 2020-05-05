package jp.kyamlab.securitysample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.fragment_first.*
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        button_write_file.setOnClickListener{
            createEncryptedFile()
        }
    }

    /**
     * 暗号化したファイルの作成
     * <p>
     * 参考: {@link https://developer.android.com/topic/security/data?hl=ja#write-files}
     * </p>
     */
    private fun createEncryptedFile(){
        val context = requireContext()
        val dir = context.filesDir

        // マスターキーの作成
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val fileToWrite = "my_sensitive_data.txt"
        val encryptedFile = EncryptedFile.Builder(
            File(dir, fileToWrite),
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        // 暗号化ファイルの作成
        // 参考リンクではopenFileOutput()がないが、openFileOutput()を使わないとbufferedWriter()が使えない
        encryptedFile.openFileOutput()
            .bufferedWriter()
            .use{ writer ->
            writer.write("MY SUPER-SECRET INFORMATION")
        }

        Log.d("暗号化ファイルのパス", context.filesDir.toString())
    }
}
