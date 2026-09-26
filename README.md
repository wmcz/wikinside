# Wikinside

This project gathers important metrics about Wikimedia project revisions made at various WMCZ events. 

## Setup

To set the tool up for local development, you will need:

- Docker
- Docker Compose
- Node.js 18 and npm/npx (or yarn or a similar package manager, should you so desire)

### Configuration

After cloning the repository, copy `.env.example` to `.env` and fill out the values:

- `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `DATABASE_ROOT_PASSWORD`: can be anything, but do not change them once the database has been created.
- `METAWIKI_KEY`, `METAWIKI_SECRET`: OAuth client key and secret, see [OAuth credentials](#oauth-credentials) below.
- `BACKEND_URL`: URL where the backend is reachable, without a trailing slash and without `/api`. For local development, use `http://localhost:8070`.
- `DEFAULT_LOCALE`: default frontend language, either `cs-CZ` or `en-US`.
- `TEMP_SUB`: comma-separated list of global user IDs of people allowed to log in. Put your own ID here, otherwise you will not be able to log in. You can find your global user ID at `https://meta.wikimedia.org/w/api.php?action=query&meta=globaluserinfo&guiuser=<your username>` (the `id` field).

Keep comments in `.env` on their own lines; a comment after a value may become part of the value.

### OAuth credentials

Register an OAuth consumer at https://meta.wikimedia.org/wiki/Special:OAuthConsumerRegistration (Wikimedia account required):

- Choose OAuth 2.0.
- Check "This consumer is for use only by (your username)" for local development; such consumers are approved automatically. Other consumers need to be approved by an administrator first.
- Use `http://localhost:8070/api/login/oauth2/code/metawiki` as the callback URL (in general, `BACKEND_URL/api/login/oauth2/code/metawiki`).
- Limit the project to metawiki.
- Keep "Client is confidential" checked.
- Request only "User identity verification only" – no other grants are needed.

Once received, store the client application key as `METAWIKI_KEY` and the client application secret as `METAWIKI_SECRET` in your `.env`.

### Backend

To start the backend, run `docker compose up --build` in the root folder of the repository. This will build the database and the backend of the server, and start it for you. The API will be available at port 8070 by default, under the `/api` path.

Restarting and rebuilding does not clear the database. However, the database is stored in an anonymous Docker volume, so `docker compose down` followed by `docker compose up` starts with an empty database. To deliberately reset the database, run `docker compose down -v`.

Backend tests use an in-memory database and can be run with `./gradlew test` (requires JDK 18).

### Frontend

To start the frontend in development mode on port 9000, run the following in the `quasar` folder:

```sh
npm install
npx quasar dev
```

The frontend reads the `.env` file in the root of the repository and queries `BACKEND_URL/api`. Restart the dev server after changing `.env`.

Open the frontend at `http://localhost:9000`, not `http://127.0.0.1:9000`. Login relies on the backend's session cookie, which only works when the frontend and `BACKEND_URL` use the same hostname.
