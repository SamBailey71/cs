Clarity Exercise 

Notes on exercise

Getting the application to run.
1. Build the docker image... from the root of the project 'docker build -t clarity-exercise .'
2. Run the docker image... from the root of the project 'docker run -p 8080:8080 clarity-exercise'
3. The APIs should now be available from http://localhost:8080 as per the requirements document

Notes on my approach.

The project has been created as a Maven SpringBoot 3 project utilising embedded h2 database (in memory) for speed of development.
I have followed the brief (and conducted manual tests), so you shouldn't have any nasty surprises in testing the APIs via PostMan or similar.
For authentication as per your suggestions I implemented API-Key authentication, and the key you should use for testing is 'Clarity'. 
In terms of testing, I implemented a number of unit tests (not comprehensive), focussing on what I would deem to be the most important areas of code, ie. my service layer, security and exception handling. 
Integration tests have been omitted due to time constraints

If I had more time... I would definitely add integration & some end to end component testing. I would love to spend some time maybe integrating Kafka or ActiveMQ, maybe publishing new metrics to a topic or similar.

