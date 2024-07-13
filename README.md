# Food Recommendation System

## Overview
The Food Recommendation System is designed to enhance the dining experience for employees by allowing them to rate and provide feedback on menu items. This system will help chefs understand employee preferences and improve the overall quality of the menu. There are three user roles in the system: Chef, Admin, and Employee/User.

## Features

### Authentication and Authorization
- **Login Feature**: Employees can log in using their employee ID and name.
- **Role-Based Access Control**: Access to APIs is restricted based on the assigned role.

### Menu Management
- **Admin Role**: 
  - Add, update, and delete menu items.
  - Manage prices and availability of menu items.

### Food Recommendation

#### Role: Chef
- **Menu Roll Out**:
  - Chef will roll out X items for breakfast, lunch, and dinner.
  - Menu is rolled out one day before (n-1).
  - X items are shown to employees for voting.
  - Employees choose their preferred items by the end of the day (EOD).
  - On the nth day, the cafe owner prepares food according to the voting results.
- **Food Attributes**:
  - Consumer comments.
  - Consumer ratings.
  - Date of provided feedback.
- **Recommendation Engine**:
  - Displays food ratings and comments to users.
  - Ensures recommendations are based on previous feedback.

#### Role: Employee
- **Feedback**:
  - Employees can give feedback on any food item from the menu, including breakfast, lunch, and dinner.

### Notifications
- **Real-Time Notifications**:
  - Sent through socket.
  - Notify for next-day food recommendations.
  - Alert about any new food items.
  - Inform about the availability status of any food item.

## Getting Started
To get started with the Food Recommendation System, follow the steps below:

1. **Installation**: Instructions on how to set up the project.
2. **Configuration**: Configuration settings for different environments.
3. **Running the Application**: Steps to run the application locally.

## License
This project is licensed to Rakshita Agarwal.

