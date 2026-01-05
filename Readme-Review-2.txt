Restaurant Billing System – Review 2

Project Overview

This is a Java Swing–based Restaurant Billing System that allows users to select food items, enter customer details, apply discounts, calculate GST, and generate a bill receipt.

⸻

Changes Made After Review 1 (Review 2)
	1.	Input Validation Added
Customer name and mobile number are now validated to prevent invalid or empty inputs.
	2.	Improved Error Handling
User-friendly error messages are shown instead of crashes. File-handling errors are safely managed.
	3.	Modular Code Structure
Bill calculation and validation logic were separated into helper methods to improve readability and maintainability.
	4.	Event Handling Fixed
The Generate Bill button was properly connected using an ActionListener so it functions correctly.
	5.	UI Improvements
Button visibility issues were fixed by improving contrast and styling for better user experience.
	6.	Billing Logic Enhancement
Student discount and GST are applied in a proper sequence, and the final bill is auto-generated as a text file.
	7.	Code Documentation
Comments were added to explain important methods and Review-2 enhancements.

⸻

Technologies Used
	•	Java
	•	Java Swing (GUI)
	•	File Handling

⸻

How to Run
	1.	Compile the file: javac RestaurantBilling.java
	2.	Run the program: java RestaurantBilling

⸻

Conclusion

Review-2 focuses on improving robustness, usability, and code quality based on feedback from Review-1.
