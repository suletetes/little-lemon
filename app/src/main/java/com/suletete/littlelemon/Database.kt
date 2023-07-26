package com.suletete.littlelemon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "menu_items")
data class MenuItemRoom(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    fun getAll(): LiveData<List<MenuItemRoom>>

    @Insert
    suspend fun insertAll(vararg menuItems: MenuItemRoom)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun getItemCount(): Int

    @Query("DELETE FROM menu_items")
    suspend fun deleteAll()
}

@Database(entities = [MenuItemRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
