Feature: Employee management operations

  Scenario: Create Employee
    Given The user is on the employee creation page and wants to create an employee with the following details:
      | First Name | Last Name | Email          | Phone Number | Department  |
      | Franco     | Franco    | test@gmail.com | +23058239865 | ENGINEERING |
    When The user submits the request to create the employee
    Then The employee should be successfully created with the message: "Employee created Successfully"

  Scenario: Get Employee by ID
    Given The user is on the employee details page and requests the employee details for employee ID "1"
    When The user submits the request to get the employee details
    Then The employee details should be returned with the following information:
      | Employee Id | First Name | Last Name | Email          | Phone Number | Department  |
      | 1           | Franco     | Franco    | test@gmail.com | +23058239865 | ENGINEERING |

  Scenario: Get All Employees
#    Given The user is on the employee list page and requests the list of all employees
    When The user requests the list of all employees
    Then The list of employees should be returned the following :
      | Employee Id | First Name | Last Name | Email          | Phone Number | Department  |
      | 1           | Franco     | Franco    | test@gmail.com | +23058239865 | ENGINEERING |


  Scenario: Create Employee with and invalid email
    Given The user is on the employee creation page and wants to create an employee with the following details:
      | First Name | Last Name | Email    | Phone Number | Department  |
      | Franco     | Franco    | test.com | +23058239865 | ENGINEERING |
    When The user submits the request to create the employee
    Then The request should fail with the following message :"Invalid email format: test.com"