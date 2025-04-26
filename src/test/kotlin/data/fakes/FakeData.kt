package data.fakes

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.text.SimpleDateFormat
import java.util.*


//region Constants for parser tests
const val invalidDateFormat =
    "some meal,101,10,5001,not-a-date,\"['quick']\",\"[300,10,20,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"
const val invalidIdNumber =
    "some meal,not_a_number,10,5001,2024-03-10,\"['quick']\",\"[300,10,20,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"
const val missingFieldLine =
    "bad meal,108,10,5002,2024-03-10,\"['quick']\",,3,\"['Do this','Do that','Serve']\",\"Some description\",\"['ingredient1','ingredient2']\",2"
const val invalidNutritionValue = "some meal,101,10,5001,2024-03-10,\"['quick']\",\"[300,10,XX,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"

const val validLineTrailingComma =
    "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\"['easy', 'quick', 'snack']\",\"[300.0, 15.0, 25.0, 2.0, 1.0, 5.0, 10.0]\",4,\"['Butter bread', 'Add cheese', 'Grill', 'Serve']\",\"Cheesy sandwich for snack\",\"['bread', 'cheese', 'butter']\",3,"
const val validLineWordInDoubleQuotation =
    "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\"['easy', 'quick', 'snack']\",\"[300.0, 15.0, 25.0, 2.0, 1.0, 5.0, 10.0]\",4,\"['Butter bread', 'Add cheese', 'Grill', 'Serve']\",\"Cheesy \"\"sandwich\"\" for snack\",\"['bread', 'cheese', 'butter']\",3"
val validMealWordInDoubleQuotation= Meal(
    name = "Grilled Cheese Sandwich",
    id = 101L,
    minutes = 10L,
    contributorId = 5001L,
    submitted = constructDate("2024-03-10"),
    tags = listOf("easy", "quick", "snack"),
    nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
    numberOfSteps = 4,
    steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
    description = "Cheesy \"sandwich\" for snack",
    ingredients = listOf("bread", "cheese", "butter"),
    numberOfIngredients = 3
)
const val validLine ="Grilled Cheese Sandwich,101,10,5001,2024-03-10,\"['easy', 'quick', 'snack']\",\"[300.0, 15.0, 25.0, 2.0, 1.0, 5.0, 10.0]\",4,\"['Butter bread', 'Add cheese', 'Grill', 'Serve']\",\"Cheesy sandwich for snack\",\"['bread', 'cheese', 'butter']\",3"

val validMeal= Meal(
    name = "Grilled Cheese Sandwich",
    id = 101L,
    minutes = 10L,
    contributorId = 5001L,
    submitted = constructDate("2024-03-10"),
    tags = listOf("easy", "quick", "snack"),
    nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
    numberOfSteps = 4,
    steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
    description = "Cheesy sandwich for snack",
    ingredients = listOf("bread", "cheese", "butter"),
    numberOfIngredients = 3
)
//endregion

//region Constants for reader tests
const val lineWithCarriageInDescription =
    "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans\rand cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5\r\n"

const val linesWithLineBreakerInDescrption =
    "grilled cheese sandwich,101,10,5001,2024-03-10,\"['easy', 'quick']\",\"[300, 15, 25, 2, 1, 5, 10]\",4,\"['Butter bread','Add cheese','Grill','Serve']\",\"Classic cheesy snack\nperfect for quick meals.\",\"['bread','cheese','butter','salt','pepper']\",5\n" +
            "veggie pasta,102,25,5002,2024-03-11,\"['vegetarian', 'lunch']\",\"[350, 10, 30, 3, 2, 4, 8]\",5,\"['Boil pasta','Chop veggies','Cook veggies','Mix pasta','Serve']\",\"A light and healthy pasta option\nwith fresh vegetables.\",\"['pasta','zucchini','bell pepper','onion','olive oil']\",5\n"
 val linesWithLineBreakerInDescriptionAsList = listOf(
    "grilled cheese sandwich,101,10,5001,2024-03-10,\"['easy', 'quick']\",\"[300, 15, 25, 2, 1, 5, 10]\",4,\"['Butter bread','Add cheese','Grill','Serve']\",\"Classic cheesy snack perfect for quick meals.\",\"['bread','cheese','butter','salt','pepper']\",5",
    "veggie pasta,102,25,5002,2024-03-11,\"['vegetarian', 'lunch']\",\"[350, 10, 30, 3, 2, 4, 8]\",5,\"['Boil pasta','Chop veggies','Cook veggies','Mix pasta','Serve']\",\"A light and healthy pasta option with fresh vegetables.\",\"['pasta','zucchini','bell pepper','onion','olive oil']\",5"
)
//endregion

//region Constants for repository tests
val validListOfRows= listOf("emptyline", "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\" +\n" +
" \"\\\"['easy','quick']\\\",\\\"[300.0,15.0,25.0,2.0,1.0,5.0,10.0]\\\",4,\" +\n" +
" \"\\\"['Butter bread','Add cheese','Grill','Serve']\\\",\\\"A cheesy snack\\\",\" +\n" +
"\"\\\"['bread','cheese','butter']\\\",3\""
)


val parsedMeal=Meal(
name = "Grilled Cheese Sandwich",
id = 101,
minutes = 10,
contributorId = 5001,
submitted = constructDate("2024-03-10"),
tags = listOf("easy", "quick"),
nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
numberOfSteps = 4,
steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
description = "A cheesy snack",
ingredients = listOf("bread", "cheese", "butter"),
numberOfIngredients = 3
)


const val lineWithEmptyDescription =
    "empteyline\nGrilled Cheese Sandwich,101,10,5001,2024-03-10," +
            "\"['easy','quick']\",\"[300.0,15.0,25.0,2.0,1.0,5.0,10.0]\",4," +
            "\"['Butter bread','Add cheese','Grill','Serve']\",\"\",\"['bread','cheese','butter']\",3"

val parsedMealWithNullDescription = Meal(
    name = "Grilled Cheese Sandwich",
    id = 101,
    minutes = 10,
    contributorId = 5001,
    submitted = constructDate("2024-03-10"),
    tags = listOf("easy", "quick"),
    nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
    numberOfSteps = 4,
    steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
    description = null, // <- Important part
    ingredients = listOf("bread", "cheese", "butter"),
    numberOfIngredients = 3
)
//endregion


fun constructDate(dateString: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.parse(dateString)

}