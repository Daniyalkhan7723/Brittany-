package com.cp.brittany.dixon.navigation.navGraphs.detailGraph

import com.cp.brittany.dixon.utills.Constants

sealed class DetailRoute(val route: String) {

    object WorkoutDetail :
        DetailRoute(route = "workout_detail?id={${Constants.CATEGORY_ID}}&workout_id={${Constants.WORKOUT_ID}}&screenType={${Constants.SCREEN_TYPE}}") {
        fun passData(
            categoryId: String = "", screenType: String = "", workoutId: String = ""
        ): String {
            return "workout_detail?id=$categoryId&workout_id=$workoutId&screenType=$screenType"
        }
    }

    object ScheduleWorkoutsDetails : DetailRoute(route = "schedule_workout_detail")
    object InsightDetails : DetailRoute(route = "insight_detail")

    object WorkoutPlayer :
        DetailRoute(route = "player_workout/{${Constants.VIDEO_URL}}/{${Constants.WORKOUT_ID}}/{${Constants.WATCH_TIME}}/{${Constants.PROGRESS}}") {
        fun passData(url: String, workoutId: String, watchTime: String, progress: Int): String {
            return "player_workout/$url/$workoutId/$watchTime/$progress"
        }
    }

    object ImagePreview :
        DetailRoute(route = "image_url/{${Constants.IMAGE_URL}}/{${Constants.IMAGE_ID}}") {
        fun passData(imageUrl: String, imageId: String): String {
            return "image_url/$imageUrl/$imageId"
        }
    }

    object WorkoutAllSearch :
        DetailRoute(route = "workout_all_search?id={${Constants.CATEGORY_ID}}&name={${Constants.CATEGORY_NAME}}&screenType={${Constants.SCREEN_TYPE}}") {
        fun passData(id: String = "", name: String = "", screenType: String = ""): String {
            return "workout_all_search?id=$id&name=$name&screenType=$screenType"
        }
    }


    object SubscriptionDetail :
        DetailRoute(route = "subscription_detail/{${Constants.PACKAGE_ID}}") {
        fun passData(packageId: Int): String {
            return "subscription_detail/$packageId"
        }
    }

    object AllInsights : DetailRoute(route = "all_insights/{${Constants.SCREEN_TYPE}}") {
        fun passData(type: String): String {
            return "all_insights/$type"
        }
    }


    object AllScheduledWorkouts : DetailRoute(route = "all_scheduled_workout")
    object SubscriptionPlan : DetailRoute(route = "subscription_plan")

    object ProfileDetail : DetailRoute(route = "profile_details")
    object ProfileEdit : DetailRoute(route = "profile_edit")
    object ResetPassword : DetailRoute(route = "reset_password")
    object SubscriptionInfo : DetailRoute(route = "subscription_info")
}