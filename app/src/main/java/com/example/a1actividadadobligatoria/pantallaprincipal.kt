package com.example.a1actividadadobligatoria

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location // Importa la clase Location correcta
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.a1actividadadobligatoria.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient // Import para el cliente de ubicación
import com.google.android.gms.location.LocationServices // Import para inicializar el cliente
import java.util.Calendar

class pantallaprincipal : AppCompatActivity() {
    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    //La siguiente variable acepta como string los permisos a lanzar
    private lateinit var requestPermissionsLauncher : ActivityResultLauncher<Array<String>>
    private var todoslospermiso = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.mainMainActvity)
        enableEdgeToEdge()
        /*
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.mainMainActvity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //inicializamos el cliente


        cargarNombre()
        init()
    }

    private fun init(){
        LanzadorPermisos()
        if(!mirarpermisos()){
            requestPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.SEND_SMS
                )
            )
        }
        //Boton para la configuracion
        mainBinding.btnConfig.setOnClickListener {
            val nameSharedFich = getString(R.string.name_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val nameSharename = getString(R.string.name_shered_name)
            val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
            val edit = sharedFich.edit()
            edit.remove(nameSharedPhone)
            edit.remove(nameSharename)
            edit.apply()
            val intent = Intent(this, configActivity::class.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("back", true)
                }
            startActivity(intent)
        }

        //Boton de SOS
        mainBinding.btnSOS.setOnClickListener {
            if (permisosLlamar()){
                val intent = Intent(this, Llamada::class.java)
                    .apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        putExtra("numero", obtenerTelefono())
                    }
                startActivity(intent)
            }else{
                Toast.makeText(this, R.string.msg_not_valid_number, Toast.LENGTH_SHORT).show()
            }
        }


        //boton de lanzar URL
        mainBinding.btnURL.setOnClickListener {
            BuscarURL("https://www.juntadeandalucia.es/temas/salud/servicios/telefonos.html")
        }

        //Boton de la alarma
        mainBinding.btnAlarma.setOnClickListener {
            PonerAlarma()
        }

        //Boton de enviar SMS
        mainBinding.btnUbicacion.setOnClickListener {
            obtenerUbicacion()
        }

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




    //Esta funcion lanza los permisos que se necesitan
    private fun LanzadorPermisos() {
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->
            permisos.forEach { (permiso, isGranted) ->
                if (isGranted) {
                    todoslospermiso = true
                    Toast.makeText(this, "$permiso concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "$permiso denegado", Toast.LENGTH_SHORT).show()
                    goToConfiguracionApp() // abre configuración manual de permisos
                }
            }
        }
    }

    //Carga el nombre del usuario en los text
    private fun cargarNombre(){
        val nameSharedFich = getString(R.string.name_shared_fich)
        val nameShare = getString(R.string.name_shered_name)
        val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
        val nombre = sharedFich.getString(nameShare, "Usuario")
        mainBinding.textView.text = nombre
    }


    @SuppressLint("MissingPermission") // esto se pone porque ya verificamos los permisos con permisoUbicacion()
    private fun obtenerUbicacion()  {
        if (permisoUbicacion()) {
            val location = fusedLocationClient.lastLocation
            location.addOnSuccessListener { location: Location? ->
                if (location != null){
                    val ubicacion = arrayOf(location.latitude, location.longitude)
                    mandarSMSUbi(ubicacion)
                }
            }
        }
    }

    private fun obtenerTelefono() : String?{
        val nameSharedFich = getString(R.string.name_shared_fich)
        val nameSharedPhone = getString(R.string.name_shared_phone)
        val sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)
        return sharedFich.getString(nameSharedPhone, null)
    }

    //Este metodo manda el SMS con la ubicacion que recibe en array de doubles
    private fun mandarSMSUbi(ubicacion: Array<Double>){
        val telefono = obtenerTelefono()
        if (ubicacion != null) {
            if (telefono != null) {
                if(permisoSMS()){
                    try {
                        val LinkGoogleMaps = "https://www.google.com/maps/search/?api=1&query=${ubicacion[0]},${ubicacion[1]}"
                        val MernsajjeSMS = "Mi ultima ubicacion conocida es: $LinkGoogleMaps"
                        val MandarSMS: SmsManager = this.getSystemService(SmsManager::class.java)
                        MandarSMS.sendTextMessage(telefono, null, MernsajjeSMS, null, null)
                        Toast.makeText(this, R.string.msg_SMS_enviado, Toast.LENGTH_SHORT).show()
                    }catch (e: Exception){
                        Toast.makeText(this, R.string.msg_SMS_no_enviado, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, R.string.msg_not_valid_permission_SMS, Toast.LENGTH_SHORT).show()
                    //he quitado retruns de aqui porque no tendrian que dar problemas
                }
            }else{
                Toast.makeText(this, R.string.msg_empty_phone, Toast.LENGTH_SHORT).show()
                //he quitado retruns de aqui porque no tendrian que dar problemas
            }
        }else{
            Toast.makeText(this, R.string.msg_empty_ubicacion, Toast.LENGTH_SHORT).show()
            //he quitado retruns de aqui porque no tendrian que dar problemas
        }
    }

    /*
         Inicio del bloque de permisos
     */

    //metodo para unir todos los permisos en uno
    private fun mirarpermisos():Boolean{
        var respuesta = false
        var permisosLlamada = permisosLlamar()
        var permisosSMS = permisoSMS()
        var permisosUbicacion = permisoUbicacion()
        //si todos los permisos estan correctos debe entrar aqui
        if(permisosLlamada == true && permisosSMS == true && permisosUbicacion == true ){
            respuesta = true
        }
        return respuesta
    }

    //metodo para los permisos de la llamada
    private fun permisosLlamar() : Boolean{
        var respuesta = false
        //Para versión del sdk inferior a la API 23, no hace falta pedir permisos en t. ejecución.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            respuesta = true //no hace falta pedir permisos en t. real al usuario
        else
           respuesta = permisosLlamar2()
        return respuesta
    }
    private fun permisosLlamar2() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED //Hay que ver si se concedieron en ejecución o no.

    //metodo para comprobar permisos de SMS
    private fun permisoSMS() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    private fun permisoUbicacion() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    /*
        Fin de bloque de permisos
     */

    //Esta funcion abre la configuracion manual de permisos
    private fun goToConfiguracionApp(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }



}


