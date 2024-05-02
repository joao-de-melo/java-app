# Exercise Java Application

I didn't gave much thought into naming and code structure, so, please forgive me on that. 
I know there is a lot of space for improvement. Naming and project structure, is the kind of problem I usually try to
adopt from what is already in place, to avoid disrupting approaches.

## Setup

- Requirements: JDK 17

## Running Tests

      ./gradlew test

## Run Locally Rest Server

      ./gradlew :rest-server:run

## Run Locally Console App

      ./gradlew :console-app:run --args="<file> http://localhost:8080"

## Assumptions

- CSV files could be bigger then the memory available to the process.
- Order of processing lines is not important.
- Should ignore (be resilient to) invalid lines.


## Project Structure

- console-app: Where the CSV parsing is implemented
  - Main class is where the main method is implemented
- rest-server: A simple rest server using spring boot
  - Main class is where the main method is implemented
- end-to-end-test: An end to end test, to ensure both apps integrate nicely
  

### Tests

- I have used Junit5 for all tests
- At the root package of both console-app and rest-server, there is an IntegrationTest which aims to validate the integration of all internal components. 

## Future Improvements

- Improve naming. In order to be quicker, I didn't gave much though into naming of modules, tests, classes and even variables. 
- Better handling on failing to process lines. Currently, we only log a message. This leads to lines getting potentially lost in the process, for example, if the rest server is not responsive.
This can be achieved by improving the resiliency, patterns like retry and circuit breakers could be useful here.
- Better validation of input parameters. At the moment, we don't check if the URL provided is a valid URL, we could even go further and check if the server is reachable.
- From an architecture point of view, in some cases (like processing a very large set of files), it might make sense to introduce a queueing technology in the middle to simplify the
  implementation of non-functional requirements like handling system/network failures and throttling/backpressure.
- Improve CI/CD (gradle plugins), by introducing code quality analysis tools (example: coverage checks, format checks, etc.), to ensure code remains as maintainable as possible.
- Use an external SQL DB. At the moment, we are using an inmemory database, just to complete the exercise, but that might not be the ideal tool.
- Split Integration Tests from Unit tests, that way this can be run in parallel to have faster feedback loops.
- Use gradle conventions for multi module project (DRY principles)
- End to end tests should use the produced artifacts, instead of importing them as dependencies.