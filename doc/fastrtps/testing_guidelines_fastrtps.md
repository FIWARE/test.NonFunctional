This project is part of FIWARE.

# FastRTPS: non functional test guidelines

eprosima Fast RTPS is a C++ implementation of the RTPS (Real Time Publish Subscribe) protocol, which provides publisher-subscriber communications over unreliable transports such as UDP, as defined and maintained by the Object Management Group (OMG) consortium. RTPS is also the wire interoperability protocol defined for the Data Distribution Service (DDS) standard, again by the OMG. eProsima Fast RTPS holds the benefit of being standalone and up-to-date, as most vendor solutions either implement RTPS as a tool to implement DDS or use past versions of the specification.

## Testing environment

For the testing environment, only one machine is needed. Two instances (publisher and subscriber) are executed at the same time.
We've modified an provided example in sourcecode (HelloWorld) for send, receive and take response time between both instances.

## Test execution

### Preliminary setup

Once the HW necessary for the test described previously at "Testing Environmment" chapter have been setup, the following preliminary steps need to be accomplished before to start the test process:

There is no extra requirements to test FastRTPS. We only need the main application and configure execution with parameters to test its performance.

## Testing step by step

The test of FastRTPS must be done in one step:

1. Script configuration:

Through the bash script we can configure number of threads, ramp-up, duration and delay between messages. All parameters can be passed as arguments to the script.

For this purpose, the following script (run_script_fastrtps.sh) must be configured:

* **ruta** -> Path where the folder "HelloWorldExample" can be found. (By default this script must be together the folder (in the same location), or configure this path)
* **duration** -> How much time will be running the test. (in seconds)
* **users** -> How many users (publishers) will be up during the test (Threads)
* **jump**-> How much time until every users will be up (ramp-up) (in seconds)
* **between** -> How many seconds between users ( =jump/users) (in seconds)


Finally, in order to get the results, the path to the results file will be "/opt/tmp/logFastRTPS_THREADNUMBER.log"  must be configured in the Simple Data Writer from the script.

2. Script execution:

For executing the script, the following command must be typed in the location of the HelloWorldExample folder:

`./HelloWorldExample/HelloWorldExample subscriber`

Once the subscriber is online, we start the publishers using the script:

(Could be necessary to give execution permissions to the file using:  `chmod +x ./run_script_fastrtps.sh`)

`./run_script_fastrtps.sh`

In other Fiware components, we recommend to execute the scripts using "nohup" mode. In this case, this is not working properly, so it is necessary to keep online the session in the system until the test ends.

It´s highly recommended to use a hardware resources monitoring tool, in order to measure things like memory and CPU usages, free memory, etc.

## Expected results

As output from the script, as many files as thread are created. Finally, when the execution ends, all of them are joined into a new single .log file (in csv format). This file can be used for plotting different kinds of charts, like threads number, response times, number of errores, responses per second, etc. A plotting tool (like gnuplot) is needed in order to do this. All this information is similar to the jmeter reporting but, in this case, we have less variable to measure.

Additionally, if a monitoring tool has been used, then different data from it is collected too. Depending on the monitoring tool, it can generate charts directly, or it can be used another tool for plotting the output from monitoring tool (for example, sar from systat library can be used for monitoring, and k-sar tool for plotting the sar output)


