# Views-Rest-API
Project Specification:

1) Each API call in the project should have a user as parameter (Assume numeric auto

incrementing ids)

2) Design a REST API endpoint to store the views of user profiles

a. UserX viewing the profile of UserY should be recorded

b. Date and time of the view should be recorded

3) Design a REST API endpoint to view the users who viewed this user’s profile in the past.

a. The list should include the viewer’s user id and also the date/time of the view

b. The list should NOT include more than 10 items

c. The list should NOT include views older than 10 days

4) Design any relevant support code to ensure that the requirements in the third

specification are met at all times.

5) Assuming you have millions of views every hour, try to come up with the most efficient

database schema in terms of storage space, latency and throughput. Write inline

comments to justify the design decisions taken, so that the next developer after you will

understand the design and can easily maintain it.

Instructions:

1) Grab Dropwizard and create a maven project with it: http://dropwizard.codahale.com/

2) Setup your project so that it can read from and write to a local file based database. (This

is essential for us to test your solution locally without creating databases and schemas).

Use any Sql compliant library here; H2, Derby, HsqlDb, Sqlite etc.

3) Create the source code to meet the following requirements

4) Create a design document that briefly discusses the chosen database schema and the

reasoning behind it. Please make sure these questions are answered and discussed:

a. Do you delete any data from the database?

i. If yes, When? Why?

ii. If no, Why?

b. Do you have any periodic task type of batch jobs to maintain data?

i. If yes, Why needed?

ii. If no, Why not needed?

c. What type of compromise (for example; tradeoff between storage on disk vs

latency) did you see? What was your decision? Why?

5) Also include the instructions to run the project and test if it works as expected
