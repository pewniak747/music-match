# music match

Music recommendation engine and REST API.
Uses Play framework and postgresql database for persistence.

## Running

* Install play framework from http://www.playframework.com/download
* Run `createdb musicmatch` to create new postgresql database
* Run `play run` in the project root directory
* Application is accessible under `http://localhost:9000`

## REST api

### Authorization

Authorization is performed with OAuth2 protocol.

```
POST /oauth2/authorize

{
  "grant_type": "password',
  "username": "email@example.com",
  "password": "password",
  "client_id": "clientid"
  "client_secret": "clientseret"
}
```

All request requiring authorization must be performed with `Authorization: Bearer token` header.

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
