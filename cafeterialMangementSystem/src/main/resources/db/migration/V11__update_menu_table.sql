-- Start a transaction
START TRANSACTION;

-- Step 1: Add the new column mealTypeId
ALTER TABLE menu
ADD COLUMN mealTypeId INT;

-- Step 2: Update existing rows with a default value (replace 'default_meal_type_id' with an actual value)
UPDATE menu
SET mealTypeId = 1;

-- Step 3: Alter the column to be NOT NULL
ALTER TABLE menu
MODIFY COLUMN mealTypeId INT NOT NULL;

-- Step 4: Add the foreign key constraint
ALTER TABLE menu
ADD CONSTRAINT fk_mealType
FOREIGN KEY (mealTypeId) REFERENCES MealType(Id);

-- Commit the transaction
COMMIT;