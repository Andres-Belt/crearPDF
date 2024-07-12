package com.codeapps.crearyexportarviewenpdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import com.codeapps.crearyexportarviewenpdf.databinding.ActivityPdfShowBinding
import java.io.File

class PdfShowActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPdfShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val ruta_dir_interno = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.canonicalPath + "/" + "Reporte" + ".pdf"
        //val file = File(ruta_dir_interno)

        //val ruta_dir_interno = File(getExternalFilesDir(".pdf"), "Reporte.pdf")

        val ruta_dir_interno = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.canonicalPath + "/" + "Reporte" + ".pdf"

        binding.vistapdf.fromFile(ruta_dir_interno)
        binding.vistapdf.isZoomEnabled = true
        binding.vistapdf.show()
    }
}