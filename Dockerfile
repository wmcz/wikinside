FROM gradle:jdk18-alpine
COPY . /src
WORKDIR /src

RUN gradle --no-daemon build
CMD gradle --no-daemon bootRun
