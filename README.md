# 🚀 KScope - Cloud-Powered Fleet Tracking System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.6-green.svg)](https://spring.io/projects/spring-boot)
[![Firebase](https://img.shields.io/badge/Firebase-Realtime%20Database-yellow.svg)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

> **Real-time fleet tracking and management system** with live telemetry, alerts, and cloud-powered analytics.

## 🌟 Features

### 🚗 **Real-Time Vehicle Simulation**
- **5 Simulated Vehicles** with realistic movement patterns
- **1-Second Updates** for live telemetry data
- **GPS Tracking** with latitude/longitude coordinates
- **Dynamic Speed Changes** (0-120 km/h)
- **Fuel Management** with consumption and refill logic
- **Route Progress** with multiple delivery routes

### ⚠️ **Smart Alerting System**
- **Overspeeding Alerts** (>80 km/h)
- **Low Fuel Warnings** (<15% fuel level)
- **Engine Failure Detection** (when fuel reaches 0%)
- **Route Deviation Alerts** (unexpected route changes)
- **Severity Levels**: CRITICAL, HIGH, MEDIUM, LOW

### 📊 **Live Dashboard**
- **Interactive Map** with Leaflet.js
- **Real-Time Vehicle Markers** with status indicators
- **Live Analytics** with fleet summary
- **Alert Monitoring** with severity color coding
- **Vehicle Cards** with detailed telemetry
- **Status Indicators** (Live/Updating/Error)

### ☁️ **Firebase Integration**
- **Realtime Database** for instant data sync
- **Cloud Storage** for telemetry history
- **Live Updates** across all connected devices
- **Offline Support** with demo mode fallback

### 📈 **Business Intelligence**
- **CSV Export** for telemetry data
- **Analytics Export** for fleet insights
- **Vehicle Details Export** for reporting
- **REST API** for third-party integrations

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend** | Java 17 + Spring Boot 3.2.6 | REST APIs, business logic |
| **Database** | Firebase Realtime Database | Cloud data storage |
| **Frontend** | HTML5 + Bootstrap + Leaflet.js | Interactive dashboard |
| **Build Tool** | Maven | Dependency management |
| **Containerization** | Docker + Docker Compose | Deployment |
| **Configuration** | dotenv-java | Environment management |

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Firebase Project** (optional, demo mode available)

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/kscope.git
cd kscope
```

### 2. Environment Setup
Create a `.env` file in the project root:
```env
FIREBASE_DATABASE_URL=https://your-project.firebaseio.com
FIREBASE_PROJECT_ID=your-project-id
```

**Note**: If Firebase is not configured, the application runs in **DEMO mode** with simulated data.

### 3. Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

### 4. Access the Application
- **Dashboard**: http://localhost:8080
- **API Status**: http://localhost:8080/api/vehicles/status
- **All Vehicles**: http://localhost:8080/api/vehicles

## 📡 API Endpoints

### Vehicle Management
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/vehicles` | GET | Get all vehicles |
| `/api/vehicles/{id}` | GET | Get specific vehicle |
| `/api/vehicles/{id}/alerts` | GET | Get vehicle alerts |

### Analytics
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/vehicles/analytics/summary` | GET | Fleet summary |
| `/api/vehicles/analytics/alerts` | GET | Alert breakdown |
| `/api/vehicles/status` | GET | System health |

### Data Export
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/vehicles/export/telemetry` | GET | Export telemetry CSV |
| `/api/vehicles/export/analytics` | GET | Export analytics CSV |
| `/api/vehicles/export/vehicle-details` | GET | Export vehicle details CSV |

## 🐳 Docker Deployment

### Using Docker Compose
```bash
# Build and run with Docker Compose
docker-compose up --build

# Run in background
docker-compose up -d
```

### Manual Docker Build
```bash
# Build Docker image
docker build -t kscope .

# Run container
docker run -p 8080:8080 kscope
```

## 🔧 Configuration

### Firebase Setup (Optional)
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Download service account key as `firebase-config.json`
3. Place in `src/main/resources/`
4. Update `.env` with your Firebase URL and Project ID

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `FIREBASE_DATABASE_URL` | Firebase Realtime Database URL | Demo mode |
| `FIREBASE_PROJECT_ID` | Firebase Project ID | Demo mode |
| `SERVER_PORT` | Application port | 8080 |

## 📊 Dashboard Features

### 🗺️ **Interactive Map**
- **Real-time vehicle markers** with status colors
- **Click to view** vehicle details
- **Auto-refresh** every 1 second
- **Zoom and pan** functionality

### 📈 **Live Analytics**
- **Fleet Summary**: Total vehicles, active alerts
- **Speed Distribution**: Average, max, min speeds
- **Fuel Status**: Low fuel vehicles count
- **Alert Breakdown**: By severity and type

### 🚨 **Alert Monitoring**
- **Real-time alerts** with severity indicators
- **Alert history** with timestamps
- **Acknowledgment status** tracking
- **Filter by vehicle** or alert type

### 📋 **Data Export**
- **Telemetry Export**: All vehicle data as CSV
- **Analytics Export**: Fleet insights as CSV
- **Vehicle Details**: Complete vehicle information
- **Download buttons** in dashboard

## 🧪 Testing

### Manual Testing
```bash
# Test API endpoints
curl http://localhost:8080/api/vehicles/status
curl http://localhost:8080/api/vehicles
curl http://localhost:8080/api/vehicles/analytics/summary

# Test CSV exports
curl -o telemetry.csv http://localhost:8080/api/vehicles/export/telemetry
curl -o analytics.csv http://localhost:8080/api/vehicles/export/analytics
```

### Automated Testing
```bash
# Run unit tests
mvn test

# Run with coverage
mvn jacoco:report
```

## 🐛 Troubleshooting

### Common Issues

#### Port 8080 Already in Use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <process_id> /F
```

#### Firebase Connection Issues
- Check `firebase-config.json` exists in `src/main/resources/`
- Verify `.env` file has correct Firebase URL
- Application will run in DEMO mode if Firebase is not configured

#### Maven Build Issues
```bash
# Clean and rebuild
mvn clean compile

# Update dependencies
mvn dependency:resolve
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Add unit tests for new features
- Update documentation for API changes
- Test with both Firebase and demo modes

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot** for the robust backend framework
- **Firebase** for real-time database capabilities
- **Leaflet.js** for interactive mapping
- **Bootstrap** for responsive UI components

## 📞 Support


- **Email**: m.kiruthikkumar@gmail.com

---

**Made with ❤️ for modern fleet management**

*Last updated: July 2024* 
