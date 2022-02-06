FROM golang:1.17

WORKDIR /usr/src/app

# pre-copy/cache go.mod for pre-downloading dependencies and only redownloading them in subsequent builds if they change
COPY ./src/go.mod ./src/go.sum ./
RUN go mod download && go mod verify

COPY ./src/main.go ./
RUN go build -v -o /usr/local/bin/app ./...

CMD ["app"]
