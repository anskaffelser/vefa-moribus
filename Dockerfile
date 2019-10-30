FROM klakegg/saxon:dev-he-graal

ENV SOURCE="moribus" \
    TARGET="/target"

ADD docker /moribus

VOLUME /src /target

WORKDIR /src
ENTRYPOINT ["sh", "/moribus/run.sh"]
