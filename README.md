# 🛒 E-Commerce Web Application – AWS Cloud Portfolio Project

A cloud-native e-commerce application built with Spring Boot, React, and MySQL, deployed on AWS to showcase scalable architecture, managed services, and hands-on cloud skills.
This project demonstrates practical implementation of AWS solutions aligned with my AWS Solutions Architect certification.

## 🌟 Key AWS Skills Demonstrated

- **Compute:** Deployed backend on EC2 instances using Linux AMI. Configured Elastic IPs for static access.
- **Storage:** React frontend hosted on S3 bucket with public access. Implemented public/private access policies.
- **Database:** Integrated RDS (MySQL) for highly available relational data storage.
- **Security & IAM:**
    - Configured IAM roles and policies for S3 access from the application.
    - Implemented JWT-based authentication for secure API access.
    - Networking: Configured VPC, security groups, and inbound/outbound rules for EC2 and RDS.
- **Deployment & DevOps Basics:**
    - Automated application deployment on EC2 using shell scripts.
    - Configured environment variables for DB credentials and API URLs.
    - Prepared for scalable deployment with stateless backend.

# 🛠 Project Features

### Customer Features

- Register/Login with secure JWT authentication
- Browse products by categories (Electronics, Footwear, Fashion, Clothing)
- Add products to cart, update quantities, and place orders
- Order history linked to customer accounts

### AWS Implementation Highlights

- **EC2 Instances:** Spring Boot API runs on EC2 instance with static Elastic IP
- **Elastic IP:** Ensures public access to the deployed application
- **S3 Storage:** React frontend hosted on S3 bucket
- **RDS (MySQL):** Relational database hosted on RDS for scalability and backup support
- **IAM Roles:** Secure access from EC2 to S3 bucket
- **Security Groups:** Configured to allow HTTP/HTTPS, restrict SSH access

# 📂 Architecture Diagram
```
+------------------+        +-----------------+       +--------------+
| React Frontend   | -----> | EC2 Backend App | -----> |  RDS MySQL   |
| (S3 + CloudFront)|        | Spring Boot API |       |              |
+------------------+        +-----------------+       +--------------+
         |                           |
         |                           |
         +------------------------> S3 Bucket (Product Images)
```

# ⚙️ Deployment Steps (AWS Focus)

1. **Setup EC2 Instances**
      - Launch Linux EC2 instances for backend
      - Assign Elastic IPs
      - Install Java, Git, and other dependencies

2. **Deploy Backend**
    - Clone repo and build Spring Boot backend
    - Configure application.properties for RDS database
    - Start backend service with systemd or nohup

3. **Deploy Frontend**
    - Build React app (npm run build)
    - Upload build files to S3 bucket
    - Configure CloudFront (optional) to serve frontend with HTTPS
    - Set backend API endpoints to Elastic IP

4. **Database**
    - Create RDS MySQL instance
    - Configure security group to allow backend EC2 to access RDS
    - Import initial schema and seed data

5. **S3 Bucket**
    - Upload product images
    - Configure IAM role for EC2 to access S3
    - Serve images in React frontend
      
6. **Security**
    - Configure security groups for HTTP/HTTPS and restricted SSH
    - Apply IAM roles and policies for S3 access

# 📦 Technology Stack
```
+------------+------------------------------------------------------+
| Layer	     |   Technology / Tool                                  |
+------------+------------------------------------------------------+
| Backend    |   Spring Boot, Java 1.8, JWT, Spring Security        |
+------------+------------------------------------------------------+
| Frontend	 |   React.js, HTML, CSS                                |
+------------+------------------------------------------------------+
| Database	 |   MySQL (RDS)                                        |
+------------+------------------------------------------------------+
| Cloud	     |   AWS EC2, S3, RDS, Elastic IP, IAM, Security Groups |
+------------+------------------------------------------------------+
| DevOps     |   Shell scripts for deployment                       |
+------------+------------------------------------------------------+
```

# 📌 Author

**Sohan Gurav - AWS Certified Cloud Practitioner | AWS Certified Solutions Architect**

**GitHub: [My Github](https://github.com/sohang05)**

**Linkedin: [My Linkedin](https://www.linkedin.com/in/sohan-gurav-b8789b1a5/)**  

**Email:** *sohan02@gmail.com*

# 🚀 Future Enhancements
- Add CI/CD pipeline with AWS CodePipeline or GitHub Actions
- Implement auto-scaling for backend EC2
- Integrate AWS CloudFront for CDN and improved frontend performance
- Add SNS/SQS for order notifications and decoupled architecture
- Implement encrypted S3 storage and RDS backups
