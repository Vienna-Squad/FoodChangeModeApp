import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.*

fun createMeals(
    name:String,
    date: Date
)=Meal(
    name, null, null, null,date , null,
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
