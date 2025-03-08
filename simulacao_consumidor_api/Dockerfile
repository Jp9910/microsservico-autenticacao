FROM debian

USER root

COPY ./script_consumo_api.sh /scripts/script_consumo_api.sh

RUN apt update && \
        apt install curl -y && \
        chmod +x /scripts/script_consumo_api.sh

ENTRYPOINT ["/scripts/script_consumo_api.sh"]
