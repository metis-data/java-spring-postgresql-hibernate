./gradlew clean test --info \
    -Dotel.javaagent.debug=true \
    -Dotel.service.name=java \
    -Dotel.exporter.otlp.endpoint=http://127.0.0.1:4318 \
    -Dotel.exporter.otlp.protocol=http/protobuf \
    -Dotel.instrumentation.common.db-statement-sanitizer.enabled=false \
    -Dotel.instrumentation.hibernate.experimental-span-attributes=true \
    -Dotel.traces.exporter=otlp