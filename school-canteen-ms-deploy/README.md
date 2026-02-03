# School Canteen Management System Deployment

## Overview
The School Canteen Management System (SCMS) is a web-based application designed to streamline the management of school canteens. This project provides a structured approach to deploying the application on a Windows Server environment.

## Project Structure
The project is organized into several directories, each serving a specific purpose:

- **scripts/**: Contains PowerShell scripts for deployment and maintenance.
  - `deploy.ps1`: Main deployment script.
  - `update_existing_install.ps1`: Updates an existing installation.
  - `helpers.ps1`: Contains helper functions.
  - `install-dependencies.ps1`: Installs necessary dependencies.

- **config/**: Configuration files for deployment.
  - `deploy.config.ps1`: Deployment-specific settings.
  - `env.sample.json`: Sample environment configuration.

- **sql/**: SQL scripts for database management.
  - `create_admin_table.sql`: Creates the admin table.
  - `force_reset_user.sql`: Resets the admin user.

- **tests/**: Contains tests for the deployment scripts.
  - `Deploy.Tests.ps1`: Tests for ensuring deployment works as expected.

- **tools/**: Documentation for any tools or utilities included in the project.
  - `readme.md`: Documentation for tools.

## Setup Instructions
1. **Clone the Repository**: 
   Clone the repository to your local machine using Git.

   ```bash
   git clone <repository-url>
   ```

2. **Install Dependencies**: 
   Navigate to the `scripts` directory and run the `install-dependencies.ps1` script to install any required dependencies.

   ```powershell
   cd scripts
   .\install-dependencies.ps1
   ```

3. **Configure Deployment**: 
   Edit the `config/deploy.config.ps1` file to set your deployment-specific settings.

4. **Deploy the Application**: 
   Run the `deploy.ps1` script to deploy the application.

   ```powershell
   .\deploy.ps1 -TomcatHome "<path-to-tomcat>" -MySQLRootPassword "<your-password>"
   ```

## Usage Guidelines
- Ensure that you have the necessary permissions to run PowerShell scripts on your system.
- Make sure that Java JDK 8 and MySQL are installed and properly configured.
- Review the `env.sample.json` file for environment variable structure and adjust as needed.

## Additional Information
For more details on specific scripts and their functionalities, refer to the individual script files in the `scripts` directory.