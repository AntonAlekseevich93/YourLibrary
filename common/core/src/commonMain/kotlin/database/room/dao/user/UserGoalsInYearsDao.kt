package database.room.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import database.room.entities.user.UserGoalInYearEntity

@Dao
interface UserGoalsInYearsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserGoal(goals: UserGoalInYearEntity)

}