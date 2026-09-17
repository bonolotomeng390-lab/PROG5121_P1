# PROG5121_P1 — User Registration and Login System

## Overview
A console-based Java application that registers a new user and then allows them to log in, validating their details along the way. The project is split into two classes: `Login`, which holds the validation and authentication logic, and `PROG5121_P1`, which drives the console interaction.

## Features
- **User registration** with validation for:
  - Username — must contain an underscore (`_`) and be no more than 5 characters long
  - Password — must be at least 8 characters and include a capital letter, a number, and a special character
  - South African cell phone number — must start with `+27` followed by 9 digits
- **User login** — verifies a supplied username and password against the registered account
- **Status messages** — clear feedback for both successful and failed registration/login attempts
- **Unit tests** (JUnit 5) covering every validation rule and both success and failure paths

## Classes

### `Login.java`
Stores a user's details (first name, last name, username, password, cell number) and provides:
- `checkUserName()` — validates the username format
- `checkPasswordComplexity()` — validates password strength
- `checkCellPhoneNumber()` — validates the SA cell number format
- `registerUser()` — runs all three checks and returns a message describing the result
- `loginUser(username, password)` — checks supplied credentials against the stored ones
- `returnLoginStatus(isLoggedIn)` — returns a personalized welcome or error message

### `PROG5121_P1.java`
Contains `main`. Prompts the user for registration details, creates a `Login` object, and prints the registration result. If registration succeeds, it then prompts for login credentials and prints the login outcome.

### `LoginTest.java`
JUnit 5 test suite covering:
- Valid and invalid usernames
- Valid and invalid passwords (short, missing capital, missing number, missing special character)
- Valid and invalid cell numbers (missing international code, too many digits)
- Full registration flow (success and each failure case)
- Login with correct and incorrect credentials
- Both branches of the login status message

## How to Run
1. Open the project in NetBeans (or any Java IDE with Maven support).
2. Run `PROG5121_P1.java` as the main class.
3. Follow the console prompts to register, then log in.

## Running the Tests
Run `LoginTest.java` using JUnit 5 (via NetBeans' built-in test runner, or `mvn test` from the command line).

## Example Valid Input
| Field | Example |
|---|---|
| Username | `kyl_1` |
| Password | `Ch3ck!234` |
| Cell number | `+27838968976` |
