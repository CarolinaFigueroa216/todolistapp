# Todo List App 📝

Una aplicación Android nativa para gestionar tareas diarias con sincronización en tiempo real usando Firebase Firestore y sistema de autenticación de usuarios.

## 🚀 Características

- 🔐 **Sistema de Autenticación**: Login y registro de usuarios
- ✅ **CRUD Completo**: Crear, leer, actualizar y eliminar tareas
- 🔥 **Firebase Firestore**: Base de datos en la nube con sincronización en tiempo real
- 🎨 **Material Design**: Interfaz moderna con colores pastel
- 📱 **RecyclerView**: Lista de tareas eficiente y fluida
- ⚡ **Actualización en Tiempo Real**: Los cambios se reflejan instantáneamente
- ✏️ **Validación de Campos**: Validación de título (mínimo 3 caracteres) y clave (10 dígitos)

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión |
|------------|---------|
| Lenguaje | Java |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |
| Compile SDK | 36 |
| Firebase Firestore | Latest |
| RecyclerView | 1.3.2 |
| CardView | 1.0.0 |
| Material Components | Latest |

## 📦 Estructura del Proyecto

```
app/
├── src/main/java/com/example/todolistapp/
│   ├── LoginActivity.java         # Actividad de inicio de sesión
│   ├── RegistroActivity.java      # Actividad de registro de usuarios
│   ├── MainActivity.java          # Actividad principal (tareas)
│   ├── Tarea.java                 # Modelo de datos
│   ├── TareaAdapter.java          # Adapter para RecyclerView
│   └── AdminSQLiteOpenHelper.java # Helper para SQLite (opcional)
│
├── src/main/res/layout/
│   ├── activity_login.xml         # Layout de inicio de sesión
│   ├── activity_registro.xml      # Layout de registro
│   ├── activity_main.xml          # Layout principal (tareas)
│   ├── item_tarea.xml             # Item individual de tarea
│   └── dialog_tarea.xml           # Diálogo para crear/editar
│
└── src/main/res/values/
    ├── colors.xml                 # Paleta de colores pastel
    ├── strings.xml                # Recursos de texto
    └── themes.xml                 # Temas de la app
```

## 🎨 Componentes Desarrollados

### 1. **LoginActivity.java** - Inicio de Sesión
- Interfaz con título llamativo "BIENVENIDOS A TODOLISTAPP"
- Panel de login con campos Usuario y Clave
- Botón "INGRESAR" para validar credenciales
- Botón "REGISTRAR" para navegar a registro de usuarios
- Validación contra colección "usuarios" en Firestore
- Mensajes Toast para feedback de autenticación

### 2. **RegistroActivity.java** - Registro de Usuarios
- Formulario completo con validaciones:
  - Nombre de usuario (requerido)
  - Correo electrónico (validación de formato)
  - Clave de exactamente 10 dígitos
  - Confirmación de clave (debe coincidir)
- Botón "REGISTRAR" que guarda en Firestore
- Mensaje de éxito: "¡Registro de usuario exitoso!"
- Redirección automática a MainActivity tras registro exitoso

### 3. **MainActivity.java** - Gestión de Tareas
- Inicializa RecyclerView con LinearLayoutManager
- Configura listener en tiempo real de Firebase Firestore
- Muestra diálogo para agregar nuevas tareas
- Valida campos antes de guardar
- Gestiona el ciclo de vida (onDestroy limpia listeners)
- **Botón "Cerrar sesión"**: Permite al usuario salir de la aplicación y regresar al login

### 4. **Tarea.java**
- Modelo de datos con propiedades:
  - `id`: Identificador único del documento
  - `titulo`: Título de la tarea
  - `descripcion`: Descripción detallada
  - `completada`: Estado de completado (boolean)

### 5. **TareaAdapter.java**
- Extiende `RecyclerView.Adapter`
- Muestra lista de tareas en tarjetas (CardView)
- Botones de acción por cada tarea:
  - **Editar**: Abre diálogo con datos prellenados
  - **Eliminar**: Borra la tarea de Firestore
- CheckBox para visualizar estado de completado

### 6. **AdminSQLiteOpenHelper.java**
- Clase helper para base de datos local SQLite
- Crea tabla `tareas` con columnas: id, titulo, descripcion, estado
- *Nota: Actualmente no está integrada en la lógica principal*

### 7. **Layouts**
- **activity_login.xml**: 
  - Título "BIENVENIDOS A TODOLISTAPP" destacado
  - CardView con campos Usuario y Clave
  - Botón INGRESAR (rosa pastel)
  - Botón REGISTRAR (menta pastel) debajo del panel
  
- **activity_registro.xml**:
  - Título "REGISTRO DE USUARIO"
  - Campos: Nombre de usuario, Correo, Clave (10 dígitos), Confirmar clave
  - Contador visible para longitud de clave
  - Botón REGISTRAR
  
- **activity_main.xml**: 
  - Botón "Cerrar sesión" en la parte superior
  - Botón "Agregar tarea"
  - RecyclerView para listar tareas
- **item_tarea.xml**: CardView con título, descripción, checkbox y botones
- **dialog_tarea.xml**: Formulario con TextInputLayout para título y descripción

### 8. **Recursos Visuales**
- Paleta de colores pastel:
  - Rosa Pastel (`#F8C8DC`)
  - Lila Pastel (`#DCC6E0`)
  - Menta Pastel (`#C7EDE6`)

## 🔥 Configuración de Firebase

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Registra tu app Android con el package name: `com.example.todolistapp`
3. Descarga el archivo `google-services.json` y colócalo en `app/`
4. Habilita Firestore Database en modo prueba o configura reglas de seguridad
5. Se crearán automáticamente dos colecciones:
   - `usuarios`: Almacena credenciales de usuarios registrados
   - `tareas`: Almacena las tareas de los usuarios

## 📋 Requisitos Previos

- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Cuenta de Google para Firebase
- Dispositivo/emulador con API 24+

## 🚀 Cómo Ejecutar

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Agrega tu archivo `google-services.json` en la carpeta `app/`
4. Sincroniza el proyecto con Gradle
5. Ejecuta en un dispositivo o emulador

```bash
# Opcional: Build desde terminal
./gradlew assembleDebug
```

## 📱 Flujo de la Aplicación

1. **Pantalla de Login**: 
   - El usuario ve "BIENVENIDOS A TODOLISTAPP"
   - Ingresa usuario y clave para acceder
   
2. **Registro** (opcional):
   - Clic en botón "REGISTRAR"
   - Completa formulario con nombre, correo y clave de 10 dígitos
   - Confirma clave y presiona "REGISTRAR"
   - Recibe mensaje de éxito y es redirigido a la lista de tareas

3. **Gestión de Tareas**:
   - Botón "Cerrar sesión" para salir y regresar al login
   - Agregar nuevas tareas con título y descripción
   - Editar tareas existentes
   - Eliminar tareas
   - Marcar tareas como completadas

## 🔮 Mejoras Futuras

- [ ] Implementar base de datos local SQLite con Room
- [ ] Modo offline con sincronización automática
- [ ] Asociar tareas a usuarios específicos
- [ ] Recuperación de contraseña
- [ ] Categorías/Etiquetas para tareas
- [ ] Fechas de vencimiento
- [ ] Notificaciones push
- [ ] Modo oscuro
- [ ] Búsqueda y filtrado

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👨‍💻 Autor

Desarrollado como proyecto de práctica con Android + Firebase.

---

**Estado del Proyecto**: ✅ Funcional - Sistema de autenticación + CRUD completo implementado con Firebase Firestore
