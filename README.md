# Intro

Write simple console program that monitors HTTP traffic on your machine. 

Treat as an opportunity to show us how you'd write something efficient, scalable, well-structured & maintainable.

### Criteria

* Logical and maintainable code structure
* Efficient use of data structures
* Comments and unit tests
* Correctness
* Insight into potential improvements

# Assignment

HTTP log monitoring console program

* Read a CSV-encoded HTTP access log. It should take either the file as a parameter or read from standard input.

_Provided log file is called `sample_csv.txt` in this gh directory_

* For every 10 seconds of log lines, display stats about the traffic during those 10 seconds: the section of the website with the most hits, as well as statistics that might be useful for debugging. A section is defined as being what's before the second '/' in the resource section of the log line. For example, the section for `/api/user` is `/api` and the section for `/report` is `/report`.

* Whenever total traffic for the past 2 mins exceeds a certain number on average, print a message to the console saying that "High traffic generated an alert - hits = {value}, triggered at {time}". The default threshold should be 10 requests per second but should be configurable.

* Whenever the total traffic drops again below that value on average for the past 2 minutes, print another message detailing when the alert recovered, +/- a second

* Consider the efficiency of your solution and how it would scale to process high volumes of log lines - don't assume you can read the entire file into memory.

* Write your solution as you would any piece of code that others might need to modify and maintain, both in terms of structure and style.

* Write a test for the alerting logc.

* Explain how you'd improve on this application design.

# Guidelines and clarifications

* The time in the alert message can be formatted however you like (using a timestamp or something more readable are both fine), but the time cited must be in terms of when the alert or recovery was triggered in the log file, not the current time

* Make a reasonable assumption about how to handle the 10-second intervals, there are a couple of valid options here. Make a similar assumption about how frequently stats should be displayed as you process (but don't just print them at the end!)

* Try to only make one pass over the file overall, for both the statistics and the alerting, as if you were reading it in real time.

* The date is in Unix time

* Free to use Google, StackOverflow etc, and standard and open source libraries, but the core of the alerting logic must be your own.

* Duplicate alerts should not be triggered - a second alert should not be triggered before a recovery of the first

* The alerting state does not need to persist across program runs.

* Package your source code along with instructions on how to run it into a zip/tar file of some sort.