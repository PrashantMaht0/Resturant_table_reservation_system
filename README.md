#  Restaurant Table Reservation System — The Spice India
### Author : Prashant Mahto (A00336051)
### email : A00336051@student.tus.ie
A simple desktop application (Java 21) for managing restaurant table reservations. The project demonstrates advanced OOP principles and core Java data-structure handling while using Java Swing for the GUI. (note!!! the name "The Spice India" is a fictional name used only for naming the project; the app is not being used by any resturant that goes by that name !!!)

## Key points
- Language: Java 21 (JDK 21 or higher required)
- UI: Java Swing
- Data storage: Fixed-size arrays (AbstractTable[], Reservation[]) 
- Architecture: Modified MVC for clear separation of concerns
- Build tools: None (no Maven/Gradle)

## Project structure (packages)
- model — Data entities
    - Reservation (record)
    - AbstractTable (abstract base)
    - TableType (enum)
- exception — Custom checked exception
    - TableNotAvailableExeception
- service — Business logic
    - ReservationManager (stores data in private arrays, implements ReservationService)
- ui — Presentation
    - Swing components (MainScreenGUI, ViewTablesGUI, dialogs, main app `ui.TableReservationApp`)

## Demonstrated concepts
- Fundamentals: Inheritance, Polymorphism (AbstractTable subclasses), Encapsulation (private arrays), Checked exception (TableNotAvailableExeception), Method overloading
- Advanced: Records (Reservation), Sealed types/Interfaces (Table), Lambdas (Predicate filtering), Defensive copying (returning copies of internal arrays)

## Build and run (example)
From project root:

- Compile (Unix/macOS):
```
javac -d out $(find . -name "*.java")
```

- Compile (Windows PowerShell):
```
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } | javac -d out @-
```

- Run:
```
java -cp out ui.TableReservationApp
```

Note: Ensure your JAVA_HOME points to JDK 21+.

## Design notes
- Uses fixed-size arrays to emphasize manual data management and defensive copying.
- Business rules are encapsulated inside ReservationManager; UI components only handle presentation and input.
- Custom checked exception enforces explicit handling of unavailable tables.

## Contributing / Extending
- Replace array storage with Collection types (ArrayList, Map) to scale capacity.
- Add persistence (file/DB) to keep reservations across runs.
- Improve UI with modern toolkits or modularize using MVC frameworks.
- Frameworks like JDBC or JSP or Servlet can be used to add features like database connectivity as currently it doesn't store data anywhere every reservation created during execution of program will get deleted after program is closed. 