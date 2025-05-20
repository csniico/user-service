package com.csniico.userService.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

    @Bean
    OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        System.out.println("✅ Creating OtlpHttpSpanExporter with URL: " + url);
        return OtlpHttpSpanExporter.builder()
                .setEndpoint(url)
                .build();
    }

    @Bean
    public SdkTracerProvider sdkTracerProvider(OtlpHttpSpanExporter exporter) {
        return SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
                .build();
    }

    @Bean
    public OpenTelemetrySdk openTelemetrySdk(SdkTracerProvider sdkTracerProvider) {
        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .build();

        OpenTelemetrySdk.builder().buildAndRegisterGlobal();

        return openTelemetrySdk;
    }
}
