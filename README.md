# Simulación de Propagación de Virus en Tejido Celular

Proyecto de modelado y simulación visual de un autómata celular en JavaFX. Representa un tejido biológico bidimensional donde se estudia la dinámica de infección y recuperación celular a través de dos fases alternadas (Reproducción y Muerte), integrando estructuras de datos lineales implementadas desde cero (Pila y Lista Simplemente Enlazada).

---

## Estructura del Proyecto

* **`models/`**: Contiene la clase `MatrizTejido`, encargada del estado celular (10x10), la vecindad de Moore de 8 posiciones y las reglas biológicas de transición.
* **`structures/`**: Implementaciones dinámicas de estructuras de datos:
  * `PilaGeneraciones` / `NodoPila`: Estructura LIFO utilizada para almacenar copias profundas de cada generación y permitir el retroceso paso a paso (desapilar).
  * `ListaEnlazadaEstadisticas` / `NodoEstadistica`: Estructura lineal utilizada para llevar el historial cronológico de células sanas, infectadas y variaciones por turno.
* **`controllers/`**: 
  * `SimulacionController`: Administra la lógica de la simulación, el ciclo de fases y la persistencia en las estructuras.
  * `SimulacionViewController`: Maneja los eventos de la interfaz JavaFX, la actualización de colores de la matriz y el área de texto.
* **`views/`**: Archivo `simulacion-view.fxml` con la definición de la interfaz gráfica.

---

## Requisitos del Sistema

* **JDK:** Java 22 o superior.
* **Gestor de compilación:** Apache Maven (se incluye el ejecutable `mvnw` en el repositorio).

---

## Compilación y Ejecución

### Desde Terminal (Línea de comandos)

1. **Limpiar y compilar el proyecto:**
   * En Windows:
     ```powershell
     .\mvnw.cmd clean compile
     ```
   * En Linux / macOS:
     ```bash
     ./mvnw clean compile
     ```

2. **Ejecutar la aplicación:**
   * En Windows:
     ```powershell
     .\mvnw.cmd javafx:run
     ```
   * En Linux / macOS:
     ```bash
     ./mvnw javafx:run
     ```

### Desde el IDE (IntelliJ IDEA / Eclipse / VS Code)

1. Abrir el proyecto reconociendo el archivo `pom.xml`.
2. Esperar a que se descarguen las dependencias de OpenJFX.
3. Ejecutar directamente la clase `com.example.demovirus.Main` (funciona como lanzador principal sin requerir flags de módulos de JavaFX).

---

## Parámetros Configurables

Los parámetros clave de la simulación están definidos como constantes en la clase `MatrizTejido` (`src/main/java/com/example/demovirus/models/MatrizTejido.java`):

| Parámetro | Valor por defecto | Descripción |
| :--- | :---: | :--- |
| `TAMANIO` | `10` | Dimensión de la cuadrícula cuadrada (10x10 = 100 celdas). |
| `UMBRAL_MUERTE` | `5` | Número mínimo de vecinos infectados para que una célula infectada muera por saturación y vuelva a estar sana. |
| `CELDA_SANA` | `0` | Identificador numérico para células en estado sano (color gris claro en la vista). |
| `CELDA_INFECTADA` | `1` | Identificador numérico para células infectadas (color rojo en la vista). |

---

## Guía de Uso de la Simulación

1. **Definir el foco infeccioso inicial:** Al iniciar el programa, haz clic sobre cualquiera de las celdas de la matriz para infectarlas (cambiarán a color rojo).
2. **Iniciar Simulación:** Presiona el botón verde *"Iniciar Simulación"*. Esto guardará la Generación 0 (estado base) y bloqueará la edición directa de la cuadrícula.
3. **Siguiente Paso:** Avanza de turno ejecutando secuencialmente:
   * **Fase de Reproducción:** Cada célula infectada contagia a todas las células sanas adyacentes (hasta 8 vecinos, límites cerrados).
   * **Fase de Muerte:** Las células infectadas con 5 o más vecinos infectados se recuperan / mueren volviendo al estado sano.
4. **Anterior (Desapilar):** Desapila el estado inmediatamente anterior para retroceder en el tiempo manteniendo la coherencia de la pila y las estadísticas.
5. **Caso Especial (Tejido Sano):** Si en algún punto ya no quedan células infectadas en el tejido, la simulación lo notifica mediante un mensaje informativo y bloquea el avance innecesario.
6. **Reiniciar:** Restablece la matriz y las estructuras a un estado limpio permitiendo definir una nueva configuración inicial sin necesidad de reiniciar la aplicación.
7. **Historial Estadístico:** En la parte inferior se muestra un registro en tiempo real recorrido desde la lista enlazada con el detalle poblacional de cada turno (células sanas, infectadas, nuevas infectadas y nuevas sanas).

