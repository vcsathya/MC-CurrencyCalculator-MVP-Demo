# MC-CurrencyCalculator-MVP-Demo

INTRODUCTION
This is a demo project implemented to perform currency conversion using the provided MasterCard API. The project is built using Model-View-Presenter design pattern following guidelines the provided in google project samples.

APP FLOW

The app flow follows what was provided in the mock-ups.

1. Open App - Displays activity with 2 cards where with base currency set to USD and target currency set to EUR. App makes network call initially to get the default values. If no network is available, user is prompted to connect to the network by a toast message. Once connected, user can simply pull to refresh the view and get the updates. 
2. Type any value to get the conversion rate. This is calculated locally with the data received from the server in the exchange object.



3. Clicking on the currency name or flag opens up the next activity with RecyclerView. This makes a server call to get the list of currencies once and is stored locally(Shared Preferences) to avoid unnecessary network calls from the device. If the cache is dirty, then a network call is made to update the list.







4. RecyclerView is searchable. The native android SerarchView is used here with the search icon at the top right corner. This is different from the mock up just to use the native android behavior.



ASSUMPTIONS

1. The currency list coming from the server provides only 12 items instead of the complete list. This is ignored since this is a demo API.

FEATURES
1. SwipeRefresh is used to offer user to refresh the current activity.
2. Project is implemented using MVP design pattern
3. Project has two different flavors for ease of deployment and testing
4. Progaurd is used and can be enabled for code obfuscation for added security and to reduce apk size
5. Mockito is used to write tests for classes.
6. Glide is used for loading images efficiently

TODO
1. Change/correct MVP pattern to show Toast and ProgressBar. Currently this is done in presenter directly for each activity
2. Align ActionBar title to center
3. Glide fixes - load drawableStart image instead of ImageView; Dynamically set image size for flag

