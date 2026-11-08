# Individual In-Class Assignment - Jenkins Pipeline & GUI Application

## 1. Assignment Description
This repository contains the implementation of in-class tasks focused on setting up Continuous Integration (CI)
with Jenkins, building a JavaFX GUI application, and containerizing it with Docker.

**Key Deliverables:**
- Automated build, test, and package pipeline using Jenkinsfile.
- Integration of JavaFX GUI with Docker using X11 forwarding (Xming/VcXsrv).
- Unit and integration testing using JUnit 5.
- Docker containerization for simple distribution and environment isolation.

## 2. Technologies & Tools Used
- **Programming Language:** Java 21 (Eclipse Temurin)
- **Frameworks & Libraries:** JavaFX 21, JUnit 5
- **Build Tool:** Apache Maven
- **CI/CD:** Jenkins Pipeline
- **Containerization:** Docker (Multi-stage build)
- **GUI Display Server (Windows):** Xming / VcXsrv (X11 Forwarding)

## 3. Design Approach & Implementation Method
- **Architecture:** The application follows a modular structure built with JavaFX for the user interface.
- **Dockerization Strategy:** Uses a multi-stage Dockerfile (or host-built JAR deployment) to compile the code with Maven, package dependencies, install necessary X11 Linux libraries (`libx11-6`, `libgtk-3-0`), and output the UI through host display forwarding (`host.docker.internal:0.0`).
- **CI/CD Integration:** A `Jenkinsfile` automates the checkout, compilation, unit test execution, and Docker image build/tagging steps on every commit.

## 4. Testing & Quality Assurance Steps
### Test Scenarios & Results:
1. **Automated Unit Tests:**
    - Executed via `mvn test` / `mvn clean verify`.
    - Verified business logic methods and core class functionality.
    - **Result:** All test cases passed successfully.
2. **Docker Build Verification:**
    - Ensured fat/shaded JAR builds without errors and dependencies are correctly bundled.
3. **GUI Forwarding Manual Test:**
    - Verified that the JavaFX GUI window launches on Windows through Xming without crashes (`-Dprism.order=sw`).

## 5. How to Run

### Prerequisites:
- Java JDK 21
- Maven 3.9+
- Docker Desktop
- Xming (for Windows GUI rendering)

### Steps to Run Locally (via Maven):
```bash
cd task1
mvn clean package
java -jar target/task1-1.0-SNAPSHOT.jar
```
### Steps to Run via Docker:
1. Pull the Docker image from Docker Hub:
```bash
docker pull gregtish/inclass_tasks:latest
```
#### Note: Ensure that Xming is running on your Windows host to allow GUI forwarding.

2. Build the Docker image:
```bash
docker build -t gregtish/inclass_tasks:latest .
```
3. Run the container:
```bash
docker run --rm -it -e DISPLAY=host.docker.internal:0.0 gregtish/inclass_tasks:latest
```

## 6. Database Localization Approach 

### Chosen Approach: Translation Table
For database localization, adopted the **Translation Table** approach (as covered in the course localization strategies), which separates language-neutral base entities from their localized text representations.

#### Schema Implementation:
- **Base Table (`temperature_unit`)**: Stores non-localized structural attributes (e.g., `id`, `symbol`).
- **Translation Table (`unit_translation`)**: Stores multi-language attributes linked via foreign key (`unit_id`, `lang`, `name`, `description`).
- **Measurement History (`temperature_conversion`)**: Persists conversion records referencing the base unit IDs.

#### Justification (Pros & Cons Analysis):
1. **No Data Redundancy / Clean Normalization:** Non-textual data (such as symbols or relational IDs) is not duplicated across multiple rows or columns.
2. **Dynamic Language Scalability:** New languages (English, Swedish, Finnish, Japanese, Arabic) can be added instantly by inserting new rows into `unit_translation` without modifying existing database schemas or table architectures.
3. **Effective Indexing & Fallback:** Allows clean indexing on `(unit_id, lang)` and supports robust query fallback mechanisms (via SQL `COALESCE`) if a specific localized string is missing.
4. **Industry Standard:** Unlike the *Field* approach (which causes rapid column growth and errors if a column is missing) or the *Table* approach (which requires dynamic runtime table selection), the Translation Table approach represents maintainable enterprise design.