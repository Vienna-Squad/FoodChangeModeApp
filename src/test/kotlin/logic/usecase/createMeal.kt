package logic.usecase

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.*

fun createMeals(
    name:String,
    id:Long,
    date: Date

)=Meal(
    name, id, null, null, date, null,
    Nutrition(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    ), null, null, null, null, null
)
