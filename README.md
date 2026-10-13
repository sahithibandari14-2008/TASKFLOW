# TASKFLOW
A Java Swing desktop To-Do List application with task management and file handling.
# TaskFlow — Smart To-Do List Manager

TaskFlow is a Java Swing desktop application designed to help users organize daily tasks, set priorities and due dates, and track task completion.

## Features

- Add, edit, and delete tasks
- Mark tasks as completed or pending
- Assign task priorities
- Organize tasks by category
- Set due dates and identify overdue tasks
- Search tasks by name
- Filter tasks by status
- Sort tasks by priority or due date
- View task statistics
- Save tasks to local files and maintain a backup

## Technologies Used

- **Java** — application logic and task management
- **Java Swing** — graphical user interface (GUI)
- **Java File Handling** — persistent task storage
- **GitHub** — source code hosting and version control

## Project Structure

- `Main.java` — Java source code
- `TaskFlow.jar` — runnable application
- `RunTaskFlow.bat` — Windows launcher

## How to Run

### Option 1: Run the JAR

Install a compatible Java runtime, download `TaskFlow.jar`, open a terminal in the download folder, and run:

```bash
java -jar TaskFlow.jar
```

### Option 2: Windows Launcher

Download `TaskFlow.jar` and `RunTaskFlow.bat` into the same folder, then double-click `RunTaskFlow.bat`.

## Data Storage

TaskFlow stores tasks in local text files. The application uses file handling to save task information and retain data between sessions.

## Purpose

The project demonstrates Java programming concepts, object-oriented design, GUI development, event handling, and file handling through a practical desktop application.

## Future Improvements

- Add task reminders
- Add calendar integration
- Improve task export options
- Introduce a database for larger task collections

---

**Project:** TaskFlow  
**Type:** Java Desktop Application  
**Status:** Functional prototype
