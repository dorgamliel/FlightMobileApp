package project.flightmobileapp

import android.content.Context
import androidx.room.*

@Entity(tableName = "ip_addresses_table")
data class IpAddresses (
    @ColumnInfo(name = "address")
    var address: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var addressId: Long = 0L
}

@Dao
interface IpDatabaseDao {
    @Insert
    fun insert(ip: IpAddresses)
    @Query("SELECT * from ip_addresses_table ORDER BY addressId DESC LIMIT 5")
    fun getLastFive(): List<IpAddresses>
    @Query(value = "SELECT * FROM ip_addresses_table WHERE address = :addr")
    fun getIP(addr: String): IpAddresses
    @Query("DELETE FROM ip_addresses_table")
    fun deleteAll()
    @Delete
    fun deleteIP(address: IpAddresses)
}

@Database(entities = [IpAddresses::class], version = 1, exportSchema = false)
abstract class IpDatabase : RoomDatabase() {
    abstract fun ipDatabaseDao(): IpDatabaseDao
    companion object {
        @Volatile
        private var INSTANCE: IpDatabase? = null
        private var lock = Any()
        operator fun invoke(context: Context) = INSTANCE ?:
        synchronized(lock) {
            INSTANCE
                ?: newDatabase(context)
                    .also { INSTANCE = it }
        }
    }
}

private fun newDatabase(context: Context): IpDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        IpDatabase::class.java,
        "ip_history_database")
        .allowMainThreadQueries().build()
}