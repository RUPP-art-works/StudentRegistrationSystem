fun main() {
    val students = mutableListOf<Student>()
    val courses = mutableListOf<Course>()
    val enrollments = mutableListOf<Enrollment>()

    while (true) {
        displayMenu()
        print("Your selection: ")
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> registerNewStudent(students)
            2 -> createNewCourse(courses)
            3 -> enrollStudentInCourse(students, courses, enrollments)
            4 -> viewCourseStudents(courses, students, enrollments)
            5 -> viewAllStudents(students)
            6 -> viewAllCourses(courses, enrollments)
            7 -> {
                println("Goodbye")
                return
            }
            else -> println("Invalid option. Please try again.")
        }
        println() // Adds a space for better readability after an action
    }
}

fun displayMenu() {
    println(
        """
        === Student Course Registration System ===
        1. Register new student
        2. Create new course
        3. Enroll student in course
        4. View students in a course
        5. View all students
        6. View all courses
        7. Exit
        
        HOW TO USE:
        - Type a number (1-7) to select an option
        - Press Enter to confirm your choice
        """.trimIndent()
    )
}

fun registerNewStudent(students: MutableList<Student>) {
    print("Enter student ID (e.g., ST001): ")
    val id = readlnOrNull() ?: ""

    if (students.any { it.id.equals(id, ignoreCase = true) }) {
        println("Error: A student with ID '$id' already exists.")
        return
    }

    print("Enter student name: ")
    val name = readlnOrNull() ?: ""
    print("Enter student email (optional): ")
    val email = readlnOrNull()
    print("Enter student major: ")
    val major = readlnOrNull() ?: ""

    if (id.isNotEmpty() && name.isNotEmpty() && major.isNotEmpty()) {
        students.add(Student(id, name, email.takeIf { !it.isNullOrBlank() }, major))
        println("Student registered successfully!")
    } else {
        println("Error: Student ID, name, and major cannot be empty.")
    }
}

fun createNewCourse(courses: MutableList<Course>) {
    print("Enter course ID (e.g., CS101): ")
    val courseId = readlnOrNull() ?: ""

    // === UPDATE: Check for duplicate course ID ===
    if (courses.any { it.courseId.equals(courseId, ignoreCase = true) }) {
        println("Error: A course with ID '$courseId' already exists.")
        return
    }

    print("Enter course name: ")
    val courseName = readlnOrNull() ?: ""
    print("Enter course credits: ")
    val credits = readlnOrNull()?.toIntOrNull()

    if (courseId.isNotEmpty() && courseName.isNotEmpty() && credits != null && credits > 0) {
        courses.add(Course(courseId, courseName, credits))
        println("Course created successfully!")
    } else {
        println("Error: Course ID and name must be provided, and credits must be a valid positive number.")
    }
}

fun enrollStudentInCourse(
    students: List<Student>,
    courses: List<Course>,
    enrollments: MutableList<Enrollment>
) {
    print("Enter student ID to enroll: ")
    val studentId = readlnOrNull() ?: ""
    print("Enter course ID to enroll in: ")
    val courseId = readlnOrNull() ?: ""

    val studentExists = students.any { it.id == studentId }
    val courseExists = courses.any { it.courseId == courseId }

    val isAlreadyEnrolled = enrollments.any { it.studentId == studentId && it.courseId == courseId }
    if (isAlreadyEnrolled) {
        println("Error: Student '$studentId' is already enrolled in course '$courseId'.")
        return
    }

    if (studentExists && courseExists) {
        enrollments.add(Enrollment(studentId, courseId))
        println("Enrollment successful!")
    } else {
        if (!studentExists) println("Error: Student with ID '$studentId' not found.")
        if (!courseExists) println("Error: Course with ID '$courseId' not found.")
    }
}

fun viewCourseStudents(
    courses: List<Course>,
    students: List<Student>,
    enrollments: List<Enrollment>
) {
    print("Enter course ID to view students: ")
    val courseId = readlnOrNull() ?: ""

    val course = courses.find { it.courseId.equals(courseId, ignoreCase = true) }
    if (course == null) {
        println("Error: Course with ID '$courseId' not found.")
        return
    }

    val enrolledStudentIds = enrollments.filter { it.courseId.equals(courseId, ignoreCase = true) }.map { it.studentId }

    if (enrolledStudentIds.isEmpty()) {
        println("No students enrolled in ${course.courseName} (${course.courseId}).")
        return
    }

    println("Students enrolled in ${course.courseName} (${course.courseId}):")
    enrolledStudentIds.forEach { studentId ->
        val student = students.find { it.id == studentId }
        student?.let {
            println("- ID: ${it.id}, Name: ${it.name}, Major: ${it.major}")
        }
    }
}

fun viewAllStudents(students: List<Student>) {
    if (students.isEmpty()) {
        println("No students registered yet.")
        return
    }
    println("=== All Registered Students ===")
    students.forEach {
        println("ID: ${it.id}, Name: ${it.name}, Email: ${it.email ?: "N/A"}, Major: ${it.major}")
    }
}

fun viewAllCourses(courses: List<Course>, enrollments: List<Enrollment>) {
    if (courses.isEmpty()) {
        println("No courses created yet.")
        return
    }
    println("=== All Available Courses ===")
    courses.forEach { course ->
        val enrollmentCount = enrollments.count { it.courseId == course.courseId }
        println("ID: ${course.courseId}, Name: ${course.courseName}, Credits: ${course.credits}, Students Enrolled: $enrollmentCount")
    }
}