# Fast RTPS: test cases description #

The scenarios defined for testing are:

- `Stress`
- `Stability` 

Both test the performance of sending and receiving message between Publishers and Subscriber.

## Test Case 1.- Stress Scenario ##

The goal of this scenario is to assess system performance with a high load in a short period of time. In this test we start a subscriber and keep it online, then start 200 threads (users) who will start to send messages (progressively) to the subscriber.

There is only one type of message sending, but every instance os publisher is isolated from the others. We add one thread every 6 seconds to reach 200 users, then test is online during another 5 minutes (total of 25 minutes).

|ID	| GE API method	| Operation	| Type	| Payload	| Max. Concurrent Threads |
|---|:--------------|:----------|:------|:----------|:------------------------|
| 1 | N/A |  Send Message	| N/A	| N/A | 200 |

Regarding the variables

- **ruta** -> Path where the folder "HelloWorldExample" can be found. (By default this script must be together the folder (in the same location), or configure this path)
- **duration** -> How much time will be running the test. (in seconds)
- **users** -> How many users (publishers) will be up during the test (Threads)
- **jump**-> How much time until every users will be up (ramp-up) (in seconds)
- **between** -> How many seconds between users ( =jump/users) (in seconds)

## Test Case 2.- Stability Scenario ##

It's analogous to the test case 1. The only difference is quantity of users and time duration. We add one thread every 6 seconds to reach 20 users, then test is online a total of 6 hours.

|ID	| GE API method	| Operation	| Type	| Payload	| Max. Concurrent Threads |
|---|:--------------|:----------|:------|:----------|:------------------------|
| 1 | N/A |  Send Message	| N/A	| N/A | 20 |

Regarding the variables

- **ruta** -> Path where the folder "HelloWorldExample" can be found. (By default this script must be together the folder (in the same location), or configure this path)
- **duration** -> How much time will be running the test. (in seconds)
- **users** -> How many users (publishers) will be up during the test (Threads)
- **jump**-> How much time until every users will be up (ramp-up) (in seconds)
- **between** -> How many seconds between users ( =jump/users) (in seconds)