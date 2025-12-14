# rehab_backend

## Infrastructure Architecture

- AWS EC2 (Ubuntu)
- Docker & Docker Compose
- Spring Boot (Java 17)
- Redis (Docker Container)
- MySQL (AWS RDS)
- GitHub Actions (CI/CD)

### Deployment Flow
1. Developer pushes code to GitHub
2. GitHub Actions triggers CI/CD
3. EC2 pulls latest code via SSH
4. Spring Boot application is built
5. Docker images are built and deployed using Docker Compose
