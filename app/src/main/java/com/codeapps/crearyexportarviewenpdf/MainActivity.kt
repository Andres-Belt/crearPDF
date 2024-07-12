package com.codeapps.crearyexportarviewenpdf

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codeapps.crearyexportarviewenpdf.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bitmapsing: Bitmap
    private lateinit var bitmapScaleSing: Bitmap


    private var descripcion : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste de parametros para la firma
        // anchos maximos y minimos del trazo
        binding.signaturePad.setMaxWidth(6F)
        binding.signaturePad.setMinWidth(3F)

        /// gestiona el borrado de la firma
        binding.btnerasesign.setOnClickListener {
            binding.signaturePad.clear()
        }
        /// Gestiona el guardado de la firma en una variable tipo bitmap
        binding.btnsavesign.setOnClickListener {
                bitmapsing = binding.signaturePad.transparentSignatureBitmap
        }

        // se gestionan los permisos de lectura y escritura
        if (checkPermission()) {
                Toast.makeText(this,"Permisos Concedidos", Toast.LENGTH_LONG).show()
        } else {
            requestPermission()
        }

        binding.btngenerar.setOnClickListener {
            generarPfd()
        }
        binding.btnver.setOnClickListener {
            val i = Intent(this,PdfShowActivity::class.java)
            startActivity(i)
        }

    }

    /// la siguiente funcion se encarga de la creacion de documento PDF
    @SuppressLint("SimpleDateFormat")
    private fun generarPfd() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val tittle = TextPaint()
        val description = TextPaint()
        val encabezados = TextPaint()
        val firm = TextPaint()

        val pageInfo = PdfDocument.PageInfo.Builder(820, 1054, 1).create() /// CREA LA PAGINA DEL PDF
        val page1 = pdfDocument.startPage(pageInfo) // INICIALIZA LA FUNCION DE CREACION DEL DOCUMENTO Y LE PASA LA INFO DE LA INFORMACION DE LA PAGINA, CARACTERISTICAS Y DEMAS

        val encabezado1  = "COLEGIO EL PROVENIR"
        val encabezado2 = "I.E.D"
        val nombreArchivo = "FORMATO OBSERVADOR ESCOLAR"
        val nombreEstudiante = "Nombre estudiante:"
        val nombreEstudiante2 = "Pepito Perez"
        val fechaSuceso1 = "Fecha del evento:"
        val fechaSuceso2 = "1/2/2024"
        val curso1 = "Curso:"
        val curso2 = "Quinto A"
        val actividadreportar = "Actividad a reportar:"
        val txtfirma = "Firma"


        val canvas = page1.canvas

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.escudo) /// importa el escudo del colegio
        val bitmapScale = Bitmap.createScaledBitmap(bitmap, 80,80,false)// CREA EL MAPA DE BITS ESCALADO
        canvas.drawBitmap(bitmapScale, 50f, 30f,paint) // DIBUJA EL MAPA DE BIST ESCALADO

        encabezados.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)  // CREA UNA FUNCION DE CANVAS TIPO TEXT POR CODIGO PARA EL TITULO
        encabezados.textSize = 40F /// DEFINE EL TAMAÑO DEL ENCABEZADO
        canvas.drawText(encabezado1,240F,55F,encabezados)/// nombre del colegio
        canvas.drawText(encabezado2,410F,100F,encabezados)/// Complemento nombre del colegio

        ///// dibuja el texto del nombre del formato
        tittle.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)//
        tittle.textSize = 28f // AJUSTA EL TAMAÑO DEL TEXTO FIRMA
        canvas.drawText(nombreArchivo,50F,180F,tittle)///
        //// dibuja la linea debajo del nombre del archivo
        canvas.drawRect(50F,200F,770F,204F,paint)

        ///// dibuja el  titulo: nombre del estudiante
        tittle.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(nombreEstudiante,50F,250F,tittle)///
        ///// dibuja la "variable" para el nombre del estudiante
        tittle.typeface = Typeface.defaultFromStyle((Typeface.NORMAL))//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(nombreEstudiante2,500F,250F,tittle)///

        ///// dibuja el  titulo: curso
        tittle.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(curso1,50F,290F,tittle)///
        ///// dibuja la "variable" para el curso
        tittle.typeface = Typeface.defaultFromStyle((Typeface.NORMAL))//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(curso2,500F,290F,tittle)///

        ///// dibuja el  titulo: fecha del suceso
        tittle.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(fechaSuceso1,50F,330F,tittle)///
        ///// dibuja la "variable" para la fecha del suceso
        tittle.typeface = Typeface.defaultFromStyle((Typeface.NORMAL))//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO
        tittle.textSize = 20f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(fechaSuceso2,500F,330F,tittle)///

        ///// dibuja el  titulo: actividad a reportar
        tittle.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO
        tittle.textSize = 24f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(actividadreportar,50F,380F,tittle)///


        /////// texto ingresado por el usuario
        description.typeface = Typeface.defaultFromStyle((Typeface.NORMAL))  // CREA UNA FUNCION DE CANVAS TIPO TEXT POR CODIGO PARA EL TEXTO
        description.textSize = 24F /// DEFINE EL TAMAÑO DEL Texto
        descripcion = binding.ettittle.text.toString() /// captura el dato del editText y lo convierte a string

        //////prueba dibujar el input text haciendo una rutina para agregar el salto de linea

        var textModificado  = insertarSignos(descripcion!!, 64)  //// llama a la funcion que agrega el salto de linea y regresa un string on los saltos
        // la funcion recibe por parametro el texto ingresaso por el usuario y un entero que determina cuando se agrega el sato de linea

        val arrayDescription = textModificado!!.split("\n") // INDICA CUAL ES EL ELEMENTO PARA SEPARAR LOS SALTOS DE LINEA
        var y = 410f ///CREA LA VARIABLE DESDE EL PUNTO EN Y DONDE INICIA EL DIBUJO DEL TEXT

       /// eL SIGUIENTE FOR HACE UN BARRIDO PINTANDO CADA ELEMENTO EN DESCRIPTION
        for(item in arrayDescription) {
            canvas.drawText(item,50f, y,description) // DIBUJA CADA LETRA DEL ARREGLO DESCRIPTION INICIANOD EL LA PARTE IZQUIETDA EN 10 Y EN Y EN Y(200)
            y += 24 /// INCREMENTA EL ESPACIO ENTRE LINEAS DEL TEXTO
        }

