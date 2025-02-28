# Validation
Our work is validated by checking the fuctionalty, effectiveness and usability of the patch.  

## Functionality
We have to proove that the sensor values are intercepted, modified and then passed to the original app, without any significant issues.  
To do this we record values with an [android app](https://gitlab.com/sensorkraken/android-app) before and after the application of our patch.

<!-- figures -->

As it can be seen, the values were changed after application of the patch.

## Effectiveness



## Usability
Usability was tested only in a very, very limited manner.  
We applied the patch to a motion controlled game, and were not able to notice any difference.  
In order to make sure, that our patch does not create any usability issues, further testing with a broad range of functionailites, dependent on motion controll, is required.

