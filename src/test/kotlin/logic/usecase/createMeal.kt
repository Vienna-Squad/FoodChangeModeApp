package logic.usecase

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.*

fun createMeals(
    name:String,
    id:Long,

)=Meal(
    name, id, null, null, Date(20/7/2003), null,
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
