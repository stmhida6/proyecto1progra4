# CDR Processor

## Descripción

Este proyecto es una aplicación Java que procesa registros de detalles de llamadas (CDR). Utiliza múltiples hilos para producir y consumir datos de CDR de manera concurrente.

## Características

- **Lenguajes y Tecnologías**:
    - Java
    - SQL
    - Maven

- **Componentes Principales**:
    - `BufferCompartido`: Un buffer compartido que utiliza una cola bloqueante para almacenar los elementos producidos y consumidos.
    - `CDRProducer`: Un productor que lee datos de archivos CSV y los coloca en el buffer.
    - `CDRConsumer`: Un consumidor que toma datos del buffer y los procesa.
    - `CDR`: Clase principal que inicia y coordina los hilos de productores y consumidores.

## Estructura del Proyecto
## Requisitos

- JDK 8 o superior


## Instalación

1. Clona el repositorio:
    ```sh
    git clone <URL_DEL_REPOSITORIO>
    ```
2. Navega al directorio del proyecto:
    ```sh
    cd cdrprocessor
    ```
3. Compila el proyecto con Maven:
    ```sh
    mvn clean install
    ```

## Uso

Para ejecutar la aplicación, usa el siguiente comando:
```sh
mvn exec:java -Dexec.mainClass="org.proyecto.cdrprocessor.CDR"