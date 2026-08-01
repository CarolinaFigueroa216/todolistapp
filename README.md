# Todo List App 📝

Una aplicación Android nativa para gestionar tareas diarias con sincronización en tiempo real usando Firebase Firestore.

## 🚀 Características

- ✅ **CRUD Completo**: Crear, leer, actualizar y eliminar tareas
- 🔥 **Firebase Firestore**: Base de datos en la nube con sincronización en tiempo real
- 🎨 **Material Design**: Interfaz moderna con colores pastel
- 📱 **RecyclerView**: Lista de tareas eficiente y fluida
- ⚡ **Actualización en Tiempo Real**: Los cambios se reflejan instantáneamente
- ✏️ **Validación de Campos**: Validación de título (mínimo 3 caracteres)

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
│   ├── MainActivity.java          # Actividad principal
│   ├── Tarea.java                 # Modelo de datos
│   ├── TareaAdapter.java          # Adapter para RecyclerView
│   └── AdminSQLiteOpenHelper.java # Helper para SQLite (opcional)
│
├── src/main/res/layout/
│   ├── activity_main.xml          # Layout principal
│   ├── item_tarea.xml             # Item individual de tarea
│   └── dialog_tarea.xml           # Diálogo para crear/editar
│
└── src/main/res/values/
    ├── colors.xml                 # Paleta de colores pastel
    ├── strings.xml                # Recursos de texto
    └── themes.xml                 # Temas de la app
```

## 🎨 Componentes Desarrollados

### 1. **MainActivity.java**
- Inicializa RecyclerView con LinearLayoutManager
- Configura listener en tiempo real de Firebase Firestore
- Muestra diálogo para agregar nuevas tareas
- Valida campos antes de guardar
- Gestiona el ciclo de vida (onDestroy limpia listeners)

### 2. **Tarea.java**
- Modelo de datos con propiedades:
  - `id`: Identificador único del documento
  - `titulo`: Título de la tarea
  - `descripcion`: Descripción detallada
  - `completada`: Estado de completado (boolean)

### 3. **TareaAdapter.java**
- Extiende `RecyclerView.Adapter`
- Muestra lista de tareas en tarjetas (CardView)
- Botones de acción por cada tarea:
  - **Editar**: Abre diálogo con datos prellenados
  - **Eliminar**: Borra la tarea de Firestore
- CheckBox para visualizar estado de completado

### 4. **AdminSQLiteOpenHelper.java**
- Clase helper para base de datos local SQLite
- Crea tabla `tareas` con columnas: id, titulo, descripcion, estado
- *Nota: Actualmente no está integrada en la lógica principal*

### 5. **Layouts**
- **activity_main.xml**: Botón "Agregar tarea" + RecyclerView
- **item_tarea.xml**: CardView con título, descripción, checkbox y botones
- **dialog_tarea.xml**: Formulario con TextInputLayout para título y descripción

### 6. **Recursos Visuales**
- Paleta de colores pastel:
  - Rosa Pastel (`#F8C8DC`)
  - Lila Pastel (`#DCC6E0`)
  - Menta Pastel (`#C7EDE6`)

## 🔥 Configuración de Firebase

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Registra tu app Android con el package name: `com.example.todolistapp`
3. Descarga el archivo `google-services.json` y colócalo en `app/`
4. Habilita Firestore Database en modo prueba o configura reglas de seguridad
5. La colección `tareas` se creará automáticamente al agregar la primera tarea

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

## 📱 Capturas de Pantalla

La aplicación presenta:
- Interfaz limpia con fondo lila pastel
- Botón rosa pastel para agregar tareas
- Tarjetas blancas con sombra para cada tarea
- Diálogos Material Design para entrada de datos

## 🔮 Mejoras Futuras

- [ ] Implementar base de datos local SQLite con Room
- [ ] Modo offline con sincronización automática
- [ ] Autenticación de usuarios
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

**Estado del Proyecto**: ✅ Funcional - CRUD completo implementado con Firebase Firestore
