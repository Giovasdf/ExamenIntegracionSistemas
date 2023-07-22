# REDAFI - Solución de Integración de Sistemas

![REDAFI Logo](https://github.com/Giovasdf/ExamenIntegracionSistemas/blob/Main/REDAFI.png)

Bienvenido al repositorio de la solución de integración de sistemas de REDAFI. En este repositorio, encontrarás la implementación de una solución basada en Apache Camel para recolectar datos financieros de múltiples fuentes y consolidarlos en una base de datos centralizada de REDAFI. Esta solución permite integrar diferentes sistemas que entregan datos en formatos distintos, como archivo plano, JSON, XML y API RESTful.

## Descripción General

REDAFI es una empresa de recolección de datos financieros que necesita obtener información de múltiples proveedores. Sin embargo, cada proveedor utiliza diferentes formatos y protocolos para entregar los datos. La solución de integración de sistemas de REDAFI, basada en Apache Camel, resuelve este desafío al permitir la conexión con cada fuente de datos, transformar los datos al formato requerido y almacenarlos en una base de datos centralizada.

## Características de la Solución

- Integración con múltiples fuentes de datos: La solución se conecta con cuatro fuentes de datos diferentes, cada una entregando datos en formatos distintos (archivo plano, JSON, XML y API RESTful).

- Transformación de datos: Utilizando patrones de integración, la solución transforma los datos recibidos a un formato común antes de almacenarlos en la base de datos centralizada de REDAFI.

- Complemento de datos: Si los datos recibidos están incompletos, la solución utiliza la base de datos interna de REDAFI para complementar la información faltante antes de almacenarla.

## Tecnologías Utilizadas

- Spring Boot: La solución está desarrollada utilizando el framework Spring Boot de Java, lo que permite crear aplicaciones Java listas para producción de manera rápida y sencilla.

- Apache Camel: Se ha utilizado Apache Camel, un framework de integración de código abierto, para conectar, enriquecer y transformar los datos entre las diferentes fuentes y sistemas.

- Pocketbase: La base de datos centralizada de REDAFI está implementada utilizando Pocketbase, una base de datos NoSQL flexible y escalable.

## Diagrama de la Solución

A continuación, se presenta un diagrama de la arquitectura de la solución de integración de sistemas utilizando Apache Camel:

![Diagrama de la Solución](https://github.com/Giovasdf/ExamenIntegracionSistemas/blob/Main/Diagrama2.png)

## Instalación y Uso

Para utilizar la solución de integración de sistemas de REDAFI, sigue los siguientes pasos:

1. Clona este repositorio en tu máquina local.

2. Asegúrate de tener instalado Java y Maven en tu sistema.

3. Ejecuta el comando `mvn spring-boot:run` en la raíz del proyecto para iniciar la aplicación.

4. La solución estará disponible en `http://localhost:8080` para recibir datos desde las fuentes de datos y realizar la integración.

## Contribuciones

¡Agradecemos las contribuciones a este proyecto! Si deseas colaborar, puedes realizar un fork del repositorio, crear una nueva rama para tus cambios y enviar un pull request.

## Recursos Adicionales

- [Enlace al Documento de Propuesta](https://www.example.com/documento-propuesta.pdf): Documento detallado de la propuesta de solución de integración de sistemas.

- [Enlace al Video de Ejemplo](https://www.youtube.com/watch?v=ejemplo): Video demostrativo del funcionamiento de la solución de integración.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

© 2023 REDAFI. Todos los derechos reservados.
