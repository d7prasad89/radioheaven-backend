# Architecture & Connection Diagrams

## 🐳 Docker Architecture

```
┌────────────────────────────────────────────────────────────┐
│                   Your Computer (Host)                      │
│  ┌──────────────────────────────────────────────────────┐  │
│  │           Docker (Virtualizer)                        │  │
│  │  ┌────────────────────────────────────────────────┐  │  │
│  │  │        Docker Network (radioheaven)            │  │  │
│  │  │                                                 │  │  │
│  │  │  ┌──────────────────┐  ┌─────────────────────┐ │  │  │
│  │  │  │  MySQL Container │  │   App Container    │ │  │  │
│  │  │  │ ┌──────────────┐ │  │ ┌───────────────────┐│ │  │  │
│  │  │  │ │ Port: 3306   │ │  │ │ Port: 8080        ││ │  │  │
│  │  │  │ │ Database:    │ │  │ │ Spring Boot       ││ │  │  │
│  │  │  │ │ radioheaven  │ │  │ │                   ││ │  │  │
│  │  │  │ │              │◄────┤ Connects via:     ││ │  │  │
│  │  │  │ │ User: root   │ │  │ mysql:3306        ││ │  │  │
│  │  │  │ │ Pass: pwd    │ │  │                   ││ │  │  │
│  │  │  │ └──────────────┘ │  │ Env variables:    ││ │  │  │
│  │  │  │                  │  │ DB_HOST=mysql    ││ │  │  │
│  │  │  └──────────────────┘  └───────────────────┘│ │  │  │
│  │  │                                                 │  │  │
│  │  └────────────────────────────────────────────────┘  │  │
│  │                          ▲                            │  │
│  │                          │ Port Mapping              │  │
│  │                    3306:3306  8080:8080             │  │
│  └──────────────────────────┼────────────────────────────┘  │
│                             │                                │
│  ┌──────────────────────────┼────────────────────────────┐  │
│  │  Host Machine            │                            │  │
│  │  localhost:3306 ◄────────┘                            │  │
│  │  localhost:8080 ◄────────┘                            │  │
│  │                                                       │  │
│  │  Browser/PostMan/curl                                │  │
│  │  → http://localhost:8080                             │  │
│  └───────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
```

## 📊 Configuration Flow

```
┌─────────────────────────┐
│  docker-compose.yml     │
├─────────────────────────┤
│ Services:               │
│ - mysql                 │
│ - app                   │
│                         │
│ Environment Variables:  │
│ DB_HOST=mysql           │
│ DB_PORT=3306            │
│ DB_USERNAME=radiouser   │
│ DB_PASSWORD=radiopass   │
└────────┬────────────────┘
         │
         │ Creates
         ▼
    ┌─────────────┐
    │ MySQL       │
    │ Container   │
    └────┬────────┘
         │
         │ Reads ├─ service name
         │       ├─ credentials
         │       └─ port
         ▼
┌────────────────────────────┐
│  application.properties    │
├────────────────────────────┤
│ url=${DB_HOST:localhost}   │
│ user=${DB_USERNAME:root}   │
│ password=${DB_PASSWORD:pw} │
└────────┬───────────────────┘
         │
         │ Used by
         ▼
   ┌────────────────┐
   │ Spring Boot    │
   │ Application    │
   └────────┬───────┘
            │
            │ Connects to
            ▼
      mysql:3306 (inside Docker network)
            or
      localhost:3306 (local development)
```

## 🔄 Request Flow

### Local Development (No Docker)
```
Client
  ↓ curl http://localhost:8080/api/...
  ↓
Spring Boot App (Running on host)
  ↓ SELECT * FROM songs;
  ↓ (JDBC Connection: jdbc:mysql://localhost:3306/radioheaven)
  ↓
MySQL Server (Running on host at localhost:3306)
  ↓
Response (JSON)
  ↓
Client
```

