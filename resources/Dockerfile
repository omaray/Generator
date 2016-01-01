FROM kiawin/grpc-java-base

RUN mkdir generator
RUN mkdir generator/exe
RUN mkdir generator/lib
RUN mkdir generator/out
RUN mkdir generator/out/bin
RUN mkdir generator/out/bin/server

COPY exe/* /generator/exe/
COPY lib/* /generator/lib/
COPY out/bin/server* /generator/out/bin/server/