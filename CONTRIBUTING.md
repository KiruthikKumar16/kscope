# 🤝 Contributing to KScope

Thank you for your interest in contributing to KScope! This document provides guidelines and information for contributors.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Pull Request Process](#pull-request-process)
- [Reporting Bugs](#reporting-bugs)
- [Feature Requests](#feature-requests)

## 📜 Code of Conduct

This project and its participants are governed by our Code of Conduct. By participating, you are expected to uphold this code.

### Our Standards

- **Be respectful** of differing opinions and viewpoints
- **Be collaborative** and work together toward common goals
- **Be constructive** in feedback and criticism
- **Be inclusive** and welcoming to all contributors

## 🚀 How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates.

**Bug Report Template:**
```markdown
**Bug Description:**
A clear and concise description of the bug.

**Steps to Reproduce:**
1. Go to '...'
2. Click on '...'
3. Scroll down to '...'
4. See error

**Expected Behavior:**
What you expected to happen.

**Actual Behavior:**
What actually happened.

**Environment:**
- OS: [e.g. Windows 10, macOS, Linux]
- Java Version: [e.g. 17.0.2]
- Maven Version: [e.g. 3.8.6]
- Browser: [e.g. Chrome, Firefox, Safari]

**Additional Context:**
Any other context about the problem.
```

### 💡 Suggesting Enhancements

We welcome feature requests! Please use the enhancement template:

**Enhancement Template:**
```markdown
**Is your feature request related to a problem?**
A clear and concise description of what the problem is.

**Describe the solution you'd like:**
A clear and concise description of what you want to happen.

**Describe alternatives you've considered:**
A clear and concise description of any alternative solutions.

**Additional context:**
Add any other context or screenshots about the feature request.
```

### 🔧 Pull Requests

We love pull requests! Here's how to contribute:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

## 🛠️ Development Setup

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Git**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/kscope.git
   cd kscope
   ```

2. **Set up environment:**
   ```bash
   # Create .env file
   cp .env.example .env
   # Edit .env with your Firebase credentials (optional)
   ```

3. **Build the project:**
   ```bash
   mvn clean compile
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application:**
   - Dashboard: http://localhost:8080
   - API: http://localhost:8080/api/vehicles

## 📝 Coding Standards

### Java Code Style

- Follow **Google Java Style Guide**
- Use **4 spaces** for indentation
- Maximum **120 characters** per line
- Use **meaningful variable names**
- Add **comments** for complex logic

### File Naming

- **Classes**: PascalCase (e.g., `VehicleController`)
- **Methods**: camelCase (e.g., `getVehicleById`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_SPEED`)
- **Packages**: lowercase (e.g., `com.kfleetguard.controller`)

### Code Organization

```java
// 1. Package declaration
package com.kfleetguard.controller;

// 2. Imports (organized)
import org.springframework.web.bind.annotation.*;
import com.kfleetguard.model.Vehicle;
import com.kfleetguard.service.VehicleService;

// 3. Class documentation
/**
 * REST controller for vehicle operations.
 * Provides endpoints for vehicle management and analytics.
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    
    // 4. Dependencies
    private final VehicleService vehicleService;
    
    // 5. Constructor
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    // 6. Public methods
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
    
    // 7. Private helper methods
    private void validateVehicleId(String vehicleId) {
        // validation logic
    }
}
```

### Documentation

- **JavaDoc** for all public methods
- **README updates** for new features
- **API documentation** for new endpoints
- **Inline comments** for complex logic

## 🧪 Testing Guidelines

### Unit Tests

- Write tests for all new functionality
- Use **JUnit 5** and **Mockito**
- Aim for **80%+ code coverage**
- Test both **success** and **failure** scenarios

### Test Structure

```java
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    
    @Mock
    private FirebaseService firebaseService;
    
    @InjectMocks
    private VehicleService vehicleService;
    
    @Test
    @DisplayName("Should return all vehicles successfully")
    void shouldReturnAllVehicles() {
        // Given
        List<Vehicle> expectedVehicles = Arrays.asList(
            new Vehicle("truck-001", new Location(12.9716, 77.5946), new Telemetry()),
            new Vehicle("truck-002", new Location(12.9789, 77.6012), new Telemetry())
        );
        
        // When
        when(firebaseService.getDatabase()).thenReturn(mockDatabaseReference);
        List<Vehicle> actualVehicles = vehicleService.getAllVehicles();
        
        // Then
        assertThat(actualVehicles).hasSize(2);
        assertThat(actualVehicles).extracting("vehicleId")
            .containsExactly("truck-001", "truck-002");
    }
}
```

### Integration Tests

- Test **API endpoints** with `@SpringBootTest`
- Test **Firebase integration** (with test database)
- Test **end-to-end** workflows

### Manual Testing

- Test **dashboard functionality**
- Test **real-time updates**
- Test **CSV exports**
- Test **alert system**

## 🔄 Pull Request Process

### Before Submitting

1. **Ensure tests pass:**
   ```bash
   mvn clean test
   ```

2. **Check code style:**
   ```bash
   mvn checkstyle:check
   ```

3. **Update documentation:**
   - Update README.md if needed
   - Add JavaDoc for new methods
   - Update API documentation

4. **Test manually:**
   - Run the application
   - Test new features
   - Verify no regressions

### Pull Request Template

```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed
- [ ] All tests pass

## Checklist
- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] Any dependent changes have been merged and published

## Screenshots (if applicable)
Add screenshots to help explain your changes.

## Additional Notes
Any additional information or context.
```

### Review Process

1. **Automated checks** must pass
2. **Code review** by maintainers
3. **Manual testing** by reviewer
4. **Documentation review**
5. **Final approval** and merge

## 🐛 Reporting Bugs

### Before Reporting

1. **Check existing issues** for duplicates
2. **Test with latest version**
3. **Reproduce the issue** consistently
4. **Gather relevant information**

### Bug Report Information

- **Environment details** (OS, Java version, etc.)
- **Steps to reproduce**
- **Expected vs actual behavior**
- **Error messages and logs**
- **Screenshots or videos** (if applicable)

## 💡 Feature Requests

### Before Requesting

1. **Check existing features** for similar functionality
2. **Consider the scope** and complexity
3. **Think about use cases** and benefits
4. **Research similar implementations**

### Feature Request Information

- **Clear description** of the feature
- **Use cases** and benefits
- **Implementation suggestions** (if any)
- **Mockups or examples** (if applicable)

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [GitHub Flow](https://guides.github.com/introduction/flow/)

## 🙏 Recognition

Contributors will be recognized in:
- **README.md** contributors section
- **GitHub contributors** page
- **Release notes** for significant contributions

---

**Thank you for contributing to KScope! 🚀**

*This contributing guide is adapted from the [Contributor Covenant](https://www.contributor-covenant.org/).* 