package com.example.intermittentfasting.domain

import android.util.Log
import com.example.intermittentfasting.data.FastRepository
import com.example.intermittentfasting.model.EmptyFast
import com.example.intermittentfasting.model.Fast
import com.example.intermittentfasting.model.FastContainer
import com.example.intermittentfasting.model.PastFast
import com.example.utils.TimeUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

data class StatBlock(
    val numberOfFasts: Int,
    val numberOfItemsActuallyShowing: Int,
    val firstFast: Fast,
    val longestFast: Fast,
    val shortestFast: Fast,
    val mostRecentCompletedFast: Fast,
    val averageLength: Long,
    val lastFastLengths: List<Float>,
    val lastFastTargets: List<Int>
)
/*
StatBlock
    longest fast
    shortest fast
    average length
    last month chart
 */

interface StatsUseCase {

    suspend fun getStatsNumbers(includeEmptyDays: Boolean = false): Flow<StatBlock?>

}

class StatsUseCaseImpl @Inject constructor(
    private val repo: FastRepository,
    private val locale: Locale,
    private val coroutineDispatcher: CoroutineDispatcher,
) : StatsUseCase {

    private fun mapPastFastsWithEmptyDays(list: List<Fast>): List<Fast> {
        val out = mutableListOf<Fast>()
        list.forEachIndexed { index, fast ->
            if (index == 0) {
                out.add(fast)
            } else {
                val previous = index - 1
                val ans =
                    TimeUtils.getDayDifference(locale, fast.endTimeUTC, list[previous].startTimeUTC)
                Log.d("BK", "===> ${ans}")
                if (ans < 1) {
//                    out.add(PastFast.toPastFast(locale, fast))
                    out.add(fast)
                } else {
                    Log.d("BK", "and $ans")
//                    out.add(EmptyFast(ans.toInt()))
                    repeat(ans.toInt()) {
                        out.add(Fast.getEmptyFast())
                    }
//                    out.add(PastFast.toPastFast(locale, fast))
                    out.add(fast)
                }
            }
        }
        return out
//        _pastfasts.value = out
    }

    override suspend fun getStatsNumbers(includeEmptyDays: Boolean): Flow<StatBlock?> {
        return withContext(coroutineDispatcher) {
            repo.getAllPastFasts().transform {
                if (it.isEmpty()) {
                    emit(null)
                } else {
                    var completeFasts = it
                    if (includeEmptyDays) {
                        completeFasts = mapPastFastsWithEmptyDays(it)
                    }


                    // as the list is revered already
                    val firstFast = it.last()
                    val lastFast = it.first()
                    val allFastLengths =
                        completeFasts.map { TimeUtils.getLengthOfFast(it.startTimestamp, it.endTimestamp) }
//                    println(allFastLengths)
//                    allFastLengths.average()
                    val mm = allFastLengths.map {
                        TimeUtils.convertMillisToX(
                            it,
                            TimeUtils.Companion.TimeType.HOUR
                        )
                    }
                    val fasttargets = completeFasts.map {it.targetHours}
                    println(mm)
                    println(fasttargets)
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
                            numberOfItemsActuallyShowing = completeFasts.size,
                            firstFast = firstFast,
                            longestFast = longestFast,
                            shortestFast = shortestFast,
                            mostRecentCompletedFast = lastFast,
                            averageLength = allFastLengths.average().toLong(),
                            lastFastLengths = mm,
                            lastFastTargets = fasttargets
                        )
                    )
                }
            }
        }
    }

}