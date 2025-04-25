import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.*

fun createMeals(
    name:String
)=Meal(
    name, null, null, null,Date() , null,
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
