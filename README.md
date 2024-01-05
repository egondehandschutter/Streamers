Streamer app
==================================
Chess.com is a chess website allowing chess players around the world to play games among eachother. Some of these players stream the matches they play over the internet, on 
twitch.tv or elsewhere. This app essentially shows the list of so called chess.com streamers on twitch.tv.  If a streamer is live on twitch.tv, a 'live icon' is visible for that streamer. The group of live streamers is put in the beginning of the scrollable list, preceding the group of streamers who are currently not live. This real time property of a streamer is refreshed on a regular basis. The static properties of all the streamers are written to a local database to enable offline usage and still be able to get an overview of all existing streamers. On a particicular streamer can be clicked to see some more detail and to provide 2 intents that can be launched to respectively connect to the twitch.tv profile or chess.com profile. The twitch.tv intent will not be enabled if the streamer does not have a twitch which is rarely, but sometimes the case. The app can be used to easily verify which streamers are live and to open the live stream of for example one of the favourite players. New streamers who are active on twitch can be easily detected as well.

Next to a range of other endpoints, Chess.com provides a Restful api service that is accessible through the https://api.chess.com/pub/streamers endpoint.