///////////////////

        ///dibuja la firma
        try {
         bitmapScaleSing = Bitmap.createScaledBitmap(bitmapsing,200,150,false)// CREA EL MAPA DE BITS ESCALADO
         canvas.drawBitmap(bitmapScaleSing,300f,760f,paint) // DIBUJA EL MAPA DE BIST ESCALADO

        }catch (e : Exception ){
            e.printStackTrace()
        }

        //// dibuja la linea para la firma
        canvas.drawRect(200F, 910F, 600F, 911F, paint)
        ///// dibuja el texto firma
        firm.typeface =
            Typeface.defaultFromStyle((Typeface.NORMAL))//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO FIRMA
        firm.textSize = 14f // AJUSTA EL TAMAÑO DEL TEXTO FIRMA
        canvas.drawText(txtfirma, 390F, 940F, firm)///

        //// dibuja el escudo de bogota
        val bitmapBog = BitmapFactory.decodeResource(resources, R.drawable.bogotayescudoalcaldia) /// importa el escudo alcaldia bog
        val bitmapScaleBog = Bitmap.createScaledBitmap(bitmapBog, 140,45,false)// CREA EL MAPA DE BITS ESCALADO
        canvas.drawBitmap(bitmapScaleBog, 320f, 1000f,paint) // DIBUJA EL MAPA DE BIST ESCALADO EN LA POSICION INDICADA

        ///// dibuja la fecha automatica del sistema
        val date = Date()
        val format = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z")
        description.typeface = Typeface.defaultFromStyle((Typeface.NORMAL))//  CREA UNA IMAGEN CANVAS TIPO TEXTO POR CODIGO PARA EL TEXTO
        description.textSize = 16f // AJUSTA EL TAMAÑO DEL TEXTO
        canvas.drawText(format.format(date),10F,1030F,description)///

        pdfDocument.finishPage(page1)

       // val ruta_dir_interno = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/" + "Reporte" + ".pdf"
        val ruta_dir_interno = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.canonicalPath + "/" + "Reporte" + ".pdf"
        val file = File(ruta_dir_interno)

        val outputStream = FileOutputStream(file)

        try{
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            Toast.makeText(this, "Archivo guardado en la memoria externa de forma exitosa", Toast.LENGTH_LONG).show()
        }catch (e : Exception ){
            e.printStackTrace()
        }
    }

    private fun insertarSignos(descripcion: String, texttSize: Int): String{

        val builder = StringBuilder() /// crea un stringbuilder que es mas eficiente
        var contador = 0

        for (caracter in descripcion) {///hace un barrido del tamaño de la cadena de texto ingresada por el usuario
            builder.append(caracter) //// en el stringbuilder va poniendo caracter a caracter los datos ingresadospor el usuario
            contador++

            if (contador == texttSize) {//Si el contador alcanza el numero que definimos arriba para el salto de lineaagrega el salto y reinicia el contador
                builder.append("\n")
                contador = 0 // Reiniciar el contador después de agregar el signo
            }
        }
        return builder.toString() ///Regresa el stringbuilder onvertido a cadena de texto
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///funcion para gestionar permisos de lectura y escritura
    private fun checkPermission(): Boolean {
        val permissionRead = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        val permissionWrite = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        return permissionWrite == PackageManager.PERMISSION_GRANTED && permissionRead == PackageManager.PERMISSION_GRANTED
    }

    ///funcion para gestionar permisos de lectura y escritura
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
            ), 1005
        )
    }

    ///funcion para gestionar permisos de lectura y escritura
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1005) {
            if (grantResults.isNotEmpty()) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permisos Consedidos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permisos Denegados", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
    /// esta funcion evita que en el life cicle al estar en onpause u ondestroy la app falle
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}