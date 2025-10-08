# Notes Android App

A simple notes manager with local SQLite (Room) storage. Features:
- Create, edit, delete notes
- List with search (debounced)
- Minimal modern light theme "Ocean Professional"

Tech stack:
- Kotlin, AndroidX
- Room, ViewModel + Flow
- Navigation, RecyclerView
- Material Components (no Compose)

Build requirements:
- JDK 17+
- Android SDK (compileSdk 34, minSdk 24)

Build and Run:
- From `android_frontend/`:
  - Unix: `./gradlew :app:assembleDebug`
  - Run on device/emulator from Android Studio or use `./gradlew :app:installDebug`

Database:
- Room v1 schema, destructive migrations fallback for simplicity.

Navigation:
- Routes: list (NotesListFragment), edit (NoteEditorFragment with optional noteId).

Theme:
- Primary #2563EB, Secondary #F59E0B, Error #EF4444, Background #f9fafb, Surface #ffffff, Text #111827.
