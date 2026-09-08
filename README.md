# Smart Pantry Manager

Smart Pantry Manager is an Android app created for the Mobile App Development 700 module.

The application will be used to keep track of some of the ingredients that the user currently has at home and they will be able to be offered recipes that can be prepared with them.

Strict recipe matching is used for the application. If all of the ingredients needed to make a recipe are not in the pantry, the recipe is not suggested.

## Features

The application has the following features:

- Add pantry ingredients
- Check out the ingredients saved in the pantry
- Edit existing ingredients
- Delete ingredients
- Record ingredients and units
- Optional expiry dates
- Input validation
- Persistent pantry storage
- Recipes to be suggested based on contents of the pantry
- Attention to the correct ingredients and quantities
- Basic singular and plural handling of ingredients' names
- Convert units of mass from grams to kilograms and vice versa
- Changing units from millilitres to litres and vice versa
- Adds quantities for the same ingredient when there are multiple cans or boxes of the item
- Description of the appropriate ingredients, preparation instructions and method
- Liquid/Weight units
- Bottom navigation between the main screens

## Recipe Matching

The recipe matching system matches the ingredients needed for any recipe against the ingredients found in the user's pantry.

Only a recipe will be displayed when all the ingredients are in stock.

For instance, if a recipe calls for:

- 200 g rice
- 2 tomatoes

In the pantry there is:

- 100 g rice
- 2 tomatoes

The recipe will not be recommended as there is not enough rice.

The application can be used to merge suitable amounts together as well. For example:

- 100 g rice
- 0.1 kg rice

They are considered as 200 g of rice.

This enables the application to deal with ingredients that have been added to the pantry multiple times, or stored with different compatible storage units.

## Database

Local data is stored using SQLite with the help of the `SQLiteOpenHelper` class.

The reason for choosing SQLite is that the app must handle structured and persistent data, but not require an internet connection or an external database server.

The database stores:

- Pantry items
- Recipes
- Recipe ingredients

Application goes to the background and when re-opened, the pantry data will still be there.

The app comes with 20 preloaded recipes, which are stored in the local database.

## Technologies Used

- Java
- Android Studio
- XML layouts
- SQLite
- SQLiteOpenHelper
- RecyclerView
- SharedPreferences
- Material Components
- Git and GitHub

## Main Screens

The main screens of the application are:

### My Pantry

Shows the ingredients that are in the user's storage.

Items can be added, modified and removed from the user's pantry from here.

### Add / Edit Ingredient

Enables the user to type in:

- Ingredient name
- Quantity
- Unit
- Optional expiry date

### Suggested Recipes

This activity matches recipes with a strict approach to recipe matching.

### Recipe Details

Shows recipe details.

### Settings

Controls the application's preferences.

## Navigation

Bottom navigation is used between:

- Pantry
- Recipes
- Settings

Extra screens like Add/Edit Ingredient and Recipe Details are displayed as needed.

## Input Validation

The application validates before putting pantry data into the database.

Examples include:

- The name of the ingredient entered cannot be blank
- Quantity is required
- Quantity should be positive
- Unit is required
- A date for the expiration is optional
- If an expiry date has been given, it must follow the format `YYYY-MM-DD`
- Invalid dates such as `2026-02-31` are rejected

## Project Setup

To run the project:

1. Clone or download this repository.
2. Open Android Studio.
3. Click on Open and then pick the `SmartPantryManager` project folder.
4. Give Gradle time to sync the project.
5. Choose an Android emulator or an attached Android device.
6. Run the application.

The minimum SDK version for this application is 24.

## Project Structure

Some of the main Java classes include:

- `MainActivity` - Displays and controls the pantry screen
- `AddEditIngredientActivity` - adds and edits ingredients
- `SuggestedRecipesActivity` - matches recipes using a strict recipe matching approach
- `RecipeDetailActivity` - shows recipe details
- `SettingsActivity` - controls the application's preferences
- `DatabaseHelper` - is used for managing the SQLite database
- `PantryAdapter` - this is the Adapter that's used to display pantry items in a RecyclerView
- `RecipeAdapter` - it is an adapter for the RecyclerView that displays suggested recipes
- `PantryItem` - pantry item model
- `Recipe` - recipe model
- `RecipeIngredient` - model for representing a recipe ingredient

## Testing

The application was tested for:

- Adding pantry ingredients
- Editing pantry ingredients
- Deleting pantry ingredients
- Empty pantry handling
- Data persistence
- Quantity validation
- Expiry date validation
- Strict recipe matching
- Insufficient ingredient quantities
- Unit conversion
- Adding two or more amounts of the same ingredient
- Recipe detail navigation
- Settings persistence
- Bottom navigation

## Author

Brogan Naidu

Mobile App Development 700  
2026