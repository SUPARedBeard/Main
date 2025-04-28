// Joshua-Pitts-CPT-168-A80S-Lab-10.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
/*
Write a C++ program to read this file into your program and do the proper calculation including overtime
if any. Overtime is times and half of the pay rate. Deduct 2% for Social Security, 5% retirement plan, 3%
Federal tax (total of 10% deduction). Create a report as follow: Lat four digit of SSN, Employee’s first initial
and last name, Hours worked, Hourly rate, Gross pay, Total deductions, and Net pay. You may have to
use “substr” for SSN and Employees first initial.
Make sure you have number of records and thank you note as a summary at the bottom.
*/
#include <iostream>
#include <fstream>
#include <iomanip>
#include <string>
using namespace std;

// Function to calculate pay


double calculateGrossPay(double hoursWorked, double hourlyRate)
{
    double grossPay;

    if (hoursWorked <= 40)
    {
        grossPay = hoursWorked * hourlyRate;
    }
    else
    {
        double overtimeHours = hoursWorked - 40;
        grossPay = 40 * hourlyRate + (1.5 * hourlyRate * overtimeHours);
    }
    return grossPay;
}


int main()
{
    system("color f0");
    // Display my Information
    cout << "\t\t\t\t******************************" << endl;
    cout << "\t\t\t\t*        Joshua Pitts        *" << endl;
    cout << "\t\t\t\t*  Payroll Sequential File   *" << endl;
    cout << "\t\t\t\t*        CPT-168-A80S        *" << endl;
    cout << "\t\t\t\t*           Lab 10           *" << endl;
    cout << "\t\t\t\t******************************" << endl;

    // Declare Variables
    int recordCount = 0;
    string ssn, firstName, lastName;
    double hoursWorked, hourlyRate;

    ifstream inputFile("Payroll.txt");
    if (!inputFile)
    {
        cout << "Error opening file." << endl;
        return 1;
    }

    cout << fixed << setprecision(2);

    cout << "\tSSN\tInitial Last\tHours\tRate\tGross Pay\tDeductions\tNet Pay" << endl;
    cout << "\t----\t------------\t-----\t-----\t---------\t----------\t-------" << endl;

   

    while (inputFile)
    {
        
        getline(inputFile, firstName, '#'); 
        getline(inputFile, lastName, '#'); 
        getline(inputFile, ssn, '#');      
        inputFile >> hoursWorked;          
        inputFile.ignore();               
        inputFile >> hourlyRate;           
        inputFile.ignore();              
        


        // Calculate gross pay
        double grossPay = calculateGrossPay(hoursWorked, hourlyRate);

        // Calculate deductions
        double socialSecurity = 0.02 * grossPay;
        double retirementPlan = 0.05 * grossPay;
        double federalTax = 0.03 * grossPay;
        double totalDeductions = socialSecurity + retirementPlan + federalTax;

        // Calculate net pay
        double netPay = grossPay - totalDeductions;

        
        // Display the report
        cout << '\t' << ssn.substr(7, 4) << '\t' << firstName[0] << " " << lastName << '\t'
            << hoursWorked << '\t' << hourlyRate << '\t' << grossPay << '\t'
            << '\t' << totalDeductions << '\t' << '\t' << netPay << endl;

        
        
        recordCount++;
    }
    
    cout << "\n\t\tNumber of Records: " << recordCount << endl;


    inputFile.close();
    cout << "\n\n\t\t\t\t\tT H A N K  Y O U" << endl;
    system("pause");
    return 0;
}
