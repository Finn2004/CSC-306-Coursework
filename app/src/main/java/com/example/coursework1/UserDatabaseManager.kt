package com.example.coursework1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

class UserDatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "APP_USERS"
        private const val DATABASE_VERSION = 27
        private const val FAILED = -1L
        private const val TABLE_USERS = "users"
        private const val TABLE_USER_INFO = "user_info"
        private const val TABLE_USER_GOALS = "user_goals"
        private const val TABLE_USER_HABITS = "user_habits"
        private const val TABLE_DAILY_CHALLENGES = "daily_challenges"
        private const val TABLE_WEEKLY_CHALLENGES = "weekly_challenges"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PRIVACY = "account_privacy"
        private const val COLUMN_DATE_CREATED = "date_created"
        private const val COLUMN_BIO = "bio"
        private const val COLUMN_PROFILE_PICTURE = "profile_picture"
        private const val COLUMN_FIRST_NAME = "firstname"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_DOB = "dob"
        private const val COLUMN_HEIGHT = "height"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_GOAL_DESCRIPTION = "goal_description"
        private const val COLUMN_GOAL_TARGET = "goal_target"
        private const val COLUMN_GOAL_PROGRESS = "goal_progress"
        private const val COLUMN_GOAL_METRIC = "goal_metric"
        private const val COLUMN_GOAL_COMPLETED = "goal_completed"
        private const val COLUMN_HABIT_NAME = "habit_name"
        private const val COLUMN_HABIT_PROGRESS = "habit_progress"
        private const val COLUMN_HABIT_MAX = "habit_max"
        private const val COLUMN_HABIT_METRIC = "habit_metric"
        private const val COLUMN_HABIT_ICON = "habit_icon"
        private const val COLUMN_CHALLENGE_TITLE = "challenge_title"
        private const val COLUMN_CHALLENGE_PROGRESS = "challenge_progress"
        private const val COLUMN_CHALLENGE_TARGET = "challenge_target"
        private const val COLUMN_CHALLENGE_METRIC = "challenge_metric"
        private const val COLUMN_CHALLENGE_XP = "challenge_xp"
        private const val COLUMN_CHALLENGE_HABIT = "challenge_habit"

    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable =
            "CREATE TABLE $TABLE_USERS($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_USERNAME TEXT UNIQUE NOT NULL," +
                    "$COLUMN_EMAIL TEXT UNIQUE NOT NULL," +
                    "$COLUMN_PASSWORD TEXT NOT NULL," +
                    "$COLUMN_PRIVACY TEXT NOT NULL," +
                    "$COLUMN_DATE_CREATED TEXT NOT NULL," +
                    "$COLUMN_BIO TEXT," +
                    "$COLUMN_PROFILE_PICTURE TEXT)"

        val createUserInfoTable =
            "CREATE TABLE $TABLE_USER_INFO($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_FIRST_NAME TEXT," +
                    "$COLUMN_SURNAME TEXT," +
                    "$COLUMN_DOB TEXT," +
                    "$COLUMN_HEIGHT INTEGER," +
                    "$COLUMN_WEIGHT INTEGER," +
                    "$COLUMN_USER_ID INTEGER," +
                    "FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE)"

        val createUserGoalsTable =
            "CREATE TABLE $TABLE_USER_GOALS($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_GOAL_DESCRIPTION TEXT," +
                    "$COLUMN_GOAL_TARGET INTEGER," +
                    "$COLUMN_GOAL_PROGRESS INTEGER," +
                    "$COLUMN_GOAL_METRIC TEXT," +
                    "$COLUMN_USER_ID INTEGER," +
                    "$COLUMN_GOAL_COMPLETED INTEGER," +
                    "FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE)"

        val createUserHabitsTable =
            "CREATE TABLE $TABLE_USER_HABITS($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_HABIT_NAME TEXT," +
                    "$COLUMN_HABIT_PROGRESS INTEGER," +
                    "$COLUMN_HABIT_MAX INTEGER," +
                    "$COLUMN_HABIT_METRIC TEXT," +
                    "$COLUMN_HABIT_ICON INTEGER," +
                    "$COLUMN_USER_ID INTEGER," +
                    "FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE)"

        val createDailyChallengesTable =
            "CREATE TABLE $TABLE_DAILY_CHALLENGES($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_CHALLENGE_TITLE TEXT," +
                    "$COLUMN_CHALLENGE_PROGRESS INTEGER," +
                    "$COLUMN_CHALLENGE_TARGET INTEGER," +
                    "$COLUMN_CHALLENGE_METRIC TEXT," +
                    "$COLUMN_CHALLENGE_XP INTEGER," +
                    "$COLUMN_CHALLENGE_HABIT TEXT)"

        val createWeeklyChallengesTable =
            "CREATE TABLE $TABLE_WEEKLY_CHALLENGES($COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_CHALLENGE_TITLE TEXT," +
                    "$COLUMN_CHALLENGE_PROGRESS INTEGER," +
                    "$COLUMN_CHALLENGE_TARGET INTEGER," +
                    "$COLUMN_CHALLENGE_METRIC TEXT," +
                    "$COLUMN_CHALLENGE_XP INTEGER," +
                    "$COLUMN_CHALLENGE_HABIT TEXT)"


        db.execSQL(createUserTable)
        db.execSQL(createUserInfoTable)
        db.execSQL(createUserGoalsTable)
        db.execSQL(createUserHabitsTable)
        db.execSQL(createDailyChallengesTable)
        db.execSQL(createWeeklyChallengesTable)

        setDailyChallenges(db)
        setWeeklyChallenges(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_GOALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_HABITS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DAILY_CHALLENGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEEKLY_CHALLENGES")
        onCreate(db)
    }

    fun setDailyChallenges(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Drink 5 Litres of water:', 0, 5000, 'ml', 15, 'Daily Water Intake')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Eat 7 fruits:', 0, 7, '', 15, '5-a-day')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Do Not Exceed 3500 kCal:', 0, 3500, 'kCal', 15, 'Daily Calorie Intake')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Walk a distance of 7 kilometres:', 0, 7000, 'm', 15, 'Walking Distance')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Cycle a distance of 25 miles:', 0, 25, 'miles', 15, 'Cycling Distance')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Sleep a total of 9 hours:', 0, 540, 'mins', 15, 'Sleep monitor')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Heavy workout for 2 hours:', 0, 120, 'mins', 15, 'Exercise Duration')")

        db.execSQL("INSERT INTO $TABLE_DAILY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Swim 10 pool lengths:', 0, 10, '', 15, 'Swimming Duration')")
    }

    fun setWeeklyChallenges(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Drink 40 Litres of water:', 0, 40000, 'ml', 50, 'Daily Water Intake')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Eat 50 fruits:', 0, 50, '', 50, '5-a-day')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Do Not Exceed 24,500 kCal:', 0, 24500, 'kCal', 50, 'Daily Calorie Intake')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Walk a distance of 45 kilometres:', 0, 45000, 'm', 50, 'Walking Distance')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Cycle a distance of 215 miles:', 0, 215, 'miles', 50, 'Cycling Distance')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Sleep a total of 62 hours:', 0, 62, 'hours', 50, 'Sleep monitor')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Workout for 25 hours:', 0, 25, 'hours', 50, 'Exercise Duration')")

        db.execSQL("INSERT INTO $TABLE_WEEKLY_CHALLENGES ($COLUMN_CHALLENGE_TITLE, $COLUMN_CHALLENGE_PROGRESS," +
                " $COLUMN_CHALLENGE_TARGET, $COLUMN_CHALLENGE_METRIC, $COLUMN_CHALLENGE_XP, $COLUMN_CHALLENGE_HABIT) " +
                "VALUES ('Swim 100 pool lengths:', 0, 100, '', 50, 'Swimming Duration')")
    }

    fun addUser(username: String, email: String, password: String) : Boolean {
        val passwordHashed = password.hashCode()
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, passwordHashed.toString())
            put(COLUMN_PRIVACY, "Public")
            put(COLUMN_DATE_CREATED, date)
            put(COLUMN_BIO, "")
            put(COLUMN_PROFILE_PICTURE, "")
        }

        val db = this.writableDatabase
        val inserted = db.insert(TABLE_USERS, null, values)
        return inserted != FAILED
    }

    fun addUserInfo(info: List<String>) : Boolean {
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, info[0])
            put(COLUMN_SURNAME, info[1])
            put(COLUMN_DOB, info[2])
            put(COLUMN_HEIGHT, info[3].toInt())
            put(COLUMN_WEIGHT, info[4].toInt())
            put(COLUMN_USER_ID, info[5].toInt())
        }

        val db = this.writableDatabase
        val inserted = db.insert(TABLE_USER_INFO, null, values)
        return inserted != FAILED
    }

    fun addUserGoal(goal: List<String>) : Boolean {
        val values = ContentValues().apply {
            put(COLUMN_GOAL_DESCRIPTION, goal[0])
            put(COLUMN_GOAL_TARGET, goal[1].toInt())
            put(COLUMN_GOAL_PROGRESS, 0)
            put(COLUMN_GOAL_METRIC, goal[2])
            put(COLUMN_GOAL_COMPLETED, 0)
            put(COLUMN_USER_ID, goal[3].toInt())
        }

        val db = this.writableDatabase
        val inserted = db.insert(TABLE_USER_GOALS, null, values)
        return inserted != FAILED
    }

    fun addUserHabit(habit: String, max: Int, metric: String, icon: Int, user: Int) : Boolean {
        val values = ContentValues().apply {
            put(COLUMN_HABIT_NAME, habit)
            put(COLUMN_HABIT_PROGRESS, 0)
            put(COLUMN_HABIT_MAX, max)
            put(COLUMN_HABIT_METRIC, metric)
            put(COLUMN_HABIT_ICON, icon)
            put(COLUMN_USER_ID, user)
        }

        val db = this.writableDatabase
        val inserted = db.insert(TABLE_USER_HABITS, null, values)
        return inserted != FAILED
    }

    fun verifyUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val passwordHashed = password.hashCode()
        val args = arrayOf(username, passwordHashed.toString())

        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", args)
        val found = cursor.count > 0

        cursor.close()
        return found
    }

    fun deleteUser(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteUserData(userID: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_USER_INFO, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun getUserIdByUsername(username: String?) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?", arrayOf(username))

        var userId = 0
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        return userId
    }

    fun getUserHabits(username: String?) : MutableList<MutableList<String>> {
        val userID = getUserIdByUsername(username)
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER_HABITS WHERE $COLUMN_USER_ID = ?", arrayOf(userID.toString()))

        val habits = mutableListOf<MutableList<String>>()
        val habit = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val habitText = cursor.getString(cursor.getColumnIndexOrThrow("habit_name"))
                habit.add(habitText)
                val habitProgress = cursor.getInt(cursor.getColumnIndexOrThrow("habit_progress"))
                habit.add(habitProgress.toString())
                val habitMax = cursor.getInt(cursor.getColumnIndexOrThrow("habit_max"))
                habit.add(habitMax.toString())
                val habitMetric = cursor.getString(cursor.getColumnIndexOrThrow("habit_metric"))
                habit.add(habitMetric)
                val habitIcon = cursor.getInt(cursor.getColumnIndexOrThrow("habit_icon"))
                habit.add(habitIcon.toString())

                val habitId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                habit.add(habitId.toString())

                habits.add(habit.toMutableList())
                habit.clear()

            } while (cursor.moveToNext())
        }

        cursor.close()
        return habits
    }

    fun getHabitProgress(habitId: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_HABIT_PROGRESS FROM $TABLE_USER_HABITS WHERE $COLUMN_ID = ?", arrayOf(habitId.toString()))

        var progress = 0
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(cursor.getColumnIndexOrThrow("habit_progress"))
        }
        cursor.close()
        return progress
    }

    fun getHabitMetric(habitId: Int) : String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_HABIT_METRIC FROM $TABLE_USER_HABITS WHERE $COLUMN_ID = ?", arrayOf(habitId.toString()))

        var metric = ""
        if (cursor.moveToFirst()) {
            metric = cursor.getString(cursor.getColumnIndexOrThrow("habit_metric"))
        }
        cursor.close()

        if (metric == "none") {
            metric = ""
        }

        return metric
    }

    fun updateHabitProgress(habitId: Int, progress: Int) {
        val newProgress = progress + getHabitProgress(habitId)
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_HABIT_PROGRESS, newProgress)
        }

        db.update(TABLE_USER_HABITS, values, "$COLUMN_ID = ?", arrayOf(habitId.toString()))
    }

    fun habitForUser(userID: Int, habit: String) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_USER_HABITS WHERE $COLUMN_USER_ID = ? AND $COLUMN_HABIT_NAME = ?", arrayOf(userID.toString(), habit))

        var result = 0
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0)
        }
        val found = result > 0

        cursor.close()
        return found
    }

    fun removeHabitIfPresent(userID: Int, habit: String) {
        val db = this.writableDatabase
        if (habitForUser(userID, habit)) {
            db.delete(TABLE_USER_HABITS, "$COLUMN_USER_ID = ? AND $COLUMN_HABIT_NAME = ?", arrayOf(userID.toString(), habit))
        }

    }

    fun addHabitIfNotPresent(userID: Int, habit: String) {

        if (!habitForUser(userID, habit)) {
            val info = getDefaultHabitInfo(habit)

            addUserHabit(habit, info[0].toInt(), info[1], info[2].toInt(), userID)
        }
    }

    fun getDefaultHabitInfo(habit: String) : List<String> {
        val defaultInfo = mutableListOf<String>()

        var habitMax = 0
        var habitMetric = "none"
        var habitIcon = R.drawable.ic_account

        when (habit) {

            "Daily Water Intake" -> {
                habitMax = 2500
                habitMetric = "ml"
                habitIcon = R.drawable.ic_water
            }
            "5-a-day" -> {
                habitMax = 5
                habitMetric = "none"
                habitIcon = R.drawable.ic_5_a_day
            }
            "Daily Calorie Intake" -> {
                habitMax = 2250
                habitMetric = "kCal"
                habitIcon = R.drawable.ic_calorie
            }
            "Walking Distance" -> {
                habitMax = 10000
                habitMetric = "steps"
                habitIcon = R.drawable.ic_walking
            }
            "Cycling Distance" -> {
                habitMax = 12
                habitMetric = "miles"
                habitIcon = R.drawable.ic_cycle
            }
            "Sleep monitor" -> {
                habitMax = 8
                habitMetric = "hours"
                habitIcon = R.drawable.ic_sleep
            }
            "Exercise Duration" -> {
                habitMax = 45
                habitMetric = "minutes"
                habitIcon = R.drawable.ic_exercise
            }
            "Swimming Duration" -> {
                habitMax = 2000
                habitMetric = "metres"
                habitIcon = R.drawable.ic_swim
            }
            else -> {}
        }

        defaultInfo.add(habitMax.toString())
        defaultInfo.add(habitMetric)
        defaultInfo.add(habitIcon.toString())
        return defaultInfo
    }

    fun getHabitByNameAndID(habit: String, userID: Int) : List<String> {
        val db = this.readableDatabase
        val habitList = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER_HABITS WHERE $COLUMN_USER_ID = ? AND $COLUMN_HABIT_NAME = ?", arrayOf(userID.toString(), habit))

        if (cursor.moveToFirst()) {
            val habitProgress = cursor.getInt(cursor.getColumnIndexOrThrow("habit_progress"))
            habitList.add(habitProgress.toString())
            val habitMax = cursor.getInt(cursor.getColumnIndexOrThrow("habit_max"))
            habitList.add(habitMax.toString())
            val habitMetric = cursor.getString(cursor.getColumnIndexOrThrow("habit_metric"))
            habitList.add(habitMetric)
            val habitIcon = cursor.getInt(cursor.getColumnIndexOrThrow("habit_icon"))
            habitList.add(habitIcon.toString())
            val habitId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            habitList.add(habitId.toString())
        }

        cursor.close()
        return habitList
    }

    fun getUserGoals(userID: Int) : MutableList<MutableList<String>> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER_GOALS WHERE $COLUMN_USER_ID = ?", arrayOf(userID.toString()))

        val goals = mutableListOf<MutableList<String>>()
        val goal = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val goalText = cursor.getString(cursor.getColumnIndexOrThrow("goal_description"))
                goal.add(goalText)
                val goalProgress = cursor.getInt(cursor.getColumnIndexOrThrow("goal_progress"))
                goal.add(goalProgress.toString())
                val goalMax = cursor.getInt(cursor.getColumnIndexOrThrow("goal_target"))
                goal.add(goalMax.toString())
                val goalMetric = cursor.getString(cursor.getColumnIndexOrThrow("goal_metric"))
                goal.add(goalMetric)
                val goalId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                goal.add(goalId.toString())

                goals.add(goal.toMutableList())
                goal.clear()

            } while (cursor.moveToNext())
        }

        cursor.close()
        return goals
    }

    fun updateGoalProgress(goalId: Int, progress: Int) {
        val newProgress = progress + getGoalProgress(goalId)
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_GOAL_PROGRESS, newProgress)
        }

        db.update(TABLE_USER_GOALS, values, "$COLUMN_ID = ?", arrayOf(goalId.toString()))
    }

    fun getGoalProgress(goalId: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_GOAL_PROGRESS FROM $TABLE_USER_GOALS WHERE $COLUMN_ID = ?", arrayOf(goalId.toString()))

        var progress = 0
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(cursor.getColumnIndexOrThrow("goal_progress"))
        }
        cursor.close()
        return progress
    }

    fun getDailyChallenges(habitName: String) : MutableList<MutableList<String>> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_DAILY_CHALLENGES WHERE $COLUMN_CHALLENGE_HABIT = ?", arrayOf(habitName))

        val challenges = mutableListOf<MutableList<String>>()
        val challenge = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val challengeText = cursor.getString(cursor.getColumnIndexOrThrow("challenge_title"))
                challenge.add(challengeText)
                val challengeProgress = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_progress"))
                challenge.add(challengeProgress.toString())
                val challengeMax = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_target"))
                challenge.add(challengeMax.toString())
                val challengeMetric = cursor.getString(cursor.getColumnIndexOrThrow("challenge_metric"))
                challenge.add(challengeMetric)
                val challengeXP = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_xp"))
                challenge.add(challengeXP.toString())
                val challengeHabit = cursor.getString(cursor.getColumnIndexOrThrow("challenge_habit"))
                challenge.add(challengeHabit.toString())

                val challengeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                challenge.add(challengeId.toString())

                challenges.add(challenge.toMutableList())
                challenge.clear()

            } while (cursor.moveToNext())
        }

        cursor.close()
        return challenges
    }

    fun updateDailyChallengeProgress(dailyId: Int, progress: Int) {
        val newProgress = progress + getDailyChallengeProgress(dailyId)
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_CHALLENGE_PROGRESS, newProgress)
        }

        db.update(TABLE_DAILY_CHALLENGES, values, "$COLUMN_ID = ?", arrayOf(dailyId.toString()))
    }

    fun getDailyChallengeProgress(dailyId: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CHALLENGE_PROGRESS FROM $TABLE_DAILY_CHALLENGES WHERE $COLUMN_ID = ?", arrayOf(dailyId.toString()))

        var progress = 0
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_progress"))
        }
        cursor.close()
        return progress
    }

    fun getWeeklyChallenges(habitName: String) : MutableList<MutableList<String>> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_WEEKLY_CHALLENGES WHERE $COLUMN_CHALLENGE_HABIT = ?", arrayOf(habitName))

        val challenges = mutableListOf<MutableList<String>>()
        val challenge = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val challengeText = cursor.getString(cursor.getColumnIndexOrThrow("challenge_title"))
                challenge.add(challengeText)
                val challengeProgress = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_progress"))
                challenge.add(challengeProgress.toString())
                val challengeMax = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_target"))
                challenge.add(challengeMax.toString())
                val challengeMetric = cursor.getString(cursor.getColumnIndexOrThrow("challenge_metric"))
                challenge.add(challengeMetric)
                val challengeXP = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_xp"))
                challenge.add(challengeXP.toString())
                val challengeHabit = cursor.getString(cursor.getColumnIndexOrThrow("challenge_habit"))
                challenge.add(challengeHabit.toString())

                val challengeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                challenge.add(challengeId.toString())

                challenges.add(challenge.toMutableList())
                challenge.clear()

            } while (cursor.moveToNext())
        }

        cursor.close()
        return challenges
    }

    fun updateWeeklyChallengeProgress(weeklyId: Int, progress: Int) {
        val newProgress = progress + getWeeklyChallengeProgress(weeklyId)
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_CHALLENGE_PROGRESS, newProgress)
        }

        db.update(TABLE_WEEKLY_CHALLENGES, values, "$COLUMN_ID = ?", arrayOf(weeklyId.toString()))
    }

    fun getWeeklyChallengeProgress(weeklyId: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CHALLENGE_PROGRESS FROM $TABLE_WEEKLY_CHALLENGES WHERE $COLUMN_ID = ?", arrayOf(weeklyId.toString()))

        var progress = 0
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_progress"))
        }
        cursor.close()
        return progress
    }

    fun getUserPrivacySetting(userID: Int) : String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_PRIVACY FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userID.toString()))

        var privacy = ""
        if (cursor.moveToFirst()) {
            privacy = cursor.getString(cursor.getColumnIndexOrThrow("account_privacy"))
        }
        cursor.close()
        return privacy
    }

    fun updateUserPrivacySetting(userID: Int, currentSetting: String) {
        val db = this.writableDatabase
        var newSetting = ""
        if (currentSetting == "Private") {
            newSetting = "Public"
        }
        else {
            newSetting = "Private"
        }

        val values = ContentValues().apply {
            put(COLUMN_PRIVACY, newSetting)
        }

        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun verifyChanges(userID: Int, email: String, password: String) : Boolean {
        val db = this.readableDatabase
        val passwordHashed = password.hashCode()
        val args = arrayOf(userID.toString())

        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_ID = ?", args)

        var verified = false
        if (cursor.moveToFirst()) {
            val savedEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val savedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))

            if (email == savedEmail && passwordHashed.toString() == savedPassword) {
                verified = true
            }
        }

        cursor.close()
        return verified
    }

    fun updateUserEmail(userID: Int, email: String) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
        }

        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun updateUserPassword(userID: Int, password: String) {
        val db = this.writableDatabase
        val passwordHashed = password.hashCode()

        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, passwordHashed)
        }

        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun getUserJoinDate(userID: Int) : String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_DATE_CREATED FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userID.toString()))

        var date = ""
        if (cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndexOrThrow("date_created"))
        }
        cursor.close()
        return date
    }

    fun getUserBio(userID: Int) : String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_BIO FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userID.toString()))

        var bio = ""
        if (cursor.moveToFirst()) {
            bio = cursor.getString(cursor.getColumnIndexOrThrow("bio"))
        }
        cursor.close()
        return bio
    }

    fun updateUserBio(userID: Int, bio: String) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_BIO, bio)
        }

        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun updateUserProfilePicture(userID: Int, uri: Uri?) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_PROFILE_PICTURE, uri?.toString())
        }

        db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userID.toString()))
    }

    fun getUserProfilePicture(userID: Int) : Uri? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_PROFILE_PICTURE FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userID.toString()))

        var uri: Uri? = null
        if (cursor.moveToFirst()) {
            val uriString = cursor.getString(0)
            if (uriString != "") {
                uri = uriString.toUri()
            }
        }
        cursor.close()
        return uri
    }

    fun completeGoal(goalID: Int) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_GOAL_PROGRESS, getGoalMax(goalID))
            put(COLUMN_GOAL_COMPLETED, 1)
        }

        db.update(TABLE_USER_GOALS, values, "$COLUMN_ID = ?", arrayOf(goalID.toString()))
    }

    fun getGoalMax(goalID: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_GOAL_TARGET FROM $TABLE_USER_GOALS WHERE $COLUMN_ID = ?", arrayOf(goalID.toString()))

        var max = 0
        if (cursor.moveToFirst()) {
            max = cursor.getInt(0)
        }
        cursor.close()
        return max
    }

    fun getGoalCompletedStatus(goalID : Int) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_GOAL_COMPLETED FROM $TABLE_USER_GOALS WHERE $COLUMN_ID = ?", arrayOf(goalID.toString()))

        var completedInt = 0
        if (cursor.moveToFirst()) {
            completedInt = cursor.getInt(0)
        }
        cursor.close()

        var completed = false
        if (completedInt == 1) {
            completed = true
        }
        return completed
    }
}