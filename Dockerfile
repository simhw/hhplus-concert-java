FROM ubuntu:latest
LABEL authors="shimhyunwoo"

FROM redis:7.2.5-alpine3.20

EXPOSE 6379

CMD ["redis-server"]

