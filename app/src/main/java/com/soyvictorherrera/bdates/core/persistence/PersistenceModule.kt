package com.soyvictorherrera.bdates.core.persistence

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersistenceModule {

    @Binds
    abstract fun bindKeyValueStoreContract(keyValueStore: KeyValueStore): KeyValueStoreContract

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(application: Application): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                APP_DATABASE_NAME
            )
            .addCallback(object : androidx.room.RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Insert default circle on fresh install
                    db.execSQL("""
                        INSERT INTO circles (id, name, description, is_default_circle)
                        VALUES ('device-local', 'Device local circle', '', 1)
                    """)
                }
            })
            .addMigrations(MIGRATION_1_2)
            .build()
        }

        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL("CREATE TABLE IF NOT EXISTS `circles_new` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `is_default_circle` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                
                // Copy the data
                database.execSQL("INSERT INTO circles_new (id, name, description, is_default_circle) SELECT id, name, description, local_only FROM circles")
                
                // Remove the old table
                database.execSQL("DROP TABLE circles")
                
                // Rename the new table to the table name
                database.execSQL("ALTER TABLE circles_new RENAME TO circles")

                // Insert default circle if none exists
                database.execSQL("""
                    INSERT OR IGNORE INTO circles (id, name, description, is_default_circle)
                    VALUES ('device-local', 'Device local circle', '', 1)
                """)
            }
        }

        @Provides
        @Singleton
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return application.getSharedPreferences(
                APP_SHARED_PREFS_FILE_NAME,
                Context.MODE_PRIVATE
            )
        }
    }
}