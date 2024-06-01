
---

# TodoAppTest

TodoAppTest is an Android application that allows users to manage their todo items. Users can create, view, update, and delete their todo items. This application follows a structured architecture for better maintainability and scalability.

## Features

- User Authentication (Login and Registration)
- Add new todo items
- View a list of todo items
- Mark todo items as completed
- Update existing todo items
- Delete todo items

### View

- **Activities**:
  - `LoginActivity`: Handles user authentication.
  - `ViewTodoActivity`: Displays the list of todo items and handles adding, updating, and deleting items.
  - `CreateTodoActivity`: Allows users to create a new todo item.
  - `EditTodoActivity`: Allows users to edit an existing todo item.

- **Layouts**:
  - `activity_view_todo.xml`: Defines the layout for viewing todo items.
  - `list_item_todo.xml`: Defines the layout for each item in the todo list, including a checkbox, text view, delete button, and update button.

### Controller

- **Adapters**:
  - `TodoAdapter`: Binds todo items to the `ListView` in `ViewTodoActivity` and handles delete and update actions through interfaces `OnDeleteButtonClickListener` and `OnUpdateButtonClickListener`.

- **Event Handling**: Activities and adapters handle various events like button clicks and list item interactions.

### Model

- **Database Helper**:
  - `DatabaseHelper`: Interacts with the SQLite database to perform CRUD operations, such as `getAllTodoItems()`, `deleteTodoItem()`, and `getTodoIdByTitle()`.

- **Data Models**:
  - `User`: Represents the user entity with attributes like `username` and `password`.

## Getting Started

### Prerequisites

- Android Studio
- Android SDK 33
- Minimum API level 21

### Installation

1. **Clone the repository**:
   `  git clone https://github.com/yourusername/TodoAppTest.git `

2. **Open the project in Android Studio**:
   - Open Android Studio.
   - Select `File > Open` and choose the cloned repository.

3. **Build the project**:
   - Let Android Studio download the required dependencies and build the project.

### Running the Application

1. **Connect your Android device** or start an emulator.
2. **Run the application**:
   - Click on the "Run" button in Android Studio or press `Shift + F10`.

### Usage

- **Login**: Enter your username and password to log in.
- **Register**: Create a new account if you don't have one.
- **View Todos**: See the list of your todo items.
- **Add Todo**: Click on "Add Todo" to create a new todo item.
- **Update Todo**: Click on the "Update" button to modify an existing todo item.
- **Delete Todo**: Click on the "Delete" button to remove a todo item.
- **Mark as Completed**: Use the checkbox to mark a todo item as completed.

## License

This project is licensed under the MIT License.

---
