package com.example.a1actividadadobligatoria

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1actividadadobligatoria.controller.controlerAccion
import com.example.a1actividadadobligatoria.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar

class pantallaprincipal : AppCompatActivity() {
    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var controller: controlerAccion

    private lateinit var requestPermissionsLauncher : ActivityResultLauncher<Array<String>>
    private var todosLosPermisosConcedidos = false // CORRECCIÓN: Nombre de variable más claro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // enableEdgeToEdge() // CORRECCIÓN: Se elimina esta línea que causa el error de compilación.

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        controller = controlerAccion(this)

        cargarNombre()
        init()
    }

    private fun init(){
        LanzadorPermisos()
        if(!mirarpermisos()){
            // Lanza la solicitud solo si no tienes todos los permisos ya concedidos
            requestPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.SEND_SMS
                )
            )
        }

        mainBinding.btnConfig.setOnClickListener {
            val nameSharedFich = getString(R.string.name_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val nameSharename = getString(R.string.name_shered_name)
            val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
            sharedFich.edit().apply {
                remove(nameSharedPhone)
                remove(nameSharename)
                apply()
            }
            val intent = Intent(this, configActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra("back", true)
            }
            startActivity(intent)
        }

        mainBinding.btnURL.setOnClickListener {
            BuscarURL("https://www.juntadeandalucia.es/temas/salud/servicios/telefonos.html")
        }

        mainBinding.btnAlar.setOnClickListener {
            PonerAlarma()
        }

        mainBinding.btnLoc.setOnClickListener {
            obtenerUbicacion()
        }

        mostradorActividades()
    }


    private fun mostradorActividades(){
        //Creamos el controller
        mainBinding.mostradorAcciones.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //asignamos el adapter
        controller.setAdapter(mainBinding.mostradorAcciones)
    }



    private fun BuscarURL(URL: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(intent)
    }

    private fun PonerAlarma(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 2)

        val hora = calendar.get(Calendar.HOUR_OF_DAY)
        val minuto = calendar.get(Calendar.MINUTE)
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hora)
            putExtra(AlarmClock.EXTRA_MINUTES, minuto)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }

        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
            Toast.makeText(this, R.string.msg_Alarm_puesta_exito, Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, R.string.msg_not_valid_aplication_Alarm, Toast.LENGTH_SHORT).show()
        }
    }

    // CORRECCIÓN: Lógica de permisos mejorada
    private fun LanzadorPermisos() {
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->
            // Comprueba si TODOS los valores en el mapa de permisos son 'true'
            val todosConcedidos = permisos.values.all { it }

            if (todosConcedidos) {
                todosLosPermisosConcedidos = true
                Toast.makeText(this, "Todos los permisos fueron concedidos", Toast.LENGTH_SHORT).show()
            } else {
                todosLosPermisosConcedidos = false
                Toast.makeText(this, "Algunos permisos fueron denegados. Algunas funciones no estarán disponibles.", Toast.LENGTH_LONG).show()
                // goToConfiguracionApp() // ADVERTENCIA: Redirigir aquí crea una mala experiencia de usuario. Es mejor mostrar un diálogo.
            }
        }
    }

    private fun cargarNombre(){
        val nameSharedFich = getString(R.string.name_shared_fich)
        val nameShare = getString(R.string.name_shered_name)
        val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
        val nombre = sharedFich.getString(nameShare, "Usuario")
        mainBinding.textUsarname.text = nombre
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        if (permisoUbicacion()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // CORRECCIÓN: Comprobar si la ubicación es nula
                if (location != null){
                    val ubicacion = arrayOf(location.latitude, location.longitude)
                    mandarSMSUbi(ubicacion)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al obtener la ubicación: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerTelefono(): String? {
        val nameSharedFich = getString(R.string.name_shared_fich)
        val nameSharedPhone = getString(R.string.name_shared_phone)
        val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
        return sharedFich.getString(nameSharedPhone, null)
    }

    private fun mandarSMSUbi(ubicacion: Array<Double>){
        val telefono = obtenerTelefono()
        // CORRECCIÓN: Comprobar primero el teléfono para no hacer trabajo innecesario
        if (telefono == null) {
            Toast.makeText(this, getString(R.string.msg_empty_phone), Toast.LENGTH_SHORT).show()
            return // Salir de la función
        }

        if(!permisoSMS()) {
            Toast.makeText(this, getString(R.string.msg_not_valid_permission_SMS), Toast.LENGTH_SHORT).show()
            return // Salir de la función
        }

        try {
            val linkGoogleMaps = "https://www.google.com/maps/search/?api=1&query=${ubicacion[0]},${ubicacion[1]}"
            val mensajeSMS = "Mi ultima ubicacion conocida es: $linkGoogleMaps"

            // CORRECCIÓN: Usar la forma moderna y segura de obtener SmsManager
            val smsManager: SmsManager? = ContextCompat.getSystemService(this, SmsManager::class.java)
            smsManager?.sendTextMessage(telefono, null, mensajeSMS, null, null)

            Toast.makeText(this, getString(R.string.msg_SMS_enviado), Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(this, getString(R.string.msg_SMS_no_enviado) + ": " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /*
     Inicio del bloque de permisos
     */

    // CORRECCIÓN: Función simplificada
    private fun mirarpermisos(): Boolean {
        return permisosLlamar() && permisoSMS() && permisoUbicacion()
    }

    // CORRECCIÓN: Función simplificada
    private fun permisosLlamar(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    private fun permisoSMS(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun permisoUbicacion(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /*
     Fin de bloque de permisos
     */

    private fun goToConfiguracionApp(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}



