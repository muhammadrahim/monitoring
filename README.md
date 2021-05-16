### How to run

1. `./gradlew installDist`
2. `cd app/build/install/app/bin`
3. `./app <path to csv> <threshold>`

### Insight into potential improvements

The discussion about efficiency is interesting. Whilst there is only one pass over the data set, and statistics are
simultaneously, there are a lot of calls to the repository (used to store data). Ideally, this repository would also be
an interface - allowing users to put in whichever storage technique they wish. There is opportunity here to use caching
also. We could cache by storing the 10-second window of data and removing stale data. Or caching the statistics is also
another option. The caching strategies will vary depending on the constraints as, for some applications, the caching of
the ten-second window would not be viable. The benefit of using this repository, however, is that we are not using as
much memory since we can inject a third-party application in place of the repository I added by adding an interface that
is implemented by my `DataRepository` and whichever application is added. By adding this repository pattern, we have
made our application more "stateless" and, hence, easier to scale.

For another approach for scaling, I could use coroutines/threads to handle the different steps. One for handling the
two-minute window and one for the ten-second window. Since these are independent tasks, they can be run simultaneously.

One problem with my statistics is that to find the most used API in a certain window, I am using a `.maxBy` which means
I iterate through my `HashMap` keeping track of the different APIs as opposed to just returning it. I do not think that
this is an issue, however, it is unlikely a user will have a lot of resource sections. There are many statistics that I
could have used, I considered importing a statistics library to handle this. However, I felt most of them would not have
been useful enough to dedicate time towards in the suggested time period. Things that would have been useful would
include: 50th, 90th, 95th and 99th percentiles. Maybe also `traceId` but this would not be viable for a take-home.
Another thing would be including data of all the resource subsections etc to see which are being hit most frequently.
For example, using `/api/example`, we could display how often `example` is being hit. This way, if that endpoint is
using distinct third parties from other endpoints, clients could explore scaling these.

Another problem was the data structure used for holding the logs. For me, I chose to use a `Queue` as the windows used
are a very clear example of a first-in-first-out approach. I debated between use of a regular queue, or a priority
queue. A priority queue has `O(log(n))` insertion complexity whereas a regular queue is `O(1)`. In the end, I chose to 
have priority queues because I felt it was more important to have data in order so that I did not lose logs or have them
in the wrong window and, consequentially, providing wrong information to the output. Another benefit  of priority queues
 (that I did not utilise) is that when I am cleaning up my two-minute window of data, I could utilise the `poll()` to 
terminate once the criteria is no longer met. This works as the queue is sorted. However, by using `removeAll`, I am 
checking all elements which will slow it down. I could also have used this to only use one priority queue, where the
ten-second window queue was just a subset and following the same pattern. However, I chose to keep it simple due to
time constraints.

In terms of my testing, due to time constraints, I did not add in an automated component test. I ran this manually on
my machine. However, if there were no time constraints, I would have included one so that it could be maintained by
other developers.