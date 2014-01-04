# music match

Music recommendation engine and REST API.
Uses Play framework and postgresql database for persistence.

## Running

* Install play framework from http://www.playframework.com/download
* Run `createdb musicmatch` to create new postgresql database
* Run `play run` in the project root directory
* Application is accessible under `http://localhost:9000`

## REST api

### Songs

Fetch list of songs, optionally filtered.

```
GET /api/songs?filter=bohemian+rhapsody
```

### Scrobbles

Create a scrobble

```
POST /api/me/scrobbles

{
  song_id: 1
}
```

### Scrobble Statistics

Fetch user's total, weekly and monthly listening statistics

```
GET /api/me/scrobbles/statistics
```

### Recommendations

Fetch user's song recommendations

```
GET /api/me/recommendations
```
