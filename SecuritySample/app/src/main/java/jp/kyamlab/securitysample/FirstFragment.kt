package jp.kyamlab.securitysample

import android.app.Activity.RESULT_OK
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_first.*
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var keyguardManager: KeyguardManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keyguardManager = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        button_write_file.setOnClickListener{
            createEncryptedFile()
        }

        button_read_file.setOnClickListener {
            showAuthenticationScreen()
        }

        // デバイスロックが有効でないとユーザ認証ができない
        if(!keyguardManager.isKeyguardSecure){
            Snackbar.make(view, "デバイスロックを有効にしてください", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS && resultCode == RESULT_OK){
            val contents = readEncryptedFile()
            textview_first.text = contents
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

    /**
     * 暗号化したファイルの読み込み
     * <p>
     * 参考: {@link https://developer.android.com/topic/security/data?hl=ja#write-files}
     * </p>
     */
    private fun readEncryptedFile(): String{
        val context = requireContext()
        val dir = context.filesDir

        // マスターキーの作成
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val fileToRead = "my_sensitive_data.txt"
        val encryptedFile = EncryptedFile.Builder(
            File(dir, fileToRead),
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        return encryptedFile.openFileInput()
            .bufferedReader()
            .useLines { lines ->
                lines.fold(""){working, line ->
                    "$working\n$line"
                }
            }
    }

    /**
     * デバイスロックの表示
     */
    private fun showAuthenticationScreen(){
        // FIXME deprecatedではない方法に修正
        val intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null)
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
        }
    }

    companion object{
        /** デバイスロックのリクエストコード */
        private const val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0
    }
}
