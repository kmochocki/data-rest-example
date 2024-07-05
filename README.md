# Getting Started

## Description

The project purpose is to show the discrepancy between updating the collection of the associated resources using `PATCH` and `PUT` HTTP methods

The tests are aggregated in `com.kmochocki.datarestexample.ResourceUpdateTest` class
* `shouldCreateMainResourceWithSingleChildResource()` and `shouldCreateMainResourceWithTwoChildResources()` are to ensure
  that the request body is correct and `POST` is working with it
* `shouldAddChildResourceUsingPatchMethod()` and `shouldAddChildResourceUsingPutMethod()` are using
  `performAddingChildUsing(HttpMethod httpMethod)` in order to reflect that the only difference is the HTTP method
* analogically, `shouldRemoveChildResourceUsingPatchMethod()` and `shouldRemoveChildResourceUsingPutMethod()` using 
  `performRemovingChildUsing(HttpMethod httpMethod)`

## Execution

in the main dir run
```
$ ./gradlew build -i
```

`-i` allows application logs to be seen in the console (HTTP requests are logged)
