# WMCZ Stats Tool

This project gathers important metrics about Wikimedia project revisions made at various WMCZ events. 

## Setup

To set the tool up for local development, you will need:

- Docker
- Docker Compose
- npm/npx (or yarn or a similar package manager, should you so desire)

After cloning the repository, copy `env.example` to `.env`, and fill out the values according to the instructions.

To start the backend, run `docker compose up --build` in the root folder of the repository. This will build the database and the backend of the server, and start it for you. The API will be available at port 8080 by default, under the `/api` path. Please do note that restarting aand rebuilding does not clear the database by default.

To start the frontend in development mode on port 9000, run `npx quasar dev` in the `/quasar` folder. The frontend will query `BACKEND_URL/api`, where `BACKEND_URL` is the value set in the .env file. 
