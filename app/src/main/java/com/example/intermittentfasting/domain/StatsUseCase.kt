package com.example.intermittentfasting.domain

import com.example.intermittentfasting.data.FastRepository
import com.example.intermittentfasting.model.Fast
import com.example.utils.TimeUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class StatBlock(
    val numberOfFasts: Int,
    val firstFast: Fast,
    val longestFast: Fast,
    val shortestFast: Fast,
    val mostRecentCompletedFast: Fast,
    val averageLength: Long
)
/*
StatBlock
    longest fast
    shortest fast
    average length
    last month chart
 */

interface StatsUseCase {

    suspend fun getStatsNumbers(): Flow<StatBlock?>

}

class StatsUseCaseImpl @Inject constructor(
    private val repo: FastRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
) : StatsUseCase {
    override suspend fun getStatsNumbers(): Flow<StatBlock?> {
        return withContext(coroutineDispatcher) {
            repo.getAllPastFasts().transform {
                if (it.isEmpty()) {
                    emit(null)
                } else {
                    // as the list is revered already
                    val firstFast = it.last()
                    val lastFast = it.first()
                    val allFastLengths =
                        it.map { TimeUtils.getLengthOfFast(it.startTimestamp, it.endTimestamp) }
                    println(allFastLengths)
                    allFastLengths.average()
                    val longestFast = it.maxWith(Comparator.comparingLong {
                        TimeUtils.getLengthOfFast(
                            it.startTimestamp,
                            it.endTimestamp
                        )
                    })
                    val shortestFast = it.minWith(Comparator.comparingLong {
                        TimeUtils.getLengthOfFast(
                            it.startTimestamp,
                            it.endTimestamp
                        )
                    })
                    emit(
                        StatBlock(
                            numberOfFasts = it.size,
                            firstFast = firstFast,
                            longestFast = longestFast,
                            shortestFast = shortestFast,
                            mostRecentCompletedFast = lastFast,
                            averageLength = allFastLengths.average().toLong()
                        )
                    )
                }
            }
        }
    }

}