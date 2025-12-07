# 1actividadadObligatoria

## Descripción

Esta es una aplicación para Android desarrollada como un proyecto de estudiante. Sirve como una herramienta multifuncional que permite a los usuarios realizar diversas acciones como hacer llamadas, enviar SMS con la ubicación, establecer alarmas y más, directamente desde una pantalla central. La aplicación requiere la configuración del usuario en el primer inicio para almacenar un nombre y un número de teléfono válido.

## Características

*   **Configuración de usuario:** Guarda el nombre y el número de teléfono del usuario para rellenar previamente las acciones.
*   **Acciones rápidas:**
    *   Abrir una URL web.
    *   Establecer una alarma.
    *   Enviar la ubicación actual por SMS al número de teléfono configurado.
*   **Juego de dados:** Un sencillo juego de tirar los dados.
*   **Chistes:** Muestra chistes.
*   **Llamadas:** Inicia una llamada telefónica.
*   **Manejo de permisos:** Solicita de forma elegante los permisos necesarios para llamadas, ubicación y SMS.

## Componentes y Métodos Principales

Aquí tienes una explicación de algunos de los métodos más importantes del código:

### `pantallaprincipal.kt`

Esta es la pantalla principal de la aplicación después de la configuración inicial.

*   `LanzadorPermisos()`: Esta función es responsable de manejar los permisos en tiempo de ejecución. Utiliza `registerForActivityResult` con `RequestMultiplePermissions` para solicitar los permisos `CALL_PHONE`, `ACCESS_FINE_LOCATION` y `SEND_SMS` al usuario. Proporciona información al usuario a través de Toasts sobre si los permisos fueron concedidos o denegados.
*   `obtenerUbicacion()`: Este método obtiene la última ubicación conocida del dispositivo utilizando el `FusedLocationProviderClient`. Antes de obtenerla, comprueba si se ha concedido el permiso de ubicación. Al obtener la ubicación con éxito, procede a llamar a `mandarSMSUbi`.
*   `mandarSMSUbi()`: Esta función envía un SMS que contiene un enlace de Google Maps de la ubicación actual del usuario al número de teléfono almacenado durante la configuración. Primero comprueba si se concede el permiso de SMS y si hay un número de teléfono disponible en `SharedPreferences`. Utiliza el `SmsManager` para enviar el mensaje de texto.
*   `PonerAlarma()`: Una sencilla función de utilidad para establecer una alarma a 2 minutos de la hora actual utilizando `AlarmClock.ACTION_SET_ALARM`. También maneja los casos en los que ninguna aplicación puede manejar el intent de la alarma.
*   `mostradorActividades()`: Esto configura un `RecyclerView` con un `LinearLayoutManager` horizontal para mostrar una lista de acciones rápidas con las que el usuario puede interactuar.

### `configActivity.kt`

Esta es la primera pantalla que ve el usuario si no ha configurado la aplicación.

*   `start()`: Este método comprueba si ya hay un número de teléfono y un nombre almacenados en `SharedPreferences`. Si es así, lanza directamente la `pantallaprincipal`. En caso contrario, establece un listener en el botón "Aceptar".
*   `isValidPhoneNumber()`: Un método de utilidad crucial que utiliza la biblioteca `libphonenumber` de Google para validar si el número de teléfono introducido es válido para el país indicado (en este caso, "ES" para España). Esto evita que los usuarios introduzcan números de teléfono incorrectos. Cuando el usuario hace clic en el botón "Aceptar", la entrada se valida utilizando este método antes de ser guardada.

## Cómo ejecutar el proyecto

1.  Clona el repositorio.
2.  Abre el proyecto en Android Studio.
3.  Deja que Gradle sincronice y descargue las dependencias necesarias.
4.  Ejecuta la aplicación en un emulador o en un dispositivo físico.
5.  En el primer inicio, se te pedirá que introduzcas tu nombre y número de teléfono.
6.  Concede los permisos necesarios cuando se te solicite.

## Mejoras futuras

*   Añadir más acciones a la pantalla principal.
*   Permitir al usuario personalizar el número de teléfono del destinatario para el SMS de ubicación.
*   Mejorar la UI/UX de la aplicación.
*   Añadir pruebas unitarias para la lógica principal.
