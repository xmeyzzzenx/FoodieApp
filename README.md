# 🍽️ FoodieApp

Proyecto final PMDM + PSP -- 2º DAM\
Arquitectura MVVM + Room + Retrofit + Auth0

Aplicación Android nativa para descubrir recetas, planificar comidas
semanales y gestionar la lista de la compra.

------------------------------------------------------------------------

## 📱 Descripción

FoodieApp permite:

-   Explorar recetas desde la API de TheMealDB
-   Guardarlas como favoritas
-   Planificar comidas por día y tipo (desayuno, comida, cena)
-   Generar automáticamente la lista de la compra
-   Crear y editar recetas propias (CRUD completo)

------------------------------------------------------------------------

## 🛠️ Tecnologías utilizadas

-   **Jetpack Compose** -- UI declarativa sin XML\
-   **Navigation Compose** -- Navegación entre pantallas\
-   **MVVM + Clean Architecture** -- Separación en capas\
-   **Room** -- Base de datos local\
-   **Retrofit2 + Gson** -- Consumo de API REST\
-   **Auth0** -- Autenticación segura\
-   **Hilt** -- Inyección de dependencias\
-   **Coroutines + Flow** -- Operaciones asíncronas\
-   **Coil** -- Carga de imágenes\
-   **Material Design 3** -- Tema personalizado

------------------------------------------------------------------------

## 📋 Pantallas principales

| Pantalla | Funcionalidad |
|----------|---------------|
| Login | Autenticación con Auth0 |
| Home | Categorías y receta del día |
| Búsqueda | Buscar recetas por nombre |
| Detalle | Ingredientes, instrucciones, favorito, planificar |
| Favoritas | Recetas guardadas |
| Plan semanal | Organización por día y tipo de comida |
| Lista de compras | Ingredientes pendientes y completados |
| Mis recetas | CRUD de recetas creadas por el usuario |
| Formulario | Crear o editar receta con validación |

------------------------------------------------------------------------

## 🏗️ Arquitectura

Estructura basada en MVVM + separación por capas:

    app/
    ├── data/
    │   ├── local/
    │   ├── remote/
    │   └── repository/
    ├── domain/
    │   ├── model/
    │   └── usecase/
    ├── ui/
    │   ├── screens/
    │   ├── components/
    │   ├── navigation/
    │   ├── theme/
    │   └── viewmodel/
    └── di/

-   La UI solo muestra estado
-   La lógica está en ViewModel
-   El Repository decide si los datos vienen de API o Room
-   Operaciones asíncronas con Coroutines

------------------------------------------------------------------------

## 🗄️ Base de datos (Room)

### Entidades principales

-   **recipes** -- Recetas API y propias\
-   **meal_plans** -- Plan semanal\
-   **shopping_items** -- Lista de compra

Incluye operaciones CRUD y observación con Flow.

------------------------------------------------------------------------

## 🌐 API -- TheMealDB

Endpoints utilizados:

-   `GET search.php?s=` -- Buscar recetas
-   `GET lookup.php?i=` -- Detalle por ID
-   `GET categories.php` -- Categorías
-   `GET filter.php?c=` -- Recetas por categoría
-   `GET random.php` -- Receta aleatoria

Manejo de estados: - Loading - Success - Error

------------------------------------------------------------------------

## 🔐 Autenticación

Implementación con **Auth0**:

-   Login funcional
-   Logout seguro
-   Protección de rutas
-   Usuario identificado por ID
-   Datos separados por usuario (favoritos y plan no se mezclan)

------------------------------------------------------------------------

## ⚙️ Instalación

1.  Clonar repositorio git clone
    https://github.com/xmeyzzzenx/FoodieApp.git

2.  Abrir en Android Studio

3.  Ejecutar en emulador o dispositivo físico

4.  Pulsar ▶ Run

------------------------------------------------------------------------

## 👩‍💻 Autora

Ximena Meyzen Calderón\
2º DAM -- Proyecto Final PMDM + PSP
