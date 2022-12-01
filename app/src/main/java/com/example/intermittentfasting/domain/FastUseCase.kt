package com.example.intermittentfasting.domain

import com.example.intermittentfasting.data.FastRepository
import com.example.intermittentfasting.model.Fast
import com.example.intermittentfasting.utils.TimeUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

interface FastUseCase {
    suspend fun manualFastEntry(startDate: String, endDate: String)

    suspend fun manualFastEntryForForgottenStart(startDate: String)

    suspend fun toggleFast()

    suspend fun getCurrentOrLast(): Flow<Fast?>

    suspend fun getAllPastFasts(): Flow<List<Fast>>

    suspend fun deleteSpecificFast(fastId: Int)
}

class FastUseCaseImpl @Inject constructor(
    private val repo: FastRepository,
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val locale: Locale
) : FastUseCase {
    //    override operator fun invoke() {
//        Log.d("BK", "Usecase invoike")
//        coroutineScope.launch(coroutineDispatcher) {
//            repo.writeFastToDatabase(Fast(startTimeUTC = "1234", endTimeUTC = ""))
//            repo.writeFastToDatabase(Fast(startTimeUTC = "456", endTimeUTC = ""))
//            repo.writeFastToDatabase(Fast(startTimeUTC = "789", endTimeUTC = ""))
//
//           val b = repo.getMostRecentFast()
//            Log.d("BK","RECENT: ${b}")
//        }
//    }

    override suspend fun manualFastEntry(startDate: String, endDate: String) {
        withContext(coroutineDispatcher) {
            repo.updateFast(
                Fast(
                    startTimeUTC = startDate,
                    endTimeUTC = endDate,
                    manuallyEnteredPastFast = true,
                    startTimestamp = TimeUtils.getTimestampFromString(locale, startDate),
                    endTimestamp = TimeUtils.getTimestampFromString(locale, endDate)
                )
            )
        }
    }

    override suspend fun manualFastEntryForForgottenStart(startDate: String) {
        withContext(coroutineDispatcher) {
            repo.updateFast(
                Fast(
                    startTimeUTC = startDate,
                    endTimeUTC = "",
                    startTimestamp = TimeUtils.getTimestampFromString(locale, startDate)
                )
            )
        }
    }

    override suspend fun toggleFast() {
        withContext(coroutineDispatcher) {
            val mostRecentFast = repo.getMostRecentFast()
            if (mostRecentFast == null || !mostRecentFast.isActive()) {
                val dateString = TimeUtils.getCurrentUTCTimeString(locale)
                repo.updateFast(
                    Fast(
                        startTimeUTC = dateString,
                        endTimeUTC = "",
                        startTimestamp = TimeUtils.getTimestampFromString(locale, dateString),
                    )
                )
            } else {
                val dateString = TimeUtils.getCurrentUTCTimeString(locale)
                repo.updateFast(
                    mostRecentFast.copy(
                        endTimeUTC = dateString,
                        endTimestamp = TimeUtils.getTimestampFromString(locale, dateString),
                    )
                )
            }
        }
    }

    override suspend fun getCurrentOrLast(): Flow<Fast?> {
        return withContext(coroutineDispatcher) {
            repo.getCurrentOrLast()
        }
    }

    override suspend fun getAllPastFasts(): Flow<List<Fast>> {
        return withContext(coroutineDispatcher) {
            repo.getAllPastFasts()
        }
    }

    override suspend fun deleteSpecificFast(fastId: Int) {
        return withContext(coroutineDispatcher) {
            repo.deleteSpecificFast(fastId)
        }
    }

//    override suspend fun getCurrent(): Flow<Fast?> = repo.getMostRecentFast()

}
