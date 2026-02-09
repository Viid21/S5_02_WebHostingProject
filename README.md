# 📘 **TuWebYa – Backend API (Spring WebFlux + JWT Security)**

## 📝 **Project Overview**
Este backend forma parte del proyecto **TuWebYa**, una aplicación real desarrollada para gestionar clientes, formularios y proyectos dentro de un flujo profesional de creación de páginas web.

La API está construida con **Spring WebFlux** y sigue una arquitectura **hexagonal (ports & adapters)**.  
Incluye un sistema completo de **autenticación y autorización con JWT**, gestión de usuarios, formularios de negocio y proyectos asignados.

El objetivo principal es ofrecer una API segura, escalable y reactiva que sirva como base para el frontend del proyecto.

---

# 🌐 **Repository**
👉 **Backend Repository:**  
[https://github.com/Viid21/S5_02_WebHostingProject.git](https://github.com/Viid21/S5_02_WebHostingProject.git)

👉 **Frontend Repository (conectado a esta API):**  
[https://github.com/Viid21/S5_02_WebHostingProject_frontend.git](https://github.com/Viid21/S5_02_WebHostingProject_frontend.git)

---

# 🏗️ **Architecture**

```
Frontend (React) → Backend API (Spring WebFlux) → MongoDB → Docker → Local Production
```

### **Key Features**
- 🔐 **Autenticación JWT** (access + refresh tokens)  
- 🧩 **Arquitectura Hexagonal** (domain → application → infra)  
- ⚡ **Spring WebFlux (reactivo)**  
- 🗄️ **MongoDB** como base de datos  
- 🧱 **Control de roles** (USER, ADMIN, SUPERADMIN)  
- 📄 **Gestión de formularios**  
- 📁 **Gestión de proyectos asignados al usuario**  
- 👤 **Gestión completa de usuarios**  

---

# 🔐 **Security & JWT Flow**

La API implementa un sistema de seguridad basado en:

- **JWT Access Token** → usado para autenticar cada request  
- **JWT Refresh Token** → permite obtener un nuevo access token  
- **JwtAuthenticationFilter** → intercepta cada request y valida el token  
- **SecurityContextService** → obtiene el usuario autenticado en cada endpoint  

Flujo básico:

```
1. Usuario se registra o inicia sesión
2. API devuelve accessToken + refreshToken
3. El frontend guarda ambos tokens
4. Cada request protegida incluye Authorization: Bearer <token>
5. Si expira, se llama a /auth/refresh
```

---

# 🚀 **Endpoints**

A continuación tienes TODOS los endpoints reales basados en tus controllers.

---

## 🔐 **AUTH – /auth**

### **POST /auth/register**
Registra un usuario y devuelve tokens JWT.

### **POST /auth/login**
Devuelve accessToken + refreshToken.

### **POST /auth/forgot-password**
Envía un email para recuperar contraseña.

### **POST /auth/refresh**
Genera un nuevo access token usando el refresh token.

### **GET /auth/validate**
Valida si el token actual es válido.

---

## 📝 **FORMS – /forms**

### **POST /forms/submit**
Envía un formulario público.

### **GET /forms/check/{email}**
Comprueba si existe un formulario asociado a un email.

### **GET /forms/exists**
Comprueba si el usuario autenticado ya tiene formulario.

### **POST /forms/create**
Crea un formulario para el usuario autenticado.

### **PUT /forms/update**
Actualiza el formulario del usuario autenticado.

### **PUT /forms/{formId}/assign-admin/{adminId}**
Asigna un administrador a un formulario (solo roles altos).

### **GET /forms/mine**
Obtiene el formulario del usuario autenticado.

---

## 📁 **PROJECTS – /projects**

### **GET /projects/mine**
Devuelve el proyecto asignado al usuario autenticado.

---

## 👤 **USERS – /user**

### **POST /user/new**
Crea un usuario (solo roles altos).

### **GET /user/me**
Devuelve los datos del usuario autenticado.

### **GET /user**
Devuelve todos los usuarios (según rol).

### **GET /user/{id}**
Devuelve un usuario por ID.

### **PUT /user/{id}**
Actualiza datos del usuario.

### **PUT /user/{id}/password**
Actualiza la contraseña del usuario.

### **DELETE /user/{id}**
Elimina un usuario.

### **PUT /user/{id}/role**
Cambia el rol de un usuario (solo superadmin).

---
# 🐳 **Docker Setup (Backend + PostgreSQL + MongoDB)**

Este proyecto se ejecuta completamente mediante Docker usando un `docker-compose.yml` que levanta:

- **PostgreSQL 16** → Base de datos de usuarios  
- **MongoDB 7** → Formularios y proyectos  
- **Backend Spring WebFlux** → API principal  

A continuación tienes la guía exacta basada en tu configuración real.

---

## ▶️ **1. Levantar toda la infraestructura**

Desde la raíz del backend:

```bash
docker compose up -d
```

Esto levantará automáticamente:

| Servicio | Contenedor | Puerto | Descripción |
|---------|------------|--------|-------------|
| PostgreSQL | `tuwebya-users-db` | 5432 | Base de datos de usuarios |
| MongoDB | `tuwebya-mongodb` | 27017 | Base de datos de formularios y proyectos |
| Backend | `tuwebya-backend` | 8080 | API Spring WebFlux |

---

## 🧱 **2. Estructura real del docker-compose**

Tu `docker-compose.yml` define exactamente esto:

### **PostgreSQL**
```yaml
db:
  image: postgres:16
  container_name: tuwebya-users-db
  environment:
    POSTGRES_USER: vid
    POSTGRES_PASSWORD: Estegosaurio9000
    POSTGRES_DB: tuwebya-users
  ports:
    - "5432:5432"
  volumes:
    - postgres_data:/var/lib/postgresql/data
  networks:
    - tuwebya-net
```

### **MongoDB**
```yaml
mongodb:
  image: mongo:7
  container_name: tuwebya-mongodb
  ports:
    - "27017:27017"
  volumes:
    - mongodb_data:/data/db
  networks:
    - tuwebya-net
```

### **Backend**
```yaml
backend:
  build: .
  container_name: tuwebya-backend
  depends_on:
    - db
    - mongodb
  environment:
    SPRING_R2DBC_URL: r2dbc:postgresql://db:5432/tuwebya-users
    SPRING_R2DBC_USERNAME: vid
    SPRING_R2DBC_PASSWORD: Estegosaurio9000
    SPRING_DATA_MONGODB_URI: mongodb://mongodb:27017/tuwebya
    SECURITY_JWT_SECRET: "aP9xL2mS8vQ4tZ7rC6bN1kH3yW5uE0fG"
  ports:
    - "8080:8080"
  networks:
    - tuwebya-net
```

---

## 🔑 **3. Variables de entorno reales**

Estas son las variables que tu backend necesita para funcionar:

| Variable | Descripción |
|---------|-------------|
| `SPRING_R2DBC_URL` | Conexión reactiva a PostgreSQL |
| `SPRING_R2DBC_USERNAME` | Usuario de PostgreSQL |
| `SPRING_R2DBC_PASSWORD` | Contraseña de PostgreSQL |
| `SPRING_DATA_MONGODB_URI` | Conexión a MongoDB |
| `SECURITY_JWT_SECRET` | Clave secreta para firmar JWT |

Ejemplo real (el que ya usas):

```env
SPRING_R2DBC_URL=r2dbc:postgresql://db:5432/tuwebya-users
SPRING_R2DBC_USERNAME=vid
SPRING_R2DBC_PASSWORD=Estegosaurio9000
SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/tuwebya
SECURITY_JWT_SECRET=aP9xL2mS8vQ4tZ7rC6bN1kH3yW5uE0fG
```

---

## 🛠️ **4. Dockerfile del backend (refactorizado para README)**

Si tu `Dockerfile` es el típico de Spring Boot, debería verse así:

```dockerfile
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

*(Si tu Dockerfile real es distinto, me lo pasas y lo ajusto.)*

---

## ▶️ **5. Levantar el backend manualmente (sin Docker)**

Si quieres arrancarlo sin contenedores:

```bash
./mvnw spring-boot:run
```

Asegúrate de tener MongoDB y PostgreSQL levantados.

---

## 🔗 **6. Conexión con el frontend**

El frontend se conecta a:

```
http://localhost:8080
```

En tu `.env` del frontend:

```env
VITE_API_URL=http://localhost:8080
```

---

# 🧪 **Testing**

Puedes probar la API con:

- Postman  
- Thunder Client  
- cURL  

Ejemplo:

```bash
curl -X GET http://localhost:8080/auth/validate \
  -H "Authorization: Bearer <token>"
```

---

# 🛠️ **Common Issues & Solutions**

### **1. Token inválido o expirado**
Solución: llamar a `/auth/refresh`.

### **2. CORS entre frontend y backend**
Configurar `CorsGlobalConfig.java`.

### **3. MongoDB no arranca**
Verificar que el contenedor está levantado:

```bash
docker ps
```

---

# 📎 **Frontend Connection**

El frontend se conecta a esta API mediante:

```
VITE_API_URL=http://localhost:8080
```

Repositorio del frontend:

👉 [https://github.com/Viid21/S5_02_WebHostingProject_frontend.git](https://github.com/Viid21/S5_02_WebHostingProject_frontend.git)

---

# ✅ **Conclusion**

Este backend implementa:

- Seguridad JWT  
- Arquitectura hexagonal  
- Endpoints reactivos con WebFlux  
- Gestión de usuarios, formularios y proyectos  
- Integración con MongoDB  
- Ejecución mediante Docker  

Una base sólida y profesional para el proyecto TuWebYa.