### Production (Docker Compose)
```
Client (Outside Docker)
  ↓ curl http://localhost:8080/api/...
  ↓ [Port Mapping: 8080:8080]
  ↓
┌─────────────────────────┐
│ Docker Network          │
│ ┌───────────────────────┤
│ │ Spring Boot Container │
│ │  :8080                │
│ │ ├─ DB_HOST=mysql      │
│ │ └─ DB_PORT=3306       │
│ │         ↓              │
│ │  mysql:3306           │
│ │ [Service Name]        │
│ │         ↓              │
│ │ MySQL Container       │
│ │  :3306                │
│ │ Database: radioheaven │
│ │ User: radiouser       │
│ └───────────────────────┤
└─────────────────────────┘
  ↓
Response
  ↓
Client
```

## 📝 Environment Variables Resolution

```
┌──────────────────────────────────────────────────────┐
│  application.properties:                             │
│  jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}  │
└────────────────┬─────────────────────────────────────┘
                 │
         ┌───────┴────────┬──────────┐
         │                │          │
    ┌────▼─────┐   ┌──────▼──┐   ┌──▼──────┐
    │ Check ENV │   │ Check   │   │ Use     │
    │ variable  │   │ default │   │ value   │
    │ DB_HOST   │   │ value   │   │         │
    └────┬─────┘   └────┬─────┘   └────┬────┘
         │              │             │
         │ Found?       │ Not found?   │ Default
         │              │             │
    ┌────▼──────────────▼──────────────▼────┐
    │ Local Dev:    DB_HOST = "localhost"    │
    │ Docker:       DB_HOST = "mysql"        │
    │ Production:   DB_HOST = "db.prod.com"  │
    └─────────────────────────────────────────┘
```

## 🔧 Component Interaction

```
                    Docker Compose
                          │
            ┌─────────────┬┴┬────────────┐
            │             │ │            │
       mysql  port  volume│ │ app  env  port
       image  3306        │ │ build  vars 8080
            │             │ │            │
    ┌───────▼─────┐  ┌────▼─▼────────────▼────┐
    │   MySQL     │  │   Spring Boot App      │
    │ • Database  │  │ • REST API             │
    │ • User mgmt │  │ • Firebase Auth        │
    │ • Tables    │  │ • S3 Integration       │
    │ • Passwords │  │ • Database Access      │
    └───────┬─────┘  └────┬───────────────────┘
            │             │
            │◄────────────┤
            │  JDBC       │
            │  Connection │
            │             │
       mysql:3306    DB_HOST=mysql
```

## 🚀 Startup Sequence

```
1. docker-compose up
   │
   ├─ Start MySQL container
   │  ├─ Initialize database (radioheaven)
   │  ├─ Create user (radiouser)
   │  └─ Wait for health check (mysqladmin ping)
   │
   ├─ Wait for MySQL health check (depends_on condition)
   │
   └─ Start Spring Boot App container
      ├─ Load docker-compose env variables
      ├─ Read application.properties
      ├─ Parse: jdbc:mysql://${DB_HOST:localhost}:...
      ├─ Substitute: jdbc:mysql://mysql:3306/...
      ├─ Create JDBC connection to mysql:3306
      ├─ Initialize Spring context
      ├─ Start embedded Tomcat on 0.0.0.0:8080
      └─ Ready to accept requests on localhost:8080
```

## 📋 Variable Resolution Example

### Local Development
```
application.properties: ${DB_HOST:localhost}
No environment variable set
→ Uses default: "localhost"
→ Connects to: jdbc:mysql://localhost:3306/radioheaven
→ Works: ✅
```

### Docker Compose
```
application.properties: ${DB_HOST:localhost}
Environment variable set: DB_HOST=mysql
→ Uses environment value: "mysql"
→ Connects to: jdbc:mysql://mysql:3306/radioheaven
→ Works: ✅ (mysql is Docker service name)
```

### Production Server
```
application.properties: ${DB_HOST:localhost}
Environment variable set: DB_HOST=db.prod.company.com
→ Uses environment value: "db.prod.company.com"
→ Connects to: jdbc:mysql://db.prod.company.com:3306/radioheaven
→ Works: ✅ (connects to remote RDS)
```

